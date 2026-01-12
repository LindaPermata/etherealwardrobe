package com.example.etherealwardrobe.repositori


import com.example.etherealwardrobe.apiservice.ServiceApiWardrobe
import com.example.etherealwardrobe.local.UserPreferences
import com.example.etherealwardrobe.modeldata.DataUser
import com.example.etherealwardrobe.modeldata.ResponseApi

class RepositoryAuth(
    private val apiService: ServiceApiWardrobe,
    private val userPreferences: UserPreferences
) {

    suspend fun login(email: String, password: String): Result<DataUser> {
        return try {
            val response = apiService.login(email, password)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success" && body.data != null) {
                    // Simpan data login ke SharedPreferences
                    userPreferences.saveLoginData(
                        token = body.data.token,
                        userId = body.data.idUser,
                        userName = body.data.namaLengkap
                    )
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Login gagal"))
                }
            } else {
                when (response.code()) {
                    401 -> Result.failure(Exception("Email atau Password salah"))
                    else -> Result.failure(Exception("Error: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<String> {
        return try {
            val token = userPreferences.getToken() ?: return Result.failure(Exception("Token tidak ditemukan"))
            val response = apiService.logout(token)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status == "success") {
                    userPreferences.clearLoginData()
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Logout gagal"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isLoggedIn(): Boolean {
        return userPreferences.isLoggedIn()
    }

    fun getToken(): String? {
        return userPreferences.getToken()
    }

    fun getUserName(): String? {
        return userPreferences.getUserName()
    }
}