package com.example.etherealwardrobe.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etherealwardrobe.modeldata.DataKategori
import com.example.etherealwardrobe.repositori.RepositoryAuth
import com.example.etherealwardrobe.repositori.RepositoryKategori
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val kategoriList: List<DataKategori>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

sealed class KategoriActionState {
    object Idle : KategoriActionState()
    object Loading : KategoriActionState()
    data class Success(val message: String) : KategoriActionState()
    data class Error(val message: String) : KategoriActionState()
}

class DashboardViewModel(
    private val repositoryAuth: RepositoryAuth,
    private val repositoryKategori: RepositoryKategori
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<KategoriActionState>(KategoriActionState.Idle)
    val actionState: StateFlow<KategoriActionState> = _actionState.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _namaKategori = MutableStateFlow("")
    val namaKategori: StateFlow<String> = _namaKategori.asStateFlow()

    private val _kategoriToEdit = MutableStateFlow<DataKategori?>(null)
    val kategoriToEdit: StateFlow<DataKategori?> = _kategoriToEdit.asStateFlow()

    init {
        loadUserName()
        loadKategori()
    }

    private fun loadUserName() {
        _userName.value = repositoryAuth.getUserName() ?: "User"
    }

    fun loadKategori() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading

            val result = repositoryKategori.getAllKategori()

            _uiState.value = if (result.isSuccess) {
                val data = result.getOrNull() ?: emptyList()
                DashboardUiState.Success(data)
            } else {
                DashboardUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        }
    }

    fun onNamaKategoriChange(value: String) {
        _namaKategori.value = value
    }

    fun setKategoriToEdit(kategori: DataKategori?) {
        _kategoriToEdit.value = kategori
        _namaKategori.value = kategori?.namaKategori ?: ""
    }

    fun saveKategori() {
        if (_namaKategori.value.isBlank()) {
            _actionState.value = KategoriActionState.Error("Nama kategori wajib diisi")
            return
        }

        viewModelScope.launch {
            _actionState.value = KategoriActionState.Loading

            val result = if (_kategoriToEdit.value != null) {
                repositoryKategori.updateKategori(
                    _kategoriToEdit.value!!.idKategori,
                    _namaKategori.value
                )
            } else {
                repositoryKategori.createKategori(_namaKategori.value)
            }

            _actionState.value = if (result.isSuccess) {
                loadKategori()
                clearForm()
                KategoriActionState.Success(result.getOrNull() ?: "Berhasil menyimpan kategori")
            } else {
                KategoriActionState.Error(result.exceptionOrNull()?.message ?: "Gagal menyimpan kategori")
            }
        }
    }

    fun deleteKategori(idKategori: String) {
        viewModelScope.launch {
            _actionState.value = KategoriActionState.Loading

            val result = repositoryKategori.deleteKategori(idKategori)

            _actionState.value = if (result.isSuccess) {
                loadKategori()
                KategoriActionState.Success(result.getOrNull() ?: "Kategori berhasil dihapus")
            } else {
                KategoriActionState.Error(result.exceptionOrNull()?.message ?: "Gagal menghapus kategori")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _actionState.value = KategoriActionState.Loading

            val result = repositoryAuth.logout()

            _actionState.value = if (result.isSuccess) {
                KategoriActionState.Success(result.getOrNull() ?: "Logout berhasil")
            } else {
                KategoriActionState.Error(result.exceptionOrNull()?.message ?: "Logout gagal")
            }
        }
    }

    fun clearForm() {
        _namaKategori.value = ""
        _kategoriToEdit.value = null
    }

    fun resetActionState() {
        _actionState.value = KategoriActionState.Idle
    }
}