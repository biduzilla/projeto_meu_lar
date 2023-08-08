package br.ricky.projeto_meu_lar.model

data class UsuarioResponse(
    var id: String = "",
    var nome: String = "",
    var email: String = "",
    var senha: String = "",
    var telefone: String = "",
    var pets: List<Pet>
)
