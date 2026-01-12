package com.example.etherealwardrobe

import android.app.Application
import com.example.etherealwardrobe.repositori.AppContainer
import com.example.etherealwardrobe.repositori.ContainerApp

class EtherealWardrobeApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = ContainerApp(this)
    }
}