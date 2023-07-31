package br.ricky.projeto_meu_lar.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import br.ricky.projeto_meu_lar.model.CredencialUser
import br.ricky.projeto_meu_lar.model.ErrorMensagem
import br.ricky.projeto_meu_lar.model.LoginUser
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
                val user: LoginUser = it
                Toast.makeText(context, "Sucesso", Toast.LENGTH_SHORT).show()
                return user
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
}