package com.login.sekolah_api

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class DetailSekolahActivity : AppCompatActivity() {

    private lateinit var imgDetail : ImageView
    private lateinit var tvNamaSekolaDetail : TextView
    private lateinit var tvNotlpDetail : TextView
    private lateinit var tvAkreditasiHurufDetail : TextView
    private lateinit var tvDetailIsi : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_sekolah)

        imgDetail = findViewById(R.id.imgDetail)
        tvNamaSekolaDetail = findViewById(R.id.tvNamaSekolahDetail)
        tvNotlpDetail = findViewById(R.id.tvNotlpDetail)
        tvAkreditasiHurufDetail = findViewById(R.id.tvAkreditasiHurufDetail)
        tvDetailIsi = findViewById(R.id.tvDetailIsi)


        Picasso.get().load(intent.getStringExtra("gambar")).into(imgDetail)
        tvNamaSekolaDetail.text = intent.getStringExtra("nama_sekolah")
        tvNotlpDetail.text = intent.getStringExtra("notlp")
        tvAkreditasiHurufDetail.text = intent.getStringExtra("akreditasi")
        tvDetailIsi.text = intent.getStringExtra("informasi")


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}