package com.zhei.lawy.data.repository
import com.google.firebase.firestore.FirebaseFirestore
import com.zhei.lawy.domain.repository.IAnswerAIRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AnswerAIRepository : IAnswerAIRepository {

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override suspend fun getAnswerWithFlow(): Flow<String> = callbackFlow {
        val listener = firestore.collection("response")
            .document("answers")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val response = value?.get("answer").toString()
                trySend(response).isSuccess
            }
        awaitClose { listener.remove() }
    }


    override suspend fun isCurrent(): Flow<Boolean> = callbackFlow {
        val listener = firestore.collection("response")
            .document("answers")
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }

                val value = snapshot?.getBoolean("isCurrent") ?: false
                trySend(value).isSuccess
            }
        awaitClose { listener.remove() }
    }
}