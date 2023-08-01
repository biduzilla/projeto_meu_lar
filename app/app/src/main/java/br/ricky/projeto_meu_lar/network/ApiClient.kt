package br.ricky.projeto_meu_lar.network

import br.ricky.projeto_meu_lar.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiClient {

    @POST("usuario/login")
    suspend fun login(@Body loginUser: CredencialUser): Response<LoginUser>

    @POST("usuario/criar")
    suspend fun cadastrarConta(@Body user: UsuarioConta): Response<Void>

    @GET("pet/todos_pets_adotar")
    suspend fun getAllPetsAdotar(@Header("Authorization") token:String): Response<Pets>
}
