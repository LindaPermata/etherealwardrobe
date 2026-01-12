package com.example.etherealwardrobe.repositori

import com.example.etherealwardrobe.apiservice.ServiceApiWardrobe
import com.example.etherealwardrobe.local.UserPreferences
import com.example.etherealwardrobe.modeldata.DataKategori

class RepositoryKategori(
    private val apiService: ServiceApiWardrobe,
    private val userPreferences: UserPreferences
) {

    private fun getAuthToken(): String {
        return userPreferences.getToken() ?: throw Exception("Token tidak ditemukan")
    }

    suspend fun getAllKategori(): Result<List<DataKategori>> {
        return try {
            val token = getAuthToken()
            val response = apiService.getKategori(token)

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

    suspend fun createKategori(namaKategori: String): Result<String> {
        return try {
            val token = getAuthToken()
            val response = apiService.createKategori(token, namaKategori)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal menambah kategori"))
                }
            } else {
                when (response.code()) {
                    409 -> Result.failure(Exception("Kategori sudah ada"))
                    401 -> Result.failure(Exception("Unauthorized"))
                    else -> Result.failure(Exception("Error: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateKategori(idKategori: String, namaKategori: String): Result<String> {
        return try {
            val token = getAuthToken()
            val response = apiService.updateKategori(token, idKategori, namaKategori)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal update kategori"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteKategori(idKategori: String): Result<String> {
        return try {
            val token = getAuthToken()
            val response = apiService.deleteKategori(token, idKategori)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Gagal menghapus kategori"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}