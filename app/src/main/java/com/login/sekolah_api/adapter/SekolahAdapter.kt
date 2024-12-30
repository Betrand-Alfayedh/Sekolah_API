package com.login.sekolah_api.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.login.sekolah_api.DetailSekolahActivity
import com.login.sekolah_api.model.SekolahResponse
import com.login.sekolah_api.R
import com.login.sekolah_api.api.ApiClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SekolahAdapter (
    val dataSekolah: ArrayList<SekolahResponse.ListItem>
): RecyclerView.Adapter<SekolahAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        val imgSekolah = view.findViewById<ImageView>(R.id.imgSekolah)
        val tvNamaSekolah = view.findViewById<TextView>(R.id.tvNamaSekolah)
        val tvNoTlp = view.findViewById<TextView>(R.id.tvNoTlp)
        val tvAkreditasi = view.findViewById<TextView>(R.id.tvAkreditasi)
        val tvAkreditasiHuruf = view.findViewById<TextView>(R.id.tvAkreditasiHuruf)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sekolah_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val hasilResponse = dataSekolah[position]
        Picasso.get().load(hasilResponse.gambar).into(holder.imgSekolah)
        holder.tvNamaSekolah.text = hasilResponse.nama_sekolah
        holder.tvNoTlp.text = hasilResponse.notlp
        holder.tvAkreditasiHuruf.text = hasilResponse.akreditasi

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailSekolahActivity::class.java).apply {
                putExtra("gambar", hasilResponse.gambar)
                putExtra("nama_sekolah", hasilResponse.nama_sekolah)
                putExtra("notlp", hasilResponse.notlp)
                putExtra("akreditasi", hasilResponse.akreditasi)
                putExtra("informasi", hasilResponse.informasi)
            }

            holder.imgSekolah.context.startActivity(intent)
        }
        //remove item
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context).apply {
                setTitle("Konfirmasi")
                setMessage("Apakah anda ingin melanjutkan?")
                setIcon(R.drawable.ic_delete)

                setPositiveButton("Yakin") { dialogInterface, i ->
                    ApiClient.apiService.delSekolah(hasilResponse.id)
                        .enqueue(object : Callback<SekolahResponse> {
                            override fun onResponse(
                                call: Call<SekolahResponse>,
                                response: Response<SekolahResponse>
                            ) {
                                if (response.body()!!.success) {
                                    removeItem(position)
                                } else {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                            override fun onFailure(call: Call<SekolahResponse>, t: Throwable) {
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Ada Kesalahan Server",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        })
                    dialogInterface.dismiss()
                }

                setNegativeButton("Batal") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
            }.show()

            true
        }
    }

    fun removeItem(position: Int) {
        dataSekolah.removeAt(position)
        notifyItemRemoved(position) // Notify the position of the removed item
        notifyItemRangeChanged(position, dataSekolah.size - position) // Optional: Adjust for index shifts
    }

    override fun getItemCount(): Int {
        return dataSekolah.size
    }

    fun setData(data: List<SekolahResponse.ListItem>){
        dataSekolah.clear()
        dataSekolah.addAll(data)
    }

}