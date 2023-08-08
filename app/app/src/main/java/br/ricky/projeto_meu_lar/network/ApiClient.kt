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

    @GET("pet/todos_pets_perdidos_encontrados")
    suspend fun getAllPetsPerdidoEncontrado(@Header("Authorization") token: String): Response<Pets>

    @GET("pet/todos_meus_pets/{idUser}")
    suspend fun getAllMyPets(
        @Header("Authorization") token: String,
        @Path("idUser", encoded = false) idUser: String
    ): Response<Pets>

    @GET("pet/dados_pet/{idPet}")
    suspend fun getPetById(
        @Header("Authorization") token: String,
        @Path("idPet") idPet: String
    ): Response<Pet>

    @POST("pet/cadastrar_pet/{idUser}")
    suspend fun cadastrarPet(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
        @Body pet: PetSalvar
    ): Response<Void>

    @DELETE("pet/apagar_pet/{idPet}/{idUser}")
    suspend fun deletarPost(
        @Header("Authorization") token: String,
        @Path("idPet") idPet: String,
        @Path("idUser") idUser: String,
    ): Response<Void>

    @PUT("pet/atualizar_pet/{idPet}/{idUser}")
    suspend fun atualizarPost(
        @Header("Authorization") token: String,
        @Path("idPet") idPet: String,
        @Path("idUser") idUser: String,
        @Body pet: PetUpdate
    ): Response<Void>

    @GET("usuario/get_user/{idUser}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
    ): Response<UsuarioResponse>

    @GET("usuario/atualizar")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body user:UsuarioRequisicao
    ): Response<Void>

}
