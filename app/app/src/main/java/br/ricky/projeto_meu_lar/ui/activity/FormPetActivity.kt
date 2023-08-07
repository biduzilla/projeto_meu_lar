package br.ricky.projeto_meu_lar.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import br.ricky.projeto_meu_lar.*
import br.ricky.projeto_meu_lar.data.SharedPref
import br.ricky.projeto_meu_lar.databinding.ActivityFormPetBinding
import br.ricky.projeto_meu_lar.databinding.BottomSheetFormPetBinding
import br.ricky.projeto_meu_lar.extensions.tentaCarregarImagem
import br.ricky.projeto_meu_lar.model.Pet
import br.ricky.projeto_meu_lar.model.PetSalvar
import br.ricky.projeto_meu_lar.model.PetUpdate
import br.ricky.projeto_meu_lar.network.FirebaseDao
import br.ricky.projeto_meu_lar.repository.PetRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FormPetActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormPetBinding.inflate(layoutInflater)
    }

    private val petRepository by lazy {
        PetRepository()
    }
    private lateinit var currentPhotoPath: String
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var resultCode: String = ""
    private var caminhoImagem: String? = null
    private var isAdocao: Boolean = false
    private var isUpdate: Boolean = false
    private var tamanho: Int? = null
    private var status: Int? = null
    private val laranja: Int = Color.parseColor("#f8a300")
    private val cinzaClaro: Int = Color.parseColor("#CAC4C4")
    private lateinit var token: String
    private lateinit var petRecuperado: Pet


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startResult()
        carregaToken()
        configClicks()
        verificaAdocaoAndUpdate()
    }

    private fun carregaToken() {
        SharedPref(this).getToken()?.let {
            token = it
        } ?: run {
            Toast.makeText(baseContext, "Error carregar token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificaAdocaoAndUpdate() {
        isUpdate = intent.getBooleanExtra(IS_UPDATE, false)
        isAdocao = intent.getBooleanExtra(IS_ADOCAO, false)

        with(binding) {
            if (isUpdate) {
                toolbar.tvTitulo.text = "Atualizar dados do pet"
                btnCadastrar.text = "Atualizar"

                scrollView.visibility = View.GONE
                loadDados.visibility = View.VISIBLE

                tentaCarregarPet()
            } else {
                toolbar.tvTitulo.text = "Cadastrar dados do pet"
                btnCadastrar.text = "Cadastrar"
                cbAdotar.visibility = View.GONE
            }

            if (isAdocao && !isUpdate) {
                llStatus.visibility = View.GONE
            } else {
                llStatus.visibility = View.VISIBLE
            }
        }
    }

    private fun tentaCarregarPet() {
        val petId = intent.getStringExtra(CHAVE_ID_PET)

        lifecycleScope.launch {
            petId?.let {
                PetRepository().getPetById(
                    activity = this@FormPetActivity,
                    idPet = it,
                    token = token
                )?.let { pet ->
                    petRecuperado = pet
                    carregaDados(pet)
                }

            }
        }
    }

    private fun carregaDados(pet: Pet) {
        with(binding) {
            edtNome.setText(pet.nomePet)
            edtDesc.setText(pet.descricao)
            imgPet.tentaCarregarImagem(pet.imagem)
            imgIcon.visibility = View.GONE

            when (pet.status) {
                "ADOTAR" -> {
                    status = 3
                    cbAdotar.isChecked = true
                    cbPerdido.setTextColor(cinzaClaro)
                    cbEncontrado.setTextColor(cinzaClaro)
                    cbAdotar.setTextColor(laranja)
                }
                "ENCONTRADO" -> {
                    status = 1
                    cbEncontrado.isChecked = true
                    cbPerdido.setTextColor(cinzaClaro)
                    cbEncontrado.setTextColor(laranja)
                    cbAdotar.setTextColor(cinzaClaro)
                }
                "PERDIDO" -> {
                    status = 2
                    cbPerdido.isChecked = true
                    cbPerdido.setTextColor(laranja)
                    cbEncontrado.setTextColor(cinzaClaro)
                    cbAdotar.setTextColor(cinzaClaro)
                }
            }

            when (pet.tamanho) {
                "PEQUENO" -> {
                    tamanho = 1
                    cbPequeno.isChecked = true
                    cbMedio.setTextColor(cinzaClaro)
                    cbGrande.setTextColor(cinzaClaro)
                    cbPequeno.setTextColor(laranja)
                }
                "MEDIO" -> {
                    tamanho = 2
                    cbMedio.isChecked = true
                    cbMedio.setTextColor(laranja)
                    cbGrande.setTextColor(cinzaClaro)
                    cbPequeno.setTextColor(cinzaClaro)
                }
                "GRANDE" -> {
                    tamanho = 3
                    cbGrande.isChecked = true
                    cbMedio.setTextColor(cinzaClaro)
                    cbGrande.setTextColor(laranja)
                    cbPequeno.setTextColor(cinzaClaro)
                }
            }
            scrollView.visibility = View.VISIBLE
            loadDados.visibility = View.GONE
        }
    }

    private fun configClicks() {
        with(binding) {
            toolbar.btnVoltar.setOnClickListener { finish() }
            btnCadastrar.setOnClickListener { validaDados() }
            cardImg.setOnClickListener { showBottonSheet() }

            cbEncontrado.setOnClickListener {
                cbPerdido.setTextColor(cinzaClaro)
                cbAdotar.setTextColor(cinzaClaro)

                if (cbEncontrado.isChecked) {
                    cbPerdido.isChecked = false
                    status = 1
                    cbEncontrado.setTextColor(laranja)
                }
            }

            cbAdotar.setOnClickListener {
                cbEncontrado.setTextColor(cinzaClaro)
                cbPerdido.setTextColor(cinzaClaro)

                if (cbAdotar.isChecked) {
                    cbAdotar.isChecked = false
                    status = 3
                    cbAdotar.setTextColor(laranja)
                }
            }

            cbPerdido.setOnClickListener {
                cbEncontrado.setTextColor(cinzaClaro)
                cbAdotar.setTextColor(cinzaClaro)
                if (cbPerdido.isChecked) {
                    cbEncontrado.isChecked = false
                    status = 1
                    cbPerdido.setTextColor(laranja)
                }
            }

            cbPerdido.isChecked.apply {
                if (this) {
                    cbEncontrado.isChecked = false
                    status = 2
                }
            }

            cbPequeno.setOnClickListener {
                if (cbPequeno.isChecked) {
                    cbMedio.isChecked = false
                    cbGrande.isChecked = false
                    tamanho = 1
                    cbPequeno.setTextColor(laranja)
                }
                cbMedio.setTextColor(cinzaClaro)
                cbGrande.setTextColor(cinzaClaro)
            }

            cbMedio.setOnClickListener {
                if (cbMedio.isChecked) {
                    cbPequeno.isChecked = false
                    cbGrande.isChecked = false
                    tamanho = 2
                    cbMedio.setTextColor(laranja)
                }
                cbPequeno.setTextColor(cinzaClaro)
                cbGrande.setTextColor(cinzaClaro)
            }

            cbGrande.setOnClickListener {
                if (cbGrande.isChecked) {
                    cbPequeno.isChecked = false
                    cbMedio.isChecked = false
                    tamanho = 3
                    cbGrande.setTextColor(laranja)
                }
                cbPequeno.setTextColor(cinzaClaro)
                cbMedio.setTextColor(cinzaClaro)
            }
            verificaCb(this)
        }
    }

    private fun verificaCb(binding: ActivityFormPetBinding) {
        with(binding) {
            if (!cbGrande.isChecked || !cbMedio.isChecked || !cbPequeno.isChecked) {
                tamanho = null
            }
            if (!cbEncontrado.isChecked || !cbPerdido.isChecked) {
                status = null
            }
        }
    }

    private fun ocultarTeclado() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.edtDesc.windowToken, 0)
        binding.edtNome.clearFocus()
        binding.edtDesc.clearFocus()
    }

    private fun showBottonSheet() {
        val sheetBinding = BottomSheetFormPetBinding.inflate(layoutInflater)

        BottomSheetDialog(this, R.style.BottomSheetDialog).apply {
            setContentView(sheetBinding.root)
            show()

            sheetBinding.btnCamera.setOnClickListener {
                resultCode = ABRIR_CAMERA
                verificarPermissaoCamera()
                dismiss()
            }
            sheetBinding.btnGaleria.setOnClickListener {
                resultCode = ABRIR_GALERIA
                verificarPermissaoGaleria()
                dismiss()
            }
            sheetBinding.btnCancelar.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun verificarPermissaoGaleria() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            resultLauncher.launch(this)
        }
    }


    private fun verificarPermissaoCamera() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                abrirCamera()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(baseContext, "Permissão Negada", Toast.LENGTH_SHORT).show()
            }
        }
        showDialogPermissao(
            permissionListener,
            "Se você não aceitar a permissão não poderá acessar a camera do dispositivo, deseja aceitar a permissão?",
            listOf(android.Manifest.permission.CAMERA)
        )
    }

    private fun abrirCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "br.ricky.projeto_meu_lar.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun showDialogPermissao(
        permissionListener: PermissionListener,
        msg: String,
        perm: List<String>
    ) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedTitle("Permissão negada")
            .setDeniedMessage(msg)
            .setDeniedCloseButtonText("Não")
            .setGotoSettingButtonText("Sim")
            .setPermissions(*perm.toTypedArray())
            .check()
    }

    private fun validaDados() {
        ocultarTeclado()

        val nome = binding.edtNome.text.toString().trim()
        val desc = binding.edtDesc.text.toString().trim()

        when {
            nome.isEmpty() -> {
                binding.edtNome.requestFocus()
                binding.edtNome.error = "Campo obrigatório"
            }
            desc.isEmpty() -> {
                binding.edtDesc.requestFocus()
                binding.edtDesc.error = "Campo obrigatório"
            }
            caminhoImagem == null && !isUpdate -> {
                Toast.makeText(
                    this,
                    "Escolha uma imagem para o pet",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                if (isAdocao) {
                    if (tamanho == null) {
                        Toast.makeText(
                            this,
                            "Escolha o tamanho do pet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (tamanho == null) {
                        Toast.makeText(
                            this,
                            "Escolha o tamanho do pet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (status == null) {
                        Toast.makeText(
                            this,
                            "Escolha o status do pet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                binding.btnCadastrar.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE

                val tam = when (tamanho) {
                    1 -> "PEQUENO"
                    2 -> "MEDIO"
                    3 -> "GRANDE"
                    else -> "ERROR"
                }
                val sta = when {
                    status == 1 -> "ENCONTRADO"
                    status == 2 -> "PERDIDO"
                    isAdocao -> "ADOTAR"
                    status == 3 -> "ADOTAR"
                    else -> "ERROR"
                }

                val pet = PetSalvar(
                    id = if (isUpdate) {
                        petRecuperado.id
                    } else {
                        UUID.randomUUID().toString()
                    },
                    nome = nome,
                    descricao = desc,
                    tamanho = tam,
                    status = sta
                )
                if (isUpdate) {
                    if (caminhoImagem == null) {
                        pet.imagem = petRecuperado.imagem
                        atualizarPet(pet)
                    } else {
                        salvarImagemFirebase(pet) { urlGerada ->
                            pet.imagem = urlGerada
                            atualizarPet(pet)
                        }
                    }
                } else {
                    salvarImagemFirebase(pet) { urlGerada ->
                        pet.imagem = urlGerada
                        salvarPet(pet)
                    }
                }
            }
        }
    }

    private fun atualizarPet(pet: PetSalvar) {
        val petUpdate = PetUpdate(
            nome = pet.nome,
            descricao = pet.descricao,
            status = pet.status,
            imagem = pet.imagem,
            tamanho = pet.tamanho
        )
        val idUser = SharedPref(this).getIdUser()
        lifecycleScope.launch {
            petRepository.atualizarPost(
                activity = this@FormPetActivity,
                token = token,
                idUser = idUser!!,
                idPet = pet.id,
                pet = petUpdate
            ).apply {
                if (this) {
                    finish()
                }else{
                    binding.btnCadastrar.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }
    }


    private fun salvarPet(pet: PetSalvar) {
        val idUser = SharedPref(this@FormPetActivity).getIdUser()

        lifecycleScope.launch {
            petRepository.salvarPet(
                this@FormPetActivity,
                pet = pet,
                token = token,
                idUser = idUser!!
            ).apply {
                if (this) {
                    finish()
                } else {
                    Toast.makeText(
                        this@FormPetActivity,
                        "Error ao cadastrar o pet",
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.btnCadastrar.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }
    }

    private fun salvarImagemFirebase(pet: PetSalvar, urlGerada: (url: String) -> Unit) {
        caminhoImagem?.let {
            FirebaseDao().salvarImagemPetFirebase(
                imagem = it,
                activity = this,
                petId = pet.id
            ) { url ->
                url?.let { urlRecuperada ->
                    urlGerada(urlRecuperada)
                }
            }
        }
    }

    private fun startResult() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    when (resultCode) {
                        ABRIR_CAMERA -> {
                            val file = File(currentPhotoPath)
                            binding.imgIcon.visibility = View.GONE
                            binding.imgPet.setImageURI(Uri.fromFile(file))
                            binding.imgPet.visibility = View.VISIBLE
                            caminhoImagem = file.toURI().toString()

                        }
                        ABRIR_GALERIA -> {

                            val imagemSelecionada: Uri = it.data!!.data!!
                            caminhoImagem = imagemSelecionada.toString()

                            val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                                MediaStore.Images.Media.getBitmap(
                                    contentResolver,
                                    imagemSelecionada
                                )
                            } else {
                                val source: ImageDecoder.Source =
                                    ImageDecoder.createSource(
                                        contentResolver,
                                        imagemSelecionada
                                    )
                                ImageDecoder.decodeBitmap(source)
                            }
                            binding.imgIcon.visibility = View.GONE
                            binding.imgPet.setImageBitmap(bitmap)
                            binding.imgPet.visibility = View.VISIBLE

                        }
                        else -> Toast.makeText(
                            this,
                            "Error carregar imagem selecionada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
}