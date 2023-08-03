package br.ricky.projeto_meu_lar.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.ricky.projeto_meu_lar.databinding.ActivityFormPetBinding

class FormPetActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityFormPetBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}