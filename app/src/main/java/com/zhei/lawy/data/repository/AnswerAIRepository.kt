package com.zhei.lawy.data.repository
import com.google.firebase.firestore.FirebaseFirestore
import com.zhei.lawy.domain.repository.IAnswerAIRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AnswerAIRepository : IAnswerAIRepository {

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override suspend fun getAnswerWithFlow(): Flow<Any?> = callbackFlow {
        val listener = firestore.collection("response")
            .document("answers").addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (value != null) {
                    trySend(value.get("answer"))
                }
            }

        awaitClose { listener.remove() }
    }


    override suspend fun isCurrent(): Flow<Boolean?> = callbackFlow {
        val listener = firestore.collection("response")
            .document("answers").addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (value != null) {
                    trySend(value.getBoolean("isCurrent"))
                }
            }
        awaitClose { listener.remove() }
    }
}