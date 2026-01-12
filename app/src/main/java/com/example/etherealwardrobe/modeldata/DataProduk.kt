package com.example.etherealwardrobe.modeldata


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataProduk(
    @SerializedName("id_produk")
    val idProduk: String,

    @SerializedName("nama_produk")
    val namaProduk: String,

    @SerializedName("warna")
    val warna: String,

    @SerializedName("harga")
    val harga: String,

    @SerializedName("jumlah_stok")
    val jumlahStok: String?,

    @SerializedName("id_kategori")
    val idKategori: String,

    @SerializedName("created_at")
    val createdAt: String? = null
) : Parcelable