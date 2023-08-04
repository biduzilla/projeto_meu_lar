package br.ricky.projeto_meu_lar.ui.activity.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import br.ricky.projeto_meu_lar.databinding.ActivityCadastrarContaBinding
import br.ricky.projeto_meu_lar.model.UsuarioConta
import br.ricky.projeto_meu_lar.repository.UserRepository
import kotlinx.coroutines.launch

class CadastrarContaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastrarContaBinding.inflate(layoutInflater)
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
        binding.btnVoltar.setOnClickListener { finish() }
        binding.btnCadastrar.setOnClickListener { validaDados() }
    }

    private fun validaDados() {
        ocultarTeclado()
        with(binding) {
            val nome = edtNome.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val telefone = edtTelefone.text.toString().trim()
            val senha = edtSenha.text.toString().trim()
            val confirmarSenha = edtConfirmarSenha.text.toString().trim()

            when {
                nome.isEmpty() -> {
                    edtNome.requestFocus()
                    edtNome.error = "Campo Obrigatório"
                }
                email.isEmpty() -> {
                    edtEmail.requestFocus()
                    edtEmail.error = "Campo Obrigatório"
                }
                telefone.isEmpty() -> {
                    edtTelefone.requestFocus()
                    edtTelefone.error = "Campo Obrigatório"
                }
                senha.isEmpty() -> {
                    edtSenha.requestFocus()
                    edtSenha.error = "Campo Obrigatório"
                }
                confirmarSenha.isEmpty() -> {
                    edtConfirmarSenha.requestFocus()
                    edtConfirmarSenha.error = "Campo Obrigatório"
                }
                senha != confirmarSenha -> {
                    edtConfirmarSenha.requestFocus()
                    edtConfirmarSenha.error = "Senhas devem ser iguais"
                }
                else -> {
                    progressCircular.visibility = View.VISIBLE
                    btnCadastrar.visibility = View.GONE
                    criarConta(nome, email, telefone, senha)
                }
            }
        }
    }

    private fun ocultarTeclado() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.btnCadastrar.windowToken, 0)
    }

    private fun criarConta(nome: String, email: String, telefone: String, senha: String) {
        val user = UsuarioConta(
            email = email,
            senha = senha,
            telefone = telefone,
            nome = nome,
        )
        lifecycleScope.launch {
            if (userRepository.criarConta(user, baseContext)) {
                finish()
            }else{
                with(binding) {
                    progressCircular.visibility = View.GONE
                    btnCadastrar.visibility = View.VISIBLE
                }
            }
        }

    }
}