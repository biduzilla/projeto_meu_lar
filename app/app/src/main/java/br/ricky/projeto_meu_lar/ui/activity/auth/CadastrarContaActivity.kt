package br.ricky.projeto_meu_lar.ui.activity.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.ricky.projeto_meu_lar.IS_UPDATE
import br.ricky.projeto_meu_lar.data.SharedPref
import br.ricky.projeto_meu_lar.databinding.ActivityCadastrarContaBinding
import br.ricky.projeto_meu_lar.model.UsuarioConta
import br.ricky.projeto_meu_lar.model.UsuarioResponse
import br.ricky.projeto_meu_lar.repository.UserRepository
import kotlinx.coroutines.launch

class CadastrarContaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastrarContaBinding.inflate(layoutInflater)
    }
    private val userRepository by lazy {
        UserRepository()
    }
    private var isUpdate: Boolean = false
    private var token: String = ""
    private var idUser: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        isUpdate()
        onClick()
    }

    private fun isUpdate() {
        isUpdate = intent.getBooleanExtra(IS_UPDATE, false)

        if (isUpdate) {
            binding.scrollView2.visibility = View.GONE
            binding.progressCircular.visibility = View.VISIBLE
            carregaUserId()
            carregaToken()
            recuperaDados()
            alteraTxt()
        }
    }

    private fun alteraTxt() {
        with(binding) {
            tvTitulo.text = "Alterar dados"
            btnCadastrar.text = "Atualizar"
        }
    }

    private fun recuperaDados() {
        lifecycleScope.launch {
            userRepository.getUserById(this@CadastrarContaActivity, token, idUser)
                ?.let { userRecuperado ->
                    colocaDados(userRecuperado)
                }
            binding.scrollView2.visibility = View.VISIBLE
            binding.progressCircular.visibility = View.GONE
        }

    }

    private fun carregaToken() {
        SharedPref(this).getToken()?.let {
            token = it
        } ?: run {
            Toast.makeText(baseContext, "Error carregar token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregaUserId() {
        SharedPref(this).getIdUser()?.let {
            idUser = it
        } ?: run {
            Toast.makeText(baseContext, "Error carregar token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun colocaDados(user: UsuarioResponse) {
        with(binding) {
            edtNome.setText(user.nome)
            edtEmail.setText(user.email)
            edtTelefone.setText(user.telefone)
        }
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
            } else {
                with(binding) {
                    progressCircular.visibility = View.GONE
                    btnCadastrar.visibility = View.VISIBLE
                }
            }
        }

    }
}