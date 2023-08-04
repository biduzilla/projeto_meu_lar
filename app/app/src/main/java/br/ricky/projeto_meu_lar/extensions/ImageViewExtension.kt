package br.ricky.projeto_meu_lar.extensions

import android.widget.ImageView
import br.ricky.projeto_meu_lar.R
import coil.load

fun ImageView.tentaCarregarImagem(url: String) {
    load(url) {
        placeholder(R.drawable.placeholder)
    }
}