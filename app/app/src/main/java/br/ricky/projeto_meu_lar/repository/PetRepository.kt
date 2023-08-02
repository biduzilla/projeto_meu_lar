package br.ricky.projeto_meu_lar.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import br.ricky.projeto_meu_lar.model.ErrorMensagem
import br.ricky.projeto_meu_lar.model.Pet
import br.ricky.projeto_meu_lar.model.Pets
import br.ricky.projeto_meu_lar.network.RetrofitInstance
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class PetRepository {

    suspend fun getAllPetsAdotar(context: Context, token: String): List<Pet>? {
        val response: Response<Pets> = try {
            RetrofitInstance.api.getAllPetsAdotar(token)
        } catch (e: IOException) {
            Toast.makeText(context, "Problema com conexão", Toast.LENGTH_SHORT).show()
            return null
        } catch (e: HttpException) {
            Toast.makeText(context, "Resposta inesperada", Toast.LENGTH_SHORT).show()
            return null
        }

        if (response.isSuccessful) {
            response.body()?.let {
                return it.pets
            }
        } else {
            try {
                val mensagemError = Gson().fromJson(
                    response
                        .errorBody()
                        ?.charStream(),
                    ErrorMensagem::class.java
                )

                mensagemError?.let {
                    Toast.makeText(context, mensagemError.error[0], Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.i("infoteste", "getAllPetsAdotar: ${response.body().toString()}")
                Toast.makeText(context, "Error ao tentar se conectar", Toast.LENGTH_SHORT).show()
            }

        }
        return null
    }

    suspend fun getAllMyPets(context: Context, idUser: String, token: String): List<Pet>? {
        try {
            val response = RetrofitInstance.api.getAllMyPets(token, idUser)

            if (response.isSuccessful) {
                response.body()?.let {
                    return it.pets
                }
            } else {
                val mensagemError = Gson().fromJson(
                    response
                        .errorBody()
                        ?.charStream(),
                    ErrorMensagem::class.java
                )

                mensagemError?.let {
                    Toast.makeText(context, mensagemError.error[0], Toast.LENGTH_SHORT).show()
                }
                return null
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error ao tentar se conectar", Toast.LENGTH_SHORT).show()
        }
        return null
    }

    suspend fun getPetById(context: Context, idPet: String, token: String): Pet? {
        try {
            val response = RetrofitInstance.api.getPetById(token, idPet)

            if (response.isSuccessful) {
                response.body()?.let {
                    return it
                }
            } else {
                val mensagemError = Gson().fromJson(
                    response
                        .errorBody()
                        ?.charStream(),
                    ErrorMensagem::class.java
                )

                mensagemError?.let {
                    Toast.makeText(context, mensagemError.error[0], Toast.LENGTH_SHORT).show()
                }
                return null
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error ao tentar se conectar", Toast.LENGTH_SHORT).show()
        }
        return null
    }
}