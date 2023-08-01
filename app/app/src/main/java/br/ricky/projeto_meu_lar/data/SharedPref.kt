package br.ricky.projeto_meu_lar.data

import android.app.Activity
import android.content.Context
import br.ricky.projeto_meu_lar.CHAVE_TOKEN

class SharedPref(activity:Activity) {
    private val sharedPreferences = activity.getSharedPreferences(CHAVE_TOKEN, Context.MODE_PRIVATE)

    fun salvarToken(token:String){
        sharedPreferences.edit()
            .putString(CHAVE_TOKEN, token)
            .apply()
    }

    fun getToken():String?{
        return sharedPreferences.getString(CHAVE_TOKEN, "")
    }
}