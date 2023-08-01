package br.ricky.projeto_meu_lar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.ricky.projeto_meu_lar.databinding.ItemPetBinding
import br.ricky.projeto_meu_lar.model.Pet
import br.ricky.projeto_meu_lar.utils.base64ToBitmap

class PetAdapter(
    pets: List<Pet> = emptyList(),
    var onClick: (pet: Pet) -> Unit = {}
) : RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    private val pets = pets.toMutableList()

    inner class ViewHolder(private val binding: ItemPetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var pet: Pet

        init {
            itemView.setOnClickListener {
                if (::pet.isInitialized) {
                    onClick(pet)
                }
            }
        }

        fun vincula(pet: Pet) {
            this.pet = pet

            with(binding) {
                tvNomePet.text = pet.nomePet
                tvDesc.text = pet.descricao
                if (pet.status == "ADOTAR") {
                    tvStatus.text = "Adoção"
                } else {
                    "Error"
                }
                imgPet.base64ToBitmap(pet.imagem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPetBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetAdapter.ViewHolder, position: Int) {
        val pet = pets[position]
        holder.vincula(pet)
    }

    override fun getItemCount(): Int = pets.size

    fun atualiza(pets: List<Pet>) {
        this.pets.clear()
        this.pets.addAll(pets)
        notifyDataSetChanged()
    }
}