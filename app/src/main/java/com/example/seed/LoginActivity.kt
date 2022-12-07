package com.example.seed

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import com.example.seed.databinding.ActivityLoginBinding
import com.example.seed.viewmodel.UserViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity

import com.google.android.gms.auth.api.identity.SignInClient

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val GOOGLE_CLIENT_ID = "658086026451-u8c5epi2fgaeh1p81k3udgcoqtf7cm0r.apps.googleusercontent.com"
        private const val TAG = "LogInActivity"
        const val ACCOUNT_KEY = "ACCOUNT"
    }

    private lateinit var binding : ActivityLoginBinding

    private lateinit var signInClient: SignInClient
    private lateinit var auth : FirebaseAuth
    private val signInLauncher = registerForActivityResult(StartIntentSenderForResult()) {
            result -> handleSignInResult(result.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        signInClient = Identity.getSignInClient(this)

        binding.btnSignIn.setOnClickListener {
            signIn()
        }

        val currentUser = auth.currentUser
        if (currentUser == null) {
            oneTapSignIn()
        }
    }

    private fun handleSuccessfulSignIn(user: FirebaseUser) {
        UserViewModel.userCollection.document(user.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val document = task.result
                    if (document.exists()){
                        Log.d(TAG, "signed in user")
                        initializeMainActivity()
                    } else {
                        Log.d(TAG, "signing up user...")
                        initializeUser(user.uid)
                    }
                } else {
                    Log.d(TAG, "Error ${task.exception}")
                }
            }
    }

    private fun initializeUser(uid: String) {
        val intentDetails = Intent()
        intentDetails.setClass(
            this, InitializeUserActivity::class.java
        )
        intentDetails.putExtra(
            ACCOUNT_KEY, uid
        )
        startActivity(intentDetails)
    }

    private fun initializeMainActivity(){
        val intentDetails = Intent()
        intentDetails.setClass(
            this, MainActivity::class.java
        )
//        intentDetails.putExtra(
//            USER_KEY, user
//        )
        startActivity(intentDetails)
    }

    private fun handleSignInResult(data: Intent?) {
        // Result returned from launching the Sign In PendingIntent
        try {
            // Google Sign In was successful, authenticate with Firebase
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                firebaseAuthWithGoogle(idToken)
            } else {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser!!
                    handleSuccessfulSignIn(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun signIn() {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(GOOGLE_CLIENT_ID)
            .build()

        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Google Sign-in failed", e)
            }
    }

    private fun oneTapSignIn() {
        // Configure One Tap UI
        val oneTapRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(GOOGLE_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

        // Display the One Tap UI
        signInClient.beginSignIn(oneTapRequest)
            .addOnSuccessListener { result ->
                launchSignIn(result.pendingIntent)
            }
            .addOnFailureListener { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
            }
    }

    private fun launchSignIn(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't start Sign In: ${e.localizedMessage}")
        }
    }
}