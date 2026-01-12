package com.example.etherealwardrobe.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etherealwardrobe.modeldata.DataProduk
import com.example.etherealwardrobe.repositori.RepositoryProduk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DetailProdukUiState {
    object Idle : DetailProdukUiState()
    object Loading : DetailProdukUiState()
    data class Success(val message: String, val shouldRefreshList: Boolean = false) : DetailProdukUiState()
    data class Error(val message: String) : DetailProdukUiState()
}

class DetailProdukViewModel(
    private val repositoryProduk: RepositoryProduk
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailProdukUiState>(DetailProdukUiState.Idle)
    val uiState: StateFlow<DetailProdukUiState> = _uiState.asStateFlow()

    private val _currentProduk = MutableStateFlow<DataProduk?>(null)
    val currentProduk: StateFlow<DataProduk?> = _currentProduk.asStateFlow()

    fun setProduk(produk: DataProduk) {
        _currentProduk.value = produk
        Log.d("DetailProdukVM", "Initial produk set. ID: ${produk.idProduk}, Stok: ${produk.jumlahStok}")
    }

    // Fungsi PUBLIC untuk force refresh dari luar
    fun forceRefreshProduk() {
        viewModelScope.launch {
            _currentProduk.value?.let { produk ->
                Log.d("DetailProdukVM", "Force refreshing produk...")
                refreshProdukFromAPI(produk.idKategori, produk.idProduk)
            }
        }
    }

    private suspend fun refreshProdukFromAPI(idKategori: String, idProduk: String) {
        try {
            // Delay untuk kasih waktu backend update
            delay(500)

            Log.d("DetailProdukVM", "Fetching fresh data from API...")

            val result = repositoryProduk.getProdukByKategori(idKategori)

            if (result.isSuccess) {
                val produkList = result.getOrNull() ?: emptyList()
                Log.d("DetailProdukVM", "API returned ${produkList.size} products")

                val updatedProduk = produkList.find { it.idProduk == idProduk }

                if (updatedProduk != null) {
                    val oldStok = _currentProduk.value?.jumlahStok ?: "?"
                    val newStok = updatedProduk.jumlahStok ?: "?"

                    Log.d("DetailProdukVM", "SUCCESS! Stok updated: $oldStok -> $newStok")

                    // Force update dengan data baru
                    _currentProduk.value = updatedProduk
                } else {
                    Log.e("DetailProdukVM", "ERROR: Produk not found in API response!")
                }
            } else {
                Log.e("DetailProdukVM", "ERROR: API call failed - ${result.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            Log.e("DetailProdukVM", "ERROR: Exception while refreshing", e)
        }
    }

    fun deleteProduk(idProduk: String) {
        viewModelScope.launch {
            _uiState.value = DetailProdukUiState.Loading

            val result = repositoryProduk.deleteProduk(idProduk)

            _uiState.value = if (result.isSuccess) {
                DetailProdukUiState.Success(result.getOrNull() ?: "Produk berhasil dihapus")
            } else {
                DetailProdukUiState.Error(result.exceptionOrNull()?.message ?: "Gagal menghapus produk")
            }
        }
    }

    fun tambahStok(idProduk: String, jumlah: String) {
        viewModelScope.launch {
            _uiState.value = DetailProdukUiState.Loading

            Log.d("DetailProdukVM", "=== TAMBAH STOK START ===")
            Log.d("DetailProdukVM", "ID Produk: $idProduk, Jumlah: $jumlah")

            val result = repositoryProduk.tambahStok(idProduk, jumlah)

            if (result.isSuccess) {
                Log.d("DetailProdukVM", "API tambahStok SUCCESS")

                // Refresh data dari API
                _currentProduk.value?.let { produk ->
                    refreshProdukFromAPI(produk.idKategori, produk.idProduk)
                }

                _uiState.value = DetailProdukUiState.Success("Stok berhasil ditambah", shouldRefreshList = true)
            } else {
                Log.e("DetailProdukVM", "API tambahStok FAILED: ${result.exceptionOrNull()?.message}")
                _uiState.value = DetailProdukUiState.Error(result.exceptionOrNull()?.message ?: "Gagal menambah stok")
            }

            Log.d("DetailProdukVM", "=== TAMBAH STOK END ===")
        }
    }

    fun kurangiStok(idProduk: String, jumlah: String) {
        viewModelScope.launch {
            _uiState.value = DetailProdukUiState.Loading

            Log.d("DetailProdukVM", "=== KURANGI STOK START ===")
            Log.d("DetailProdukVM", "ID Produk: $idProduk, Jumlah: $jumlah")

            val result = repositoryProduk.kurangiStok(idProduk, jumlah)

            if (result.isSuccess) {
                Log.d("DetailProdukVM", "API kurangiStok SUCCESS")

                // Refresh data dari API
                _currentProduk.value?.let { produk ->
                    refreshProdukFromAPI(produk.idKategori, produk.idProduk)
                }

                _uiState.value = DetailProdukUiState.Success("Stok berhasil dikurangi", shouldRefreshList = true)
            } else {
                Log.e("DetailProdukVM", "API kurangiStok FAILED: ${result.exceptionOrNull()?.message}")
                _uiState.value = DetailProdukUiState.Error(result.exceptionOrNull()?.message ?: "Gagal mengurangi stok")
            }

            Log.d("DetailProdukVM", "=== KURANGI STOK END ===")
        }
    }

    fun resetState() {
        _uiState.value = DetailProdukUiState.Idle
    }
}