package br.ricky.projeto_meu_lar.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.ricky.projeto_meu_lar.IS_UPDATE
import br.ricky.projeto_meu_lar.databinding.FragmentConfigBinding
import br.ricky.projeto_meu_lar.extensions.iniciaActivity
import br.ricky.projeto_meu_lar.ui.activity.auth.CadastrarContaActivity


class ConfigFragment : Fragment() {
    private var _binding: FragmentConfigBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)

        configClicks()
        return binding.root
    }

    private fun configClicks() {
        binding.btnConfig.setOnClickListener {
            Intent(requireContext(), CadastrarContaActivity::class.java).apply {
                putExtra(IS_UPDATE,true)
                startActivity(this)
            }
        }
    }


}