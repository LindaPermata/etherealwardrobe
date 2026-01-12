package com.example.etherealwardrobe.modeldata


import com.google.gson.annotations.SerializedName

data class DataUser(
    @SerializedName("id_user")
    val idUser: String,

    @SerializedName("nama_lengkap")
    val namaLengkap: String,

    @SerializedName("token")
    val token: String
)