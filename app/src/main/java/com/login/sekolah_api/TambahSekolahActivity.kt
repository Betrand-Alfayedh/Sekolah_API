package com.login.sekolah_api

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.login.sekolah_api.api.ApiClient
import com.login.sekolah_api.model.TambahSekolahResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class TambahSekolahActivity : AppCompatActivity() {
    private lateinit var etNamaSekolah: EditText
    private lateinit var etNoTelpon: EditText
    private lateinit var etInformasi: EditText
    private lateinit var etAkreditasi: EditText
    private lateinit var btnGambar: Button
    private lateinit var imgGambar: ImageView
    private lateinit var btnTambah: Button
    private lateinit var progressBar: ProgressBar
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_sekolah)

        // Inisialisasi view
        etNamaSekolah = findViewById(R.id.etNamaSekolah)
        etNoTelpon = findViewById(R.id.etNoTelpon)
        etInformasi = findViewById(R.id.etInformasi)
        etAkreditasi = findViewById(R.id.etAkreditasi)
        btnTambah = findViewById(R.id.btnTambah)
        btnGambar = findViewById(R.id.btnGambar)
        imgGambar = findViewById(R.id.imgGambar)
        progressBar = findViewById(R.id.progressBar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tambahkan listener untuk tombol pilih gambar
        btnGambar.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
        btnTambah.setOnClickListener {
            imageFile?.let { file ->
                tambahSekolah(
                    etNamaSekolah.text.toString(),
                    file,
                    etNoTelpon.text.toString(),
                    etInformasi.text.toString(),
                    etAkreditasi.text.toString()
                )
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data!!
            imageFile = File(uri.path!!)
            imgGambar.visibility = View.VISIBLE
            imgGambar.setImageURI(uri)
        }
    }

    //proses tambah sekolah
    private fun tambahSekolah(namaSekolah: String, fileGambar: File, notlp: String, informasi: String, akreditasi: String) {
        progressBar.visibility = View.VISIBLE
        val requestBody = fileGambar.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val partFileGambar = MultipartBody.Part.createFormData("fileGambar", fileGambar.name, requestBody)
        val namaSekolahBody = namaSekolah.toRequestBody("text/plain".toMediaTypeOrNull())
        val notlpBody = notlp.toRequestBody("text/plain".toMediaTypeOrNull())
        val informasiBody = informasi.toRequestBody("text/plain".toMediaTypeOrNull())
        val akreditasiBody = akreditasi.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.apiService.addSekolah(namaSekolahBody, notlpBody, informasiBody, partFileGambar, akreditasiBody).enqueue(object :
            Callback<TambahSekolahResponse>{
            override fun onResponse(
                call: Call<TambahSekolahResponse>,
                response: Response<TambahSekolahResponse>
            ) {
                if(response.isSuccessful){
                    if(response.body()!!.success){

                        startActivity(
                            Intent(
                                this@TambahSekolahActivity,
                                DashboardActivity::class.java)
                        )
                    }
                    else {
                        Toast.makeText(this@TambahSekolahActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else
                {
                    Toast.makeText(this@TambahSekolahActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressBar.visibility = View.GONE

            }


            override fun onFailure(call: Call<TambahSekolahResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }
}
