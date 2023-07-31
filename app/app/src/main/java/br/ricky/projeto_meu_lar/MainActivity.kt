package br.ricky.projeto_meu_lar

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.ricky.projeto_meu_lar.databinding.ActivityMainBinding
import br.ricky.projeto_meu_lar.model.CredencialUser
import br.ricky.projeto_meu_lar.model.LoginUser
import br.ricky.projeto_meu_lar.network.RetrofitInstance
import br.ricky.projeto_meu_lar.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val userRepository by lazy {
        UserRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onClick()
    }

    private fun onClick() {
        binding.btnLogin.setOnClickListener {
            validaDados()
        }
    }

    private fun validaDados() {
        with(binding) {
            val email = edtEmail.text.toString().trim()
            val senha = edtSenha.text.toString().trim()

            when {
                email.isEmpty() -> {
                    edtEmail.requestFocus()
                    edtEmail.error = "Campo Obrigatório"
                }
                senha.isEmpty() -> {
                    edtSenha.requestFocus()
                    edtSenha.error = "Campo Obrigatório"
                }
                else -> {
                    progressCircular.visibility = View.VISIBLE
                    btnLogin.visibility = View.GONE

                    lifecycleScope.launch { login(email, senha) }
                    progressCircular.visibility = View.GONE
                    btnLogin.visibility = View.VISIBLE
                }
            }
        }
    }

    private suspend fun login(email: String, senha: String) {
        val login: CredencialUser = CredencialUser(email, senha)

        userRepository.login(login, baseContext)
    }
}