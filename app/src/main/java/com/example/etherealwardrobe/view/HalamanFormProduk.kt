package com.example.etherealwardrobe.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.etherealwardrobe.R
import com.example.etherealwardrobe.modeldata.DataProduk
import com.example.etherealwardrobe.viewmodel.FormProdukUiState
import com.example.etherealwardrobe.viewmodel.FormProdukViewModel
import com.example.etherealwardrobe.viewmodel.provider.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanFormProduk(
    viewModel: FormProdukViewModel,
    isEdit: Boolean,
    idKategoriFromNav: String?,
    produkToEdit: DataProduk?,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val kategoriList by viewModel.kategoriList.collectAsState()
    val namaProduk by viewModel.namaProduk.collectAsState()
    val selectedKategori by viewModel.selectedKategori.collectAsState()
    val harga by viewModel.harga.collectAsState()
    val warna by viewModel.warna.collectAsState()
    val stokAwal by viewModel.stokAwal.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()

    var expandedKategori by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Set edit mode if editing
    LaunchedEffect(produkToEdit) {
        if (isEdit && produkToEdit != null) {
            viewModel.setEditMode(produkToEdit)
        }
    }

    // Handle UI state
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is FormProdukUiState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                onSaveSuccess()
                viewModel.resetState()
            }
            is FormProdukUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) stringResource(R.string.edit_produk)
                        else stringResource(R.string.tambah_produk)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nama Produk
            OutlinedTextField(
                value = namaProduk,
                onValueChange = { viewModel.onNamaProdukChange(it) },
                label = { Text(stringResource(R.string.nama_produk)) },
                placeholder = { Text(stringResource(R.string.nama_produk_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = uiState !is FormProdukUiState.Loading
            )

            // Kategori Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedKategori,
                onExpandedChange = { expandedKategori = !expandedKategori }
            ) {
                OutlinedTextField(
                    value = selectedKategori?.namaKategori ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.kategori)) },
                    placeholder = { Text(stringResource(R.string.pilih_kategori)) },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    enabled = uiState !is FormProdukUiState.Loading && !isEditMode
                )

                ExposedDropdownMenu(
                    expanded = expandedKategori,
                    onDismissRequest = { expandedKategori = false }
                ) {
                    kategoriList.forEach { kategori ->
                        DropdownMenuItem(
                            text = { Text(kategori.namaKategori) },
                            onClick = {
                                viewModel.onKategoriSelected(kategori)
                                expandedKategori = false
                            }
                        )
                    }
                }
            }

            // Harga
            OutlinedTextField(
                value = harga,
                onValueChange = { viewModel.onHargaChange(it) },
                label = { Text(stringResource(R.string.harga)) },
                placeholder = { Text(stringResource(R.string.harga_hint)) },
                leadingIcon = { Text("Rp") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = uiState !is FormProdukUiState.Loading
            )

            // Warna
            OutlinedTextField(
                value = warna,
                onValueChange = { viewModel.onWarnaChange(it) },
                label = { Text(stringResource(R.string.warna)) },
                placeholder = { Text(stringResource(R.string.warna_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = uiState !is FormProdukUiState.Loading
            )

            // Stok Awal (hanya saat tambah)
            if (!isEditMode) {
                OutlinedTextField(
                    value = stokAwal,
                    onValueChange = { viewModel.onStokAwalChange(it) },
                    label = { Text(stringResource(R.string.stok_awal)) },
                    placeholder = { Text(stringResource(R.string.stok_awal_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = uiState !is FormProdukUiState.Loading
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = { viewModel.saveProduk() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = uiState !is FormProdukUiState.Loading
            ) {
                if (uiState is FormProdukUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.simpan))
                }
            }
        }
    }
}