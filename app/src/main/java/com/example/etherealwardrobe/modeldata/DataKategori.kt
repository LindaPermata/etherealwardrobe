package com.example.etherealwardrobe.modeldata


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataKategori(
    @SerializedName("id_kategori")
    val idKategori: String,

    @SerializedName("nama_kategori")
    val namaKategori: String,

    @SerializedName("created_at")
    val createdAt: String? = null
) : Parcelable