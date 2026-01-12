package com.example.etherealwardrobe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etherealwardrobe.modeldata.DataKategori
import com.example.etherealwardrobe.modeldata.DataProduk
import com.example.etherealwardrobe.repositori.RepositoryKategori
import com.example.etherealwardrobe.repositori.RepositoryProduk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FormProdukUiState {
    object Idle : FormProdukUiState()
    object Loading : FormProdukUiState()
    data class Success(val message: String) : FormProdukUiState()
    data class Error(val message: String) : FormProdukUiState()
}

class FormProdukViewModel(
    private val repositoryProduk: RepositoryProduk,
    private val repositoryKategori: RepositoryKategori
) : ViewModel() {

    private val _uiState = MutableStateFlow<FormProdukUiState>(FormProdukUiState.Idle)
    val uiState: StateFlow<FormProdukUiState> = _uiState.asStateFlow()

    private val _kategoriList = MutableStateFlow<List<DataKategori>>(emptyList())
    val kategoriList: StateFlow<List<DataKategori>> = _kategoriList.asStateFlow()

    private val _namaProduk = MutableStateFlow("")
    val namaProduk: StateFlow<String> = _namaProduk.asStateFlow()

    private val _selectedKategori = MutableStateFlow<DataKategori?>(null)
    val selectedKategori: StateFlow<DataKategori?> = _selectedKategori.asStateFlow()

    private val _harga = MutableStateFlow("")
    val harga: StateFlow<String> = _harga.asStateFlow()

    private val _warna = MutableStateFlow("")
    val warna: StateFlow<String> = _warna.asStateFlow()

    private val _stokAwal = MutableStateFlow("")
    val stokAwal: StateFlow<String> = _stokAwal.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _currentProdukId = MutableStateFlow<String?>(null)

    init {
        loadKategori()
    }

    private fun loadKategori() {
        viewModelScope.launch {
            val result = repositoryKategori.getAllKategori()
            if (result.isSuccess) {
                _kategoriList.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    fun setEditMode(produk: DataProduk) {
        _isEditMode.value = true
        _currentProdukId.value = produk.idProduk
        _namaProduk.value = produk.namaProduk
        _harga.value = produk.harga
        _warna.value = produk.warna

        // Set selected kategori
        val kategori = _kategoriList.value.find { it.idKategori == produk.idKategori }
        _selectedKategori.value = kategori
    }

    fun setKategoriFromId(idKategori: String) {
        val kategori = _kategoriList.value.find { it.idKategori == idKategori }
        _selectedKategori.value = kategori
    }

    fun onNamaProdukChange(value: String) {
        _namaProduk.value = value
    }

    fun onKategoriSelected(kategori: DataKategori) {
        _selectedKategori.value = kategori
    }

    fun onHargaChange(value: String) {
        _harga.value = value
    }

    fun onWarnaChange(value: String) {
        _warna.value = value
    }

    fun onStokAwalChange(value: String) {
        _stokAwal.value = value
    }

    fun saveProduk() {
        if (_namaProduk.value.isBlank() || _selectedKategori.value == null || _harga.value.isBlank()) {
            _uiState.value = FormProdukUiState.Error("Nama, Kategori, dan Harga wajib diisi")
            return
        }

        viewModelScope.launch {
            _uiState.value = FormProdukUiState.Loading

            val result = if (_isEditMode.value) {
                repositoryProduk.updateProduk(
                    idProduk = _currentProdukId.value!!,
                    namaProduk = _namaProduk.value,
                    harga = _harga.value,
                    warna = _warna.value
                )
            } else {
                repositoryProduk.createProduk(
                    namaProduk = _namaProduk.value,
                    idKategori = _selectedKategori.value!!.idKategori,
                    harga = _harga.value,
                    warna = _warna.value,
                    stokAwal = _stokAwal.value.ifBlank { "0" }
                )
            }

            _uiState.value = if (result.isSuccess) {
                FormProdukUiState.Success(result.getOrNull() ?: "Berhasil menyimpan produk")
            } else {
                FormProdukUiState.Error(result.exceptionOrNull()?.message ?: "Gagal menyimpan produk")
            }
        }
    }

    fun resetState() {
        _uiState.value = FormProdukUiState.Idle
    }

    fun clearForm() {
        _namaProduk.value = ""
        _selectedKategori.value = null
        _harga.value = ""
        _warna.value = ""
        _stokAwal.value = ""
        _isEditMode.value = false
        _currentProdukId.value = null
    }
}