package com.example.etherealwardrobe.repositori


import com.example.etherealwardrobe.apiservice.ServiceApiWardrobe
import com.example.etherealwardrobe.local.UserPreferences
import com.example.etherealwardrobe.modeldata.DataProduk

class RepositoryProduk(
    private val apiService: ServiceApiWardrobe,
    private val userPreferences: UserPreferences
) {

    private fun getAuthToken(): String {
        return userPreferences.getToken() ?: throw Exception("Token tidak ditemukan")
    }

    suspend fun getAllProduk(): Result<List<DataProduk>> {
        return try {
            val token = getAuthToken()
            val response = apiService.getAllProduk(token)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.data ?: emptyList())
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal mengambil data"))
                }
            } else {
                when (response.code()) {
                    401 -> Result.failure(Exception("Unauthorized"))
                    else -> Result.failure(Exception("Error: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProdukByKategori(idKategori: String): Result<List<DataProduk>> {
        return try {
            val token = getAuthToken()
            val response = apiService.getProdukByKategori(token, idKategori)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.data ?: emptyList())
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal mengambil data"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduk(
        namaProduk: String,
        idKategori: String,
        harga: String,
        warna: String,
        stokAwal: String
    ): Result<String> {
        return try {
            val token = getAuthToken()
            val response = apiService.createProduk(
                token, namaProduk, idKategori, harga, warna, stokAwal
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal menambah produk"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduk(
        idProduk: String,
        namaProduk: String,
        harga: String,
        warna: String
    ): Result<String> {
        return try {
            val token = getAuthToken()
            val response = apiService.updateProduk(token, idProduk, namaProduk, harga, warna)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal update produk"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduk(idProduk: String): Result<String> {
        return try {
            val token = getAuthToken()
            val response = apiService.deleteProduk(token, idProduk)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal menghapus produk"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun tambahStok(idProduk: String, jumlah: String): Result<String> {
        return try {
            val token = getAuthToken()
            val response = apiService.tambahStok(token, idProduk, jumlah)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal tambah stok"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun kurangiStok(idProduk: String, jumlah: String): Result<String> {
        return try {
            val token = getAuthToken()
            val response = apiService.kurangiStok(token, idProduk, jumlah)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal kurangi stok"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}