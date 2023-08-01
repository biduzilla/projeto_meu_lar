package br.ricky.projeto_meu_lar.repository

import android.content.Context
import android.widget.Toast
import br.ricky.projeto_meu_lar.model.CredencialUser
import br.ricky.projeto_meu_lar.model.ErrorMensagem
import br.ricky.projeto_meu_lar.model.LoginUser
import br.ricky.projeto_meu_lar.model.UsuarioConta
import br.ricky.projeto_meu_lar.network.RetrofitInstance
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserRepository {
    suspend fun login(login: CredencialUser, context: Context): LoginUser? {
        val response: Response<LoginUser> = try {
            RetrofitInstance.api.login(login)
        } catch (e: IOException) {
            Toast.makeText(context, "Sem internet", Toast.LENGTH_SHORT).show()
            return null
        } catch (e: HttpException) {
            Toast.makeText(context, "Resposta inesperada", Toast.LENGTH_SHORT).show()
            return null
        }
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
        return null
    }

    suspend fun criarConta(user: UsuarioConta, context: Context): Boolean {
        val response: Response<Void> =
            try {
                RetrofitInstance.api.cadastrarConta(user)
            } catch (e: IOException) {
                Toast.makeText(context, "Sem internet", Toast.LENGTH_SHORT).show()
                return false
            } catch (e: HttpException) {
                Toast.makeText(context, "Resposta inesperada", Toast.LENGTH_SHORT).show()
                return false
            }

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
    }
}