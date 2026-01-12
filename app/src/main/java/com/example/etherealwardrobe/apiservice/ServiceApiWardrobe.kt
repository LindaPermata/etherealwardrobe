package com.example.etherealwardrobe.apiservice

import com.example.etherealwardrobe.modeldata.*
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiWardrobe {

    // Auth Endpoints
    @FormUrlEncoded
    @POST("auth/login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ResponseApi<DataUser>>

    @POST("auth/logout.php")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<ResponseApi<Any>>

    // Kategori Endpoints
    @GET("kategori/read.php")
    suspend fun getKategori(
        @Header("Authorization") token: String
    ): Response<ResponseApi<List<DataKategori>>>

    @FormUrlEncoded
    @POST("kategori/create.php")
    suspend fun createKategori(
        @Header("Authorization") token: String,
        @Field("nama_kategori") namaKategori: String
    ): Response<ResponseApi<Any>>

    @FormUrlEncoded
    @PUT("kategori/update.php")
    suspend fun updateKategori(
        @Header("Authorization") token: String,
        @Field("id_kategori") idKategori: String,
        @Field("nama_kategori") namaKategori: String
    ): Response<ResponseApi<Any>>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "kategori/delete.php", hasBody = true)
    suspend fun deleteKategori(
        @Header("Authorization") token: String,
        @Field("id_kategori") idKategori: String
    ): Response<ResponseApi<Any>>

    // Produk Endpoints
    @GET("produk/read.php")
    suspend fun getAllProduk(
        @Header("Authorization") token: String
    ): Response<ResponseApi<List<DataProduk>>>

    @GET("produk/read.php")
    suspend fun getProdukByKategori(
        @Header("Authorization") token: String,
        @Query("id_kategori") idKategori: String
    ): Response<ResponseApi<List<DataProduk>>>

    @FormUrlEncoded
    @POST("produk/create.php")
    suspend fun createProduk(
        @Header("Authorization") token: String,
        @Field("nama_produk") namaProduk: String,
        @Field("id_kategori") idKategori: String,
        @Field("harga") harga: String,
        @Field("warna") warna: String,
        @Field("stok_awal") stokAwal: String
    ): Response<ResponseApi<Any>>

    @FormUrlEncoded
    @PUT("produk/update.php")
    suspend fun updateProduk(
        @Header("Authorization") token: String,
        @Field("id_produk") idProduk: String,
        @Field("nama_produk") namaProduk: String,
        @Field("harga") harga: String,
        @Field("warna") warna: String
    ): Response<ResponseApi<Any>>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "produk/delete.php", hasBody = true)
    suspend fun deleteProduk(
        @Header("Authorization") token: String,
        @Field("id_produk") idProduk: String
    ): Response<ResponseApi<Any>>

    // Stok Endpoints
    @FormUrlEncoded
    @POST("stok/tambah.php")
    suspend fun tambahStok(
        @Header("Authorization") token: String,
        @Field("id_produk") idProduk: String,
        @Field("jumlah") jumlah: String
    ): Response<ResponseApi<Any>>

    @FormUrlEncoded
    @POST("stok/kurangi.php")
    suspend fun kurangiStok(
        @Header("Authorization") token: String,
        @Field("id_produk") idProduk: String,
        @Field("jumlah") jumlah: String
    ): Response<ResponseApi<Any>>
}