package com.example.seed.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

abstract class FirestoreAdapter<VH : RecyclerView.ViewHolder>(private var query: Query?) :
    RecyclerView.Adapter<VH>(),
    EventListener<QuerySnapshot> {

    private var registration: ListenerRegistration? = null

    private val snapshots = ArrayList<DocumentSnapshot>()
    private val ids = ArrayList<String>()

    override fun onEvent(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            Log.w(TAG, "onEvent:error", e)
            onError(e)
            return
        }

        if (documentSnapshots == null) {
            return
        }

        // Dispatch the event
        Log.d(TAG, "onEvent:numChanges:" + documentSnapshots.documentChanges.size)
        for (change in documentSnapshots.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }

        onDataChanged()
    }

    fun startListening() {
        if (query != null && registration == null) {
            registration = query!!.addSnapshotListener(this)
        }
    }

    fun stopListening() {
        registration?.remove()
        registration = null

        snapshots.clear()
        ids.clear()
        notifyDataSetChanged()
    }

    fun setQuery(query: Query) {
        // Stop listening
        stopListening()

        // Listen to new query
        this.query = query
        startListening()
    }

    open fun onError(e: FirebaseFirestoreException) {
        Log.w(TAG, "onError", e)
    }

    open fun onDataChanged() {}

    override fun getItemCount(): Int {
        return snapshots.size
    }

    protected fun getSnapshot(index: Int): DocumentSnapshot {
        return snapshots[index]
    }
    
    protected fun getSnapshotId(index: Int): String {
        return ids[index]
    }

    private fun onDocumentAdded(change: DocumentChange) {
        snapshots.add(change.newIndex, change.document)
        ids.add(change.newIndex, change.document.id)
        notifyItemInserted(change.newIndex)
    }

    private fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            snapshots[change.oldIndex] = change.document
            ids[change.oldIndex] = change.document.id
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            ids.removeAt(change.oldIndex)
            ids.add(change.newIndex, change.document.id)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    private fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        ids.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    companion object {
        private const val TAG = "FirestoreAdapter"
    }
}