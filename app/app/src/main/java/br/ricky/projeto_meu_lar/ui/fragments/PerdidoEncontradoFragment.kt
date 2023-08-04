package br.ricky.projeto_meu_lar.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.ricky.projeto_meu_lar.*
import br.ricky.projeto_meu_lar.data.SharedPref
import br.ricky.projeto_meu_lar.databinding.FragmentHomeBinding
import br.ricky.projeto_meu_lar.databinding.FragmentPerdidoEncontradoBinding
import br.ricky.projeto_meu_lar.model.LoginUser
import br.ricky.projeto_meu_lar.model.Pet
import br.ricky.projeto_meu_lar.repository.PetRepository
import br.ricky.projeto_meu_lar.ui.activity.DetalhesPetActivity
import br.ricky.projeto_meu_lar.ui.activity.FormPetActivity
import br.ricky.projeto_meu_lar.ui.adapter.PetAdapter
import kotlinx.coroutines.launch


class PerdidoEncontradoFragment : Fragment() {
    private var _binding: FragmentPerdidoEncontradoBinding? = null
    private val binding get() = _binding!!
    private var isPerdido: Boolean = false
    private var isEncontrado: Boolean = false
    private var isMyPet: Boolean = false
    private val laranja: Int = Color.parseColor("#f8a300")
    private val branco: Int = Color.parseColor("#FFFFFFFF")
    private val adapter = PetAdapter()
    private var petsRecuperados = mutableListOf<Pet>()
    private var meusPetsRecuperados = mutableListOf<Pet>()
    private var loginUser: LoginUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerdidoEncontradoBinding.inflate(inflater, container, false)

        configClicks()
        carregaDadosPref()
        configRv()
        recuperaPets()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        meusPetsRecuperados.clear()
        petsRecuperados.clear()
        recuperaPets()
    }

    private fun recuperaPets() {

        loginUser?.token?.let { token ->

            lifecycleScope.launch {
                PetRepository().getAllPetsPerdidoEncontrado(requireActivity(), token)
                    ?.let { petsList ->
                        petsRecuperados = petsList.toMutableList()
                        adapter.atualiza(petsList)

                        with(binding) {
                            progressCircular.visibility = View.GONE
                            rv.visibility = View.VISIBLE
                        }
                    }
            }

            lifecycleScope.launch {
                PetRepository().getAllMyPets(
                    requireActivity(),
                    idUser = loginUser!!.idUser,
                    token = token
                )
                    ?.let { petsList ->
                        petsList.forEach {
                            if (it.status != "ADOTAR") {
                                meusPetsRecuperados.add(it)
                            }
                        }
                    }
            }
        }
    }

    private fun configRv() {
        with(binding) {
            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(requireActivity())
            adapter.onClick = { pet ->
                Intent(requireContext(), DetalhesPetActivity::class.java).apply {

                    val meuPet: Boolean = meusPetsRecuperados.contains(pet)

                    putExtra(CHAVE_ID_PET, pet.id)
                    putExtra(CHAVE_MEU_PET, meuPet)
                    startActivity(this)
                }
            }
        }
    }

    private fun carregaDadosPref() {
        loginUser = SharedPref(requireActivity()).getDadosLogin()
    }

    private fun configClicks() {
        with(binding) {
            btnAdd.setOnClickListener {
                Intent(requireContext(), FormPetActivity::class.java).apply {
                    putExtra(IS_UPDATE, false)
                    putExtra(IS_ADOCAO, false)
                    startActivity(this)
                }
            }
            cardPerdido.setOnClickListener {
                adapter.atualiza(emptyList())
                changeCardPerdidoColor()
            }

            cardEncontrado.setOnClickListener {
                adapter.atualiza(emptyList())
                changeCardEncontradoColor()
            }

            cardMeuPet.setOnClickListener {
                adapter.atualiza(emptyList())
                changeCardMeuPetColor()
            }
        }
    }

    private fun FragmentPerdidoEncontradoBinding.changeCardMeuPetColor() {
        if (isMyPet) {
            cardMeuPet.setCardBackgroundColor(branco)
            tvMeusPets.setTextColor(laranja)

            isMyPet = false

            adapter.atualiza(petsRecuperados)
        } else {
            cardMeuPet.setCardBackgroundColor(laranja)
            tvMeusPets.setTextColor(branco)

            cardPerdido.setCardBackgroundColor(branco)
            tvPerdido.setTextColor(laranja)

            cardEncontrado.setCardBackgroundColor(branco)
            tvEncontrado.setTextColor(laranja)

            isPerdido = false
            isEncontrado = false
            isMyPet = true


            adapter.atualiza(meusPetsRecuperados)
        }
    }

    private fun FragmentPerdidoEncontradoBinding.changeCardEncontradoColor() {
        if (isEncontrado) {
            cardEncontrado.setCardBackgroundColor(branco)
            tvEncontrado.setTextColor(laranja)

            isEncontrado = false

            adapter.atualiza(petsRecuperados)
        } else {
            cardEncontrado.setCardBackgroundColor(laranja)
            tvEncontrado.setTextColor(branco)

            cardPerdido.setCardBackgroundColor(branco)
            tvPerdido.setTextColor(laranja)

            cardMeuPet.setCardBackgroundColor(branco)
            tvMeusPets.setTextColor(laranja)

            isPerdido = false
            isEncontrado = true
            isMyPet = false


            filtrarStatusPet(2)
        }
    }

    private fun FragmentPerdidoEncontradoBinding.changeCardPerdidoColor() {
        if (isPerdido) {
            cardPerdido.setCardBackgroundColor(branco)
            tvPerdido.setTextColor(laranja)

            isPerdido = false

            adapter.atualiza(petsRecuperados)
        } else {
            cardPerdido.setCardBackgroundColor(laranja)
            tvPerdido.setTextColor(branco)

            cardEncontrado.setCardBackgroundColor(branco)
            tvEncontrado.setTextColor(laranja)

            cardMeuPet.setCardBackgroundColor(branco)
            tvMeusPets.setTextColor(laranja)

            isPerdido = true
            isEncontrado = false
            isMyPet = false


            filtrarStatusPet(1)
        }
    }

    private fun filtrarStatusPet(code: Int) {
        val lstPets = mutableListOf<Pet>()
        when (code) {
            1 -> {
                petsRecuperados.forEach {
                    if (it.status == "PERDIDO") {
                        lstPets.add(it)
                    }
                    adapter.atualiza(lstPets)
                }
            }
            2 -> {
                petsRecuperados.forEach {
                    if (it.status == "ENCONTRADO") {
                        lstPets.add(it)
                    }
                    adapter.atualiza(lstPets)
                }
            }
        }
    }
}