package br.ricky.projeto_meu_lar.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: ApiClient by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.32:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
    }
}