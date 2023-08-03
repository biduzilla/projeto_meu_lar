package br.ricky.projeto_meu_lar.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import br.ricky.projeto_meu_lar.ABRIR_CAMERA
import br.ricky.projeto_meu_lar.ABRIR_GALERIA
import br.ricky.projeto_meu_lar.R
import br.ricky.projeto_meu_lar.databinding.ActivityFormPetBinding
import br.ricky.projeto_meu_lar.databinding.BottomSheetFormPetBinding
import br.ricky.projeto_meu_lar.model.Pet
import br.ricky.projeto_meu_lar.utils.bitmapToBase64
import br.ricky.projeto_meu_lar.utils.uriToBitmap
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FormPetActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormPetBinding.inflate(layoutInflater)
    }
    private lateinit var currentPhotoPath: String
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var isUpdate: Boolean = false
    private var resultCode: String = ""
    private var caminhoImagem: String? = null
    private var imagemEscolhida: String? = null
    private val isAdocao: Boolean = false
    private var tamanho: Int? = null
    private var status: Int? = null
    private val laranja: Int = Color.parseColor("#f8a300")
    private val cinzaClaro: Int = Color.parseColor("#CAC4C4")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startResult()
        configClicks()
    }

    private fun configClicks() {
        with(binding) {
            toolbar.btnVoltar.setOnClickListener { finish() }
            btnCadastrar.setOnClickListener { validaDados() }
            cardImg.setOnClickListener { showBottonSheet() }

            cbEncontrado.setOnClickListener {
                cbPerdido.setTextColor(cinzaClaro)

                if (cbEncontrado.isChecked){
                    cbPerdido.isChecked = false
                    status = 1
                    cbEncontrado.setTextColor(laranja)
                }
            }

            cbPerdido.setOnClickListener {
                cbEncontrado.setTextColor(cinzaClaro)
                if (cbPerdido.isChecked){
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

            cbPequeno.setOnClickListener{
                if (cbPequeno.isChecked) {
                    cbMedio.isChecked = false
                    cbGrande.isChecked = false
                    tamanho = 1
                    cbPequeno.setTextColor(laranja)
                }
                cbMedio.setTextColor(cinzaClaro)
                cbGrande.setTextColor(cinzaClaro)
            }

            cbMedio.setOnClickListener{
                if (cbMedio.isChecked) {
                    cbPequeno.isChecked = false
                    cbGrande.isChecked = false
                    tamanho = 2
                    cbMedio.setTextColor(laranja)
                }
                cbPequeno.setTextColor(cinzaClaro)
                cbGrande.setTextColor(cinzaClaro)
            }

            cbGrande.setOnClickListener{
                if (cbGrande.isChecked) {
                    cbPequeno.isChecked = false
                    cbMedio.isChecked = false
                    tamanho = 2
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
                        "com.toddy.vagasifb.fileprovider",
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
                binding.edtNome.requestFocus()
                binding.edtNome.error = "Campo obrigatório"
            }
            imagemEscolhida == null -> {
                Toast.makeText(
                    this,
                    "Escolha uma imagem para o pet",
                    Toast.LENGTH_SHORT
                ).show()
            }
            isAdocao -> {
                if (tamanho == null) {
                    Toast.makeText(
                        this,
                        "Escolha o tamanho do pet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            !isAdocao -> {
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
            else -> {
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
                    else -> "ERROR"
                }

                val pet = Pet(
                    nomePet = nome,
                    descricao = desc,
                    imagem = imagemEscolhida!!,
                    tamanho = tam,
                    status = sta
                )
                salvarPet(pet)
            }
        }

    }

    private fun salvarPet(pet: Pet) {
        binding.btnCadastrar.visibility = View.VISIBLE
        binding.progressCircular.visibility = View.GONE
        Log.i("infoteste", "salvarPet nome: ${pet.nomePet}")
        Log.i("infoteste", "salvarPet desc: ${pet.descricao}")
        Log.i("infoteste", "salvarPet status: ${pet.status}")
        Log.i("infoteste", "salvarPet tamanho: ${pet.tamanho}")
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

                            val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            imagemEscolhida = bitmapToBase64(bitmap)
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

                            val bitmapImg: Bitmap? = uriToBitmap(this, imagemSelecionada)
                            bitmapImg?.let { img ->
                                imagemEscolhida = bitmapToBase64(img)
                            } ?: kotlin.run {
                                Toast.makeText(
                                    this,
                                    "Error carregar imagem galeria",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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