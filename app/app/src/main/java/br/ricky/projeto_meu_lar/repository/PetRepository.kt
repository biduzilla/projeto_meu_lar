package br.ricky.projeto_meu_lar.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import br.ricky.projeto_meu_lar.model.ErrorMensagem
import br.ricky.projeto_meu_lar.model.Pet
import br.ricky.projeto_meu_lar.model.Pets
import br.ricky.projeto_meu_lar.network.RetrofitInstance
import br.ricky.projeto_meu_lar.utils.dialogLogarNovamente
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class PetRepository {

    suspend fun getAllPetsAdotar(activity: Activity, token: String): List<Pet>? {
        val response: Response<Pets> = try {
            RetrofitInstance.api.getAllPetsAdotar(token)
        } catch (e: IOException) {
            Toast.makeText(activity.baseContext, "Problema com conexão", Toast.LENGTH_SHORT).show()
            return null
        } catch (e: HttpException) {
            Toast.makeText(activity.baseContext, "Resposta inesperada", Toast.LENGTH_SHORT).show()
            return null
        }

        if (response.isSuccessful) {
            response.body()?.let {
                return it.pets
            }
        } else {
            if (response.code() == 403) {
                dialogLogarNovamente(activity)
            } else {
                try {
                    val mensagemError = Gson().fromJson(
                        response
                            .errorBody()
                            ?.charStream(),
                        ErrorMensagem::class.java
                    )

                    mensagemError?.let {
                        Toast.makeText(
                            activity.baseContext,
                            mensagemError.error[0],
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        activity.baseContext,
                        "Error ao tentar se conectar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return null
    }

    suspend fun getAllMyPets(activity: Activity, idUser: String, token: String): List<Pet>? {
        try {
            val response = RetrofitInstance.api.getAllMyPets(token, idUser)

            if (response.isSuccessful) {
                response.body()?.let {
                    return it.pets
                }
            } else {
                if (response.code() == 403) {
                    dialogLogarNovamente(activity)
                } else {
                    val mensagemError = Gson().fromJson(
                        response
                            .errorBody()
                            ?.charStream(),
                        ErrorMensagem::class.java
                    )
                    mensagemError?.let {
                        Toast.makeText(
                            activity.baseContext,
                            mensagemError.error[0],
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(activity.baseContext, "Error ao tentar se conectar", Toast.LENGTH_SHORT)
                .show()
        }
        return null
    }

    suspend fun getPetById(activity: Activity, idPet: String, token: String): Pet? {
        try {
            val response = RetrofitInstance.api.getPetById(token, idPet)

            if (response.isSuccessful) {
                response.body()?.let {
                    return it
                }
            } else {
                if (response.code() == 403) {
                    dialogLogarNovamente(activity)
                } else {
                    val mensagemError = Gson().fromJson(
                        response
                            .errorBody()
                            ?.charStream(),
                        ErrorMensagem::class.java
                    )

                    mensagemError?.let {
                        Toast.makeText(
                            activity.baseContext,
                            mensagemError.error[0],
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(activity.baseContext, "Error ao tentar se conectar", Toast.LENGTH_SHORT)
                .show()
        }
        return null
    }
}