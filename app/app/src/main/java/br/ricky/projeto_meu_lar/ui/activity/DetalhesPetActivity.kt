package br.ricky.projeto_meu_lar.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.ricky.projeto_meu_lar.CHAVE_ID_PET
import br.ricky.projeto_meu_lar.CHAVE_MEU_PET
import br.ricky.projeto_meu_lar.data.SharedPref
import br.ricky.projeto_meu_lar.databinding.ActivityDetalhesPetBinding
import br.ricky.projeto_meu_lar.model.Pet
import br.ricky.projeto_meu_lar.repository.PetRepository
import br.ricky.projeto_meu_lar.utils.base64ToBitmap
import kotlinx.coroutines.launch

class DetalhesPetActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesPetBinding.inflate(layoutInflater)
    }
    private lateinit var petRecuperado: Pet
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        carregaToken()
        tentaCarregarVaga()
    }

    private fun carregaToken() {
        SharedPref(this).getToken()?.let {
            token = it
        } ?: run {
            Toast.makeText(baseContext, "Error carregar token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tentaCarregarVaga() {
        val petId = intent.getStringExtra(CHAVE_ID_PET)
        val isMyPet = intent.getBooleanExtra(CHAVE_MEU_PET, false)

        if (!isMyPet) {
            esconderMenu()
        }

        lifecycleScope.launch {
            petId?.let {
                PetRepository().getPetById(baseContext, idPet = it, token = token)?.let { pet ->
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
            imgPet.base64ToBitmap(petRecuperado.imagem)

            with(binding) {
                progressCircular.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
            }
        }
    }

    private fun esconderMenu() {
        with(binding) {
            toolbar.btnDelete.visibility = View.GONE
            toolbar.btnEditar.visibility = View.GONE
        }
    }
}