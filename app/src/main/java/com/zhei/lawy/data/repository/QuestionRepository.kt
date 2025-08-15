package com.zhei.lawy.data.repository
import com.google.firebase.firestore.FirebaseFirestore
import com.zhei.lawy.domain.repository.IQuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class QuestionRepository : IQuestionRepository {

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override suspend fun updateQuestion(map: Map<String, Any>) {
        withContext(Dispatchers.IO) {
            try {
                firestore.collection("response").document("questions").update(map)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override suspend fun isActivate() : Flow<Boolean?> = callbackFlow {
        val response = firestore
            .collection("response")
            .document("questions").addSnapshotListener { value, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (value != null) {
                    trySend(value.getBoolean("activate"))
                }
            }
        awaitClose { response.remove() }
    }
}