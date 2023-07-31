package br.ricky.projeto_meu_lar.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.ricky.projeto_meu_lar.R
import br.ricky.projeto_meu_lar.extensions.iniciaActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            iniciaActivity(LoginActivity::class.java)
            finish()
        }, 3000)
    }
}