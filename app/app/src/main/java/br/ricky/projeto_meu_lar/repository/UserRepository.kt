package br.ricky.projeto_meu_lar.repository

import android.app.Activity
import android.content.Context
import android.widget.Toast
import br.ricky.projeto_meu_lar.model.*
import br.ricky.projeto_meu_lar.network.RetrofitInstance
import br.ricky.projeto_meu_lar.utils.dialogLogarNovamente
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserRepository {
    suspend fun login(login: CredencialUser, context: Context): LoginUser? {
        try {
            val response: Response<LoginUser> =
                RetrofitInstance.api.login(login)

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
            }

        } catch (e: Exception) {
            Toast.makeText(context, "Error ao tentar se conectar", Toast.LENGTH_SHORT).show()
        }
        return null
    }

    suspend fun criarConta(user: UsuarioConta, context: Context): Boolean {
        try {
            val response: Response<Void> =
                RetrofitInstance.api.cadastrarConta(user)
            return if (response.isSuccessful) {
                Toast.makeText(context, "Conta criada com sucesso", Toast.LENGTH_SHORT).show()
                true
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
                false
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error ao tentar se conectar", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    suspend fun getUserById(activity: Activity, token: String, idUser: String): UsuarioResponse? {
        try {
            val response: Response<UsuarioResponse> =
                RetrofitInstance.api.getUserById(token = token, idUser = idUser)
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

    suspend fun updateUser(user: UsuarioRequisicao, activity: Activity, token: String): Boolean {
        try {
            val response: Response<Void> =
                RetrofitInstance.api.updateUser(token = token, user = user)
            if (response.isSuccessful) {
                    return true
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
        return false
    }
}