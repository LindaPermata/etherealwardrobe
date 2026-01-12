package com.example.etherealwardrobe.repositori


import android.content.Context
import com.example.etherealwardrobe.apiservice.ServiceApiWardrobe
import com.example.etherealwardrobe.local.UserPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val repositoryAuth: RepositoryAuth
    val repositoryKategori: RepositoryKategori
    val repositoryProduk: RepositoryProduk
}

class ContainerApp(private val context: Context) : AppContainer {

    private val BASE_URL = "http://10.0.2.2/ethereal_api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ServiceApiWardrobe by lazy {
        retrofit.create(ServiceApiWardrobe::class.java)
    }

    private val userPreferences: UserPreferences by lazy {
        UserPreferences(context)
    }

    override val repositoryAuth: RepositoryAuth by lazy {
        RepositoryAuth(apiService, userPreferences)
    }

    override val repositoryKategori: RepositoryKategori by lazy {
        RepositoryKategori(apiService, userPreferences)
    }

    override val repositoryProduk: RepositoryProduk by lazy {
        RepositoryProduk(apiService, userPreferences)
    }
}