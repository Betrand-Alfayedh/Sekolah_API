package com.login.sekolah_api

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.login.sekolah_api.api.ApiClient
import com.login.sekolah_api.adapter.SekolahAdapter
import com.login.sekolah_api.model.SekolahResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var svJudul: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvBerita: RecyclerView
    private lateinit var floatBtnTambah: FloatingActionButton
    private lateinit var sekolahAdapter: SekolahAdapter
    private lateinit var imgNotFound: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        svJudul = findViewById(R.id.svJudul)
        progressBar = findViewById(R.id.progressBar)
        rvBerita = findViewById(R.id.rvBerita)
        floatBtnTambah = findViewById(R.id.floatBtnTambah)
        imgNotFound = findViewById(R.id.imgNotFound)

        // Inisialisasi Adapter
        sekolahAdapter = SekolahAdapter(arrayListOf())
        rvBerita.adapter = sekolahAdapter
        rvBerita.layoutManager = LinearLayoutManager(this)

        // Panggil method getBerita
        getSekolah("")

        svJudul.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(pencarian: String?): Boolean {
                getSekolah(pencarian.toString())
                return true
            }
        })
        floatBtnTambah = findViewById(R.id.floatBtnTambah)
        floatBtnTambah.setOnClickListener {
            startActivity(Intent(this, TambahSekolahActivity::class.java))
        }

    }
    private fun getSekolah(nama: String) {
        progressBar.visibility = View.VISIBLE

        ApiClient.apiService.getListSekolah(nama).enqueue(object : Callback<SekolahResponse> {
            override fun onResponse(
                call: Call<SekolahResponse>,
                response: Response<SekolahResponse>
            ) {
                if (response.isSuccessful){
                    if(response.body()!!.success){
                        //set data ke adapter
                        sekolahAdapter = SekolahAdapter(response.body()!!.data)
                        rvBerita.layoutManager = LinearLayoutManager(this@DashboardActivity)
                        rvBerita.adapter = sekolahAdapter
                        imgNotFound.visibility = View.GONE
                    } else {
                        //jika data tidak ditemukan
                        sekolahAdapter = SekolahAdapter(arrayListOf())
                        rvBerita.layoutManager = LinearLayoutManager(this@DashboardActivity)
                        rvBerita.adapter = sekolahAdapter
                        imgNotFound.visibility = View.VISIBLE
                    }
                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<SekolahResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Error : ${t.message}", Toast.LENGTH_LONG)
                    .show()
                progressBar.visibility = View.GONE
            }
        })
    }

}