package com.example.seed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.seed.data.User
import com.example.seed.databinding.ActivityInitializeUserBinding
import com.example.seed.viewmodel.UserViewModel

class InitializeUserActivity : AppCompatActivity() {

    companion object {
        const val TAG = "InitializeUserActivity"
    }

    private lateinit var binding : ActivityInitializeUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitializeUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(LoginActivity.ACCOUNT_KEY)){
            val uid = intent.getStringExtra(LoginActivity.ACCOUNT_KEY)!!

            binding.btnEditProfile.setOnClickListener {
                Log.d(TAG, "clicked button: uid=$uid")

                val newUser = User(
                    username = binding.etUsername.text.toString(),
                    uid = uid,
                    bio = binding.etBio.text.toString()
                )

                UserViewModel.updateUser(
                    uid,
                    newUser,
                    ::launchMainActivity
                )
            }
        } else {
            Toast.makeText(this, "Error! uid is null", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun launchMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}