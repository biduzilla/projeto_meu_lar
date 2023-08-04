package br.ricky.projeto_meu_lar.data

import android.app.Activity
import android.content.Context
import br.ricky.projeto_meu_lar.CHAVE_ID_USER
import br.ricky.projeto_meu_lar.CHAVE_NOME_USER
import br.ricky.projeto_meu_lar.CHAVE_TOKEN
import br.ricky.projeto_meu_lar.model.LoginUser

class SharedPref(activity: Activity) {
    private val sharedPreferences = activity.getSharedPreferences(CHAVE_TOKEN, Context.MODE_PRIVATE)

    fun salvarToken(token: String) {
        sharedPreferences.edit()
            .putString(CHAVE_TOKEN, token)
            .apply()
    }

    fun salvarIdUser(idUser: String) {
        sharedPreferences.edit()
            .putString(CHAVE_ID_USER, idUser)
            .apply()
    }

    fun salvarNomeUser(nomeUser: String) {
        sharedPreferences.edit()
            .putString(CHAVE_NOME_USER, nomeUser)
            .apply()
    }

    fun getDadosLogin(): LoginUser {
        val dados = mutableListOf<String>()
        with(sharedPreferences) {
            val token = getString(CHAVE_TOKEN, "")
            val idUser = getString(CHAVE_ID_USER, "")
            val nomeUser = getString(CHAVE_NOME_USER, "")

            return LoginUser(token = token!!, idUser = idUser!!, nome = nomeUser!!)
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString(CHAVE_TOKEN, "")
    }

    fun getIdUser(): String? {
        return sharedPreferences.getString(CHAVE_ID_USER, "")
    }
    fun getNomeUser(): String? {
        return sharedPreferences.getString(CHAVE_NOME_USER, "")
    }

}