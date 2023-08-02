package br.ricky.projeto_meu_lar.network

import br.ricky.projeto_meu_lar.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @POST("usuario/login")
    suspend fun login(@Body loginUser: CredencialUser): Response<LoginUser>

    @POST("usuario/criar")
    suspend fun cadastrarConta(@Body user: UsuarioConta): Response<Void>

    @GET("pet/todos_pets_adotar")
    suspend fun getAllPetsAdotar(@Header("Authorization") token: String): Response<Pets>

    @GET("pet/todos_meus_pets/{idUser}")
    suspend fun getAllMyPets(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String
    ): Response<Pets>

    @GET("pet/dados_pet/{idPet}")
    suspend fun getPetById(
        @Header("Authorization") token: String,
        @Path("idPet") idPet: String
    ): Response<Pet>
}
