package br.ricky.projeto_meu_lar.network

import br.ricky.projeto_meu_lar.model.CredencialUser
import br.ricky.projeto_meu_lar.model.LoginUser
import br.ricky.projeto_meu_lar.model.UsuarioConta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiClient {

    @POST("usuario/login")
    suspend fun login(@Body loginUser: CredencialUser): Response<LoginUser>

    @POST("usuario/criar")
    suspend fun cadastrarConta(@Body user:UsuarioConta):Response<Void>
}
