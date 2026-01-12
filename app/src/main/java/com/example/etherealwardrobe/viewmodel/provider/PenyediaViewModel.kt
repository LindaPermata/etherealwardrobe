package com.example.etherealwardrobe.viewmodel.provider


import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.etherealwardrobe.EtherealWardrobeApplication
import com.example.etherealwardrobe.viewmodel.DashboardViewModel
import com.example.etherealwardrobe.viewmodel.DetailProdukViewModel
import com.example.etherealwardrobe.viewmodel.FormProdukViewModel
import com.example.etherealwardrobe.viewmodel.ListProdukViewModel
import com.example.etherealwardrobe.viewmodel.LoginViewModel

object PenyediaViewModel {

    val Factory = viewModelFactory {
        // Login ViewModel
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EtherealWardrobeApplication)
            val repositoryAuth = application.container.repositoryAuth
            LoginViewModel(repositoryAuth)
        }

        // Dashboard ViewModel
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EtherealWardrobeApplication)
            val repositoryAuth = application.container.repositoryAuth
            val repositoryKategori = application.container.repositoryKategori
            DashboardViewModel(repositoryAuth, repositoryKategori)
        }

        // List Produk ViewModel
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EtherealWardrobeApplication)
            val repositoryProduk = application.container.repositoryProduk
            ListProdukViewModel(repositoryProduk)
        }

        // Detail Produk ViewModel
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EtherealWardrobeApplication)
            val repositoryProduk = application.container.repositoryProduk
            DetailProdukViewModel(repositoryProduk)
        }

        // Form Produk ViewModel
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EtherealWardrobeApplication)
            val repositoryProduk = application.container.repositoryProduk
            val repositoryKategori = application.container.repositoryKategori
            FormProdukViewModel(repositoryProduk, repositoryKategori)
        }
    }
}