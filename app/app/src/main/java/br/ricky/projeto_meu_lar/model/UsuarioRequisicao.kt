package br.ricky.projeto_meu_lar.model

data class UsuarioRequisicao(
    var id: String = "",
    var email: String = "",
    var senha: String = "",
    var telefone: String = "",
    var nome: String = "",
    var pets: List<Pet> = emptyList()
)
