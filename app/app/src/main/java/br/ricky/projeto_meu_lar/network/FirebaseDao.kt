package br.ricky.projeto_meu_lar.network

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage

class FirebaseDao {
    fun salvarImagemPetFirebase(
        imagem: String,
        activity: Activity,
        petId:String,
        urlCriado: (url: String?) -> Unit
    ) {
        val storage = FirebaseStorage.getInstance().reference
            .child("imagens")
            .child("pets")
            .child(petId)
            .child("pet.jpeg")

        storage.putFile(Uri.parse(imagem)).apply {
            addOnSuccessListener {
                storage.downloadUrl.addOnCompleteListener { task ->
                    urlCriado(task.result.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(activity.baseContext, it.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

}