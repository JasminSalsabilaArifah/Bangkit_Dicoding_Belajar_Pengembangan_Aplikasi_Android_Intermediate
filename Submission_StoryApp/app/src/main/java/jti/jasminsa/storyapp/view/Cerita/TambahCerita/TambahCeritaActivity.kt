package jti.jasminsa.storyapp.view.Cerita.TambahCerita

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import jti.jasminsa.storyapp.ViewModelFactory
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.data.createCustomTempFile
import jti.jasminsa.storyapp.data.reduceFileImage
import jti.jasminsa.storyapp.data.response.RegisterResponse
import jti.jasminsa.storyapp.data.retrofit.ApiConfig
import jti.jasminsa.storyapp.data.uriToFile
import jti.jasminsa.storyapp.databinding.ActivityTambahCeritaBinding
import jti.jasminsa.storyapp.view.Cerita.DaftarCerita.DaftarCeritaActivity
import jti.jasminsa.storyapp.view.Cerita.DaftarCerita.DaftarCeritaViewModel
import jti.jasminsa.storyapp.view.login.LoginViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userLogin")
class TambahCeritaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahCeritaBinding
    private lateinit var tcViewModel: DaftarCeritaViewModel
    private var getFile: File? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahCeritaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        tcViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[DaftarCeritaViewModel::class.java]
        

        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "jti.jasminsa.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            tcViewModel.getUser().observe(this, { story ->
                val file = reduceFileImage(getFile as File)
                val description = binding.desEditText.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val token = story.token
                val apiService = ApiConfig.getApiService()
                val uploadImageRequest = apiService.postStory("Bearer $token" ,imageMultipart, description)
                uploadImageRequest.enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                val intent = Intent(this@TambahCeritaActivity, DaftarCeritaActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this@TambahCeritaActivity, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@TambahCeritaActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            })
        } else {
            Toast.makeText(this@TambahCeritaActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file ->
//          Silakan gunakan kode ini jika mengalami perubahan rotasi
//                rotateFile(file)
                getFile = file
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this)
                getFile = myFile
                binding.previewImageView.setImageURI(uri)
            }
        }
    }
}