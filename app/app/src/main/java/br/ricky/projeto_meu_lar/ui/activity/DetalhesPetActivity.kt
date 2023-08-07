package br.ricky.projeto_meu_lar.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.ricky.projeto_meu_lar.CHAVE_ID_PET
import br.ricky.projeto_meu_lar.CHAVE_MEU_PET
import br.ricky.projeto_meu_lar.IS_UPDATE
import br.ricky.projeto_meu_lar.data.SharedPref
import br.ricky.projeto_meu_lar.databinding.ActivityDetalhesPetBinding
import br.ricky.projeto_meu_lar.databinding.DialogApagarPetBinding
import br.ricky.projeto_meu_lar.databinding.DialogTokenInvalidBinding
import br.ricky.projeto_meu_lar.extensions.iniciaActivity
import br.ricky.projeto_meu_lar.extensions.tentaCarregarImagem
import br.ricky.projeto_meu_lar.model.Pet
import br.ricky.projeto_meu_lar.network.FirebaseDao
import br.ricky.projeto_meu_lar.repository.PetRepository
import br.ricky.projeto_meu_lar.ui.activity.auth.LoginActivity
import br.ricky.projeto_meu_lar.utils.base64ToBitmap
import kotlinx.coroutines.launch

class DetalhesPetActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesPetBinding.inflate(layoutInflater)
    }
    private val petRepository by lazy {
        PetRepository()
    }
    private lateinit var petRecuperado: Pet
    private lateinit var idUser: String
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        carregaToken()
        carregaUserId()
        tentaCarregarPet()
        configClicks()
    }

    private fun carregaUserId() {
        SharedPref(this).getIdUser()?.let {
            idUser = it
        } ?: run {
            Toast.makeText(baseContext, "Error carregar token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configClicks() {
        with(binding) {
            toolbar.btnVoltar.setOnClickListener { finish() }
            toolbar.btnEditar.setOnClickListener {
                Intent(this@DetalhesPetActivity, FormPetActivity::class.java).apply {
                    putExtra(CHAVE_ID_PET, petRecuperado.id)
                    putExtra(IS_UPDATE, true)
                    startActivity(this)
                }
            }
        }
    }

    private fun carregaToken() {
        SharedPref(this).getToken()?.let {
            token = it
        } ?: run {
            Toast.makeText(baseContext, "Error carregar token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tentaCarregarPet() {
        val petId = intent.getStringExtra(CHAVE_ID_PET)
        val isMyPet = intent.getBooleanExtra(CHAVE_MEU_PET, false)

        if (!isMyPet) {
            esconderMenu()
        }

        lifecycleScope.launch {
            petId?.let {
                petRepository.getPetById(
                    activity = this@DetalhesPetActivity,
                    idPet = it,
                    token = token
                )?.let { pet ->
                    petRecuperado = pet

                    carregaDados()
                }

            }
        }
    }

    private fun carregaDados() {
        with(binding) {
            val status = when (petRecuperado.status) {
                "ADOTAR" -> {
                    "Adoção"
                }
                "ENCONTRADO" -> {
                    "Encontrado"
                }
                "PERDIDO" -> {
                    "Status Perdido"
                }
                else -> {
                    "Error"
                }
            }

            "${petRecuperado.nomePet} - $status".also { tvNomePet.text = it }
            tvDesc.text = petRecuperado.descricao
            "Tamanho: ${petRecuperado.tamanho} ".also { tvTamanho.text = it }
            "${petRecuperado.telefone} ${petRecuperado.nomeContato}".also { btnLigar.text = it }
            imgPet.tentaCarregarImagem(petRecuperado.imagem)

            toolbar.tvTitulo.text = status

            progressCircular.visibility = View.GONE
            scrollView.visibility = View.VISIBLE

        }
    }

    private fun esconderMenu() {
        with(binding) {
            toolbar.btnDelete.visibility = View.GONE
            toolbar.btnEditar.visibility = View.GONE
        }
    }

    private fun dialogExcluirPost() {
        DialogApagarPetBinding.inflate(layoutInflater).apply {
            val dialog = AlertDialog.Builder(this@DetalhesPetActivity)
                .setView(root)
                .create()

            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()

            btnApagar.setOnClickListener {
                apagarPost()
            }
            btnCancelar.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun apagarPost() {
        lifecycleScope.launch {
            petRepository.apagarPost(
                activity = this@DetalhesPetActivity,
                token = token,
                idUser = idUser,
                idPet = petRecuperado.id
            ).apply {
                if (this) {
                    FirebaseDao().apagarImagemFirebase(petRecuperado.id)
                    finish()
                }
            }
        }
    }
}