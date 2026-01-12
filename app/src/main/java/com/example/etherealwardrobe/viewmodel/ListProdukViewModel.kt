package com.example.etherealwardrobe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etherealwardrobe.modeldata.DataProduk
import com.example.etherealwardrobe.repositori.RepositoryProduk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ListProdukUiState {
    object Loading : ListProdukUiState()
    data class Success(val produkList: List<DataProduk>) : ListProdukUiState()
    data class Error(val message: String) : ListProdukUiState()
}

sealed class ProdukActionState {
    object Idle : ProdukActionState()
    object Loading : ProdukActionState()
    data class Success(val message: String) : ProdukActionState()
    data class Error(val message: String) : ProdukActionState()
}

class ListProdukViewModel(
    private val repositoryProduk: RepositoryProduk
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListProdukUiState>(ListProdukUiState.Loading)
    val uiState: StateFlow<ListProdukUiState> = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<ProdukActionState>(ProdukActionState.Idle)
    val actionState: StateFlow<ProdukActionState> = _actionState.asStateFlow()

    fun loadProdukByKategori(idKategori: String) {
        viewModelScope.launch {
            _uiState.value = ListProdukUiState.Loading

            val result = repositoryProduk.getProdukByKategori(idKategori)

            _uiState.value = if (result.isSuccess) {
                val data = result.getOrNull() ?: emptyList()
                ListProdukUiState.Success(data)
            } else {
                ListProdukUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        }
    }

    fun deleteProduk(idProduk: String, idKategori: String) {
        viewModelScope.launch {
            _actionState.value = ProdukActionState.Loading

            val result = repositoryProduk.deleteProduk(idProduk)

            _actionState.value = if (result.isSuccess) {
                // Reload produk setelah delete
                loadProdukByKategori(idKategori)
                ProdukActionState.Success(result.getOrNull() ?: "Produk berhasil dihapus")
            } else {
                ProdukActionState.Error(result.exceptionOrNull()?.message ?: "Gagal menghapus produk")
            }
        }
    }

    fun resetActionState() {
        _actionState.value = ProdukActionState.Idle
    }
}