package br.ricky.projeto_meu_lar.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import br.ricky.projeto_meu_lar.databinding.DialogTokenInvalidBinding
import br.ricky.projeto_meu_lar.extensions.iniciaActivity
import br.ricky.projeto_meu_lar.ui.activity.auth.LoginActivity
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun ImageView.base64ToBitmap(base64: String) {
    val decodedBytes: ByteArray = Base64.decode(base64, Base64.DEFAULT)
    val inputStream = ByteArrayInputStream(decodedBytes)
    val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

    this.setImageBitmap(bitmap)
}

fun dialogLogarNovamente(activity: Activity) {
    DialogTokenInvalidBinding.inflate(activity.layoutInflater).apply {
        val dialog = AlertDialog.Builder(activity)
            .setView(root)
            .create()

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        btnLogin.setOnClickListener {
            activity.iniciaActivity(LoginActivity::class.java)
            activity.finish()
        }
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun uriToBitmap(activity: Activity,uri: Uri): Bitmap? {
    try {
        val inputStream = activity.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

