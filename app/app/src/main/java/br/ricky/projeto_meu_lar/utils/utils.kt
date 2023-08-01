package br.ricky.projeto_meu_lar.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayInputStream

fun ImageView.base64ToBitmap(base64: String) {
    val decodedBytes: ByteArray = Base64.decode(base64, Base64.DEFAULT)
    val inputStream = ByteArrayInputStream(decodedBytes)
    val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

    this.setImageBitmap(bitmap)
}