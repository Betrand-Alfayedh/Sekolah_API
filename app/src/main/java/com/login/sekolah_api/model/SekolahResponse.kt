package com.login.sekolah_api.model

data class SekolahResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<ListItem>
){
    data class ListItem(
        val id: String,
        val nama_sekolah: String,
        val notlp: String,
        val informasi: String,
        val gambar: String,
        val akreditasi: String,
        val akreditasiHuruf: String,
    )
}
