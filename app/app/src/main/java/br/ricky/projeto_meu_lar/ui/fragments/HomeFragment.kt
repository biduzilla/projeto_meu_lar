package br.ricky.projeto_meu_lar.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.ricky.projeto_meu_lar.CHAVE_ID_PET
import br.ricky.projeto_meu_lar.CHAVE_MEU_PET
import br.ricky.projeto_meu_lar.R
import br.ricky.projeto_meu_lar.data.SharedPref
import br.ricky.projeto_meu_lar.databinding.FragmentHomeBinding
import br.ricky.projeto_meu_lar.extensions.iniciaActivity
import br.ricky.projeto_meu_lar.model.LoginUser
import br.ricky.projeto_meu_lar.model.Pet
import br.ricky.projeto_meu_lar.repository.PetRepository
import br.ricky.projeto_meu_lar.ui.activity.DetalhesPetActivity
import br.ricky.projeto_meu_lar.ui.activity.auth.LoginActivity
import br.ricky.projeto_meu_lar.ui.adapter.PetAdapter
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = PetAdapter()
    private var petsRecuperados = mutableListOf<Pet>()
    private var meusPetsRecuperados = mutableListOf<Pet>()
    private var loginUser: LoginUser? = null
    private val laranja: Int = Color.parseColor("#f8a300")
    private val branco: Int = Color.parseColor("#FFFFFFFF")
    private var isPequeno: Boolean = false
    private var isMedio: Boolean = false
    private var isGrande: Boolean = false
    private var isMyPet: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        configClicks()
        carregaDadosPref()
        recuperaPets()
        configRv()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loginUser?.let {
            binding.tvNome.text = it.nome
        }

        if (petsRecuperados.isEmpty()) {
            with(binding) {
                progressCircular.visibility = View.VISIBLE
                rv.visibility = View.GONE
            }
            recuperaPets()
        }

        with(binding) {
            progressCircular.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }
    }

    private fun ocultarTeclado() {
        val imm = ContextCompat.getSystemService(requireView().context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
    }


    private fun configClicks() {
        with(binding) {
            btnSair.setOnClickListener {
                SharedPref(requireActivity()).salvarToken("")
                requireActivity().iniciaActivity(LoginActivity::class.java)
                requireActivity().finish()
            }

            cardPequeno.setOnClickListener {
                ocultarTeclado()
                edtSearch.clearFocus()
                changeCardPequenoColor()
            }

            cardMedio.setOnClickListener {
                ocultarTeclado()
                edtSearch.clearFocus()
                changeCardMedioColor()
            }

            cardGrande.setOnClickListener {
                ocultarTeclado()
                edtSearch.clearFocus()
                changeCardGrandeColor()
            }

            cardMeusPets.setOnClickListener {
                ocultarTeclado()
                edtSearch.clearFocus()
                changeCardMeusPetsColor()
            }

            edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    filtrarNomePet(edtSearch.text.toString())
                }
            })
        }
    }

    private fun FragmentHomeBinding.changeCardGrandeColor() {
        if (isGrande) {
            cardGrande.setCardBackgroundColor(branco)
            tvGrande.setTextColor(laranja)

            isGrande = false
            adapter.atualiza(petsRecuperados)
        } else {
            cardGrande.setCardBackgroundColor(laranja)
            tvGrande.setTextColor(branco)

            cardMedio.setCardBackgroundColor(branco)
            tvMedio.setTextColor(laranja)

            cardPequeno.setCardBackgroundColor(branco)
            tvPequeno.setTextColor(laranja)

            cardMeusPets.setCardBackgroundColor(branco)
            tvMeusPets.setTextColor(laranja)

            isGrande = true
            isMyPet = false
            isMedio = false
            isPequeno = false

            filtrarTamanhoPet(3)
        }
    }

    private fun FragmentHomeBinding.changeCardMeusPetsColor() {
        if (isMyPet) {
            cardMeusPets.setCardBackgroundColor(branco)
            tvMeusPets.setTextColor(laranja)

            isMyPet = false
            adapter.atualiza(petsRecuperados)
        } else {
            cardMeusPets.setCardBackgroundColor(laranja)
            tvMeusPets.setTextColor(branco)

            cardMedio.setCardBackgroundColor(branco)
            tvMedio.setTextColor(laranja)

            cardPequeno.setCardBackgroundColor(branco)
            tvPequeno.setTextColor(laranja)

            cardGrande.setCardBackgroundColor(branco)
            tvGrande.setTextColor(laranja)

            isMyPet = true
            isGrande = false
            isMedio = false
            isPequeno = false
            adapter.atualiza(meusPetsRecuperados)
        }
    }

    private fun FragmentHomeBinding.changeCardMedioColor() {
        if (isMedio) {
            cardMedio.setCardBackgroundColor(branco)
            tvMedio.setTextColor(laranja)

            isMedio = false
            adapter.atualiza(petsRecuperados)
        } else {
            cardMedio.setCardBackgroundColor(laranja)
            tvMedio.setTextColor(branco)

            cardMeusPets.setCardBackgroundColor(branco)
            tvMeusPets.setTextColor(laranja)

            cardPequeno.setCardBackgroundColor(branco)
            tvPequeno.setTextColor(laranja)

            cardGrande.setCardBackgroundColor(branco)
            tvGrande.setTextColor(laranja)

            isMedio = true
            isPequeno = false
            isGrande = false
            isMyPet = false

            filtrarTamanhoPet(2)
        }
    }

    private fun FragmentHomeBinding.changeCardPequenoColor() {
        if (isPequeno) {
            cardPequeno.setCardBackgroundColor(branco)
            tvPequeno.setTextColor(laranja)

            isPequeno = false

            adapter.atualiza(petsRecuperados)
        } else {
            cardPequeno.setCardBackgroundColor(laranja)
            tvPequeno.setTextColor(branco)

            cardMedio.setCardBackgroundColor(branco)
            tvMedio.setTextColor(laranja)

            cardGrande.setCardBackgroundColor(branco)
            tvGrande.setTextColor(laranja)

            cardMeusPets.setCardBackgroundColor(branco)
            tvMeusPets.setTextColor(laranja)

            isPequeno = true
            isGrande = false
            isMyPet = false
            isMedio = false

            filtrarTamanhoPet(1)
        }
    }

    private fun carregaDadosPref() {
        loginUser = SharedPref(requireActivity()).getDadosLogin()
    }

    private fun recuperaPets() {

        loginUser?.token?.let { token ->

            lifecycleScope.launch {
                PetRepository().getAllPetsAdotar(requireContext(), token)?.let { petsList ->
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
                    requireContext(),
                    idUser = loginUser!!.idUser,
                    token = token
                )
                    ?.let { petsList ->
                        meusPetsRecuperados = petsList.toMutableList()
                    }
            }
        }
    }

    private fun filtrarNomePet(nomePet: String) {
        val lstPets = mutableListOf<Pet>()
        petsRecuperados.forEach {
            if (it.nomePet.lowercase().contains(nomePet.lowercase())) {
                lstPets.add(it)
            }
        }
        adapter.atualiza(lstPets)
    }

    private fun filtrarTamanhoPet(code: Int) {
        val lstPets = mutableListOf<Pet>()
        when (code) {
            1 -> {
                petsRecuperados.forEach {
                    if (it.tamanho == "PEQUENO") {
                        lstPets.add(it)
                    }
                    adapter.atualiza(lstPets)
                }
            }
            2 -> {
                petsRecuperados.forEach {
                    if (it.tamanho == "MEDIO") {
                        lstPets.add(it)
                    }
                    adapter.atualiza(lstPets)
                }
            }
            3 -> {
                petsRecuperados.forEach {
                    if (it.tamanho == "GRANDE") {
                        lstPets.add(it)
                    }
                    adapter.atualiza(lstPets)
                }
            }
        }
    }

    private fun configRv() {
        with(binding) {
            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(requireActivity())
            adapter.onClick = { pet ->
                binding.edtSearch.text.clear()
                Intent(requireContext(), DetalhesPetActivity::class.java).apply {

                    val meuPet: Boolean = meusPetsRecuperados.contains(pet)

                    putExtra(CHAVE_ID_PET, pet.id)
                    putExtra(CHAVE_MEU_PET, meuPet)
                    startActivity(this)
                }
            }
        }
    }


}