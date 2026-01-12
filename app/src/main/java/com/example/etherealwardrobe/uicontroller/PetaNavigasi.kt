package com.example.etherealwardrobe.uicontrollerimport

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.etherealwardrobe.modeldata.DataKategori
import com.example.etherealwardrobe.modeldata.DataProduk
import com.example.etherealwardrobe.uicontroller.route.DestinasiDashboard
import com.example.etherealwardrobe.uicontroller.route.DestinasiDetailProduk
import com.example.etherealwardrobe.uicontroller.route.DestinasiFormProduk
import com.example.etherealwardrobe.uicontroller.route.DestinasiListProduk
import com.example.etherealwardrobe.uicontroller.route.DestinasiLogin
import com.example.etherealwardrobe.view.*
import com.example.etherealwardrobe.viewmodel.DashboardViewModel
import com.example.etherealwardrobe.viewmodel.DetailProdukViewModel
import com.example.etherealwardrobe.viewmodel.FormProdukViewModel
import com.example.etherealwardrobe.viewmodel.ListProdukViewModel
import com.example.etherealwardrobe.viewmodel.LoginViewModel
import com.example.etherealwardrobe.viewmodel.provider.*

@Composable
fun PetaNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiLogin.route,
        modifier = modifier
    ) {
        // Login Screen
        composable(route = DestinasiLogin.route) {
            val viewModel: LoginViewModel = viewModel(factory = PenyediaViewModel.Factory)
            HalamanLogin(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(DestinasiDashboard.route) {
                        popUpTo(DestinasiLogin.route) { inclusive = true }
                    }
                }
            )
        }

        // Dashboard Screen (CRUD Kategori)
        composable(route = DestinasiDashboard.route) {
            val viewModel: DashboardViewModel = viewModel(factory = PenyediaViewModel.Factory)
            HalamanDashboard(
                viewModel = viewModel,
                onKategoriClick = { kategori ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("kategori", kategori)
                    navController.navigate("${DestinasiListProduk.route}/${kategori.idKategori}")
                },
                onLogoutSuccess = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // List Produk Screen (by Kategori)
        composable(
            route = "${DestinasiListProduk.route}/{${DestinasiListProduk.idKategoriArg}}",
            arguments = listOf(
                navArgument(DestinasiListProduk.idKategoriArg) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val viewModel: ListProdukViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val kategori = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<DataKategori>("kategori")
            val idKategori = backStackEntry.arguments
                ?.getString(DestinasiListProduk.idKategoriArg) ?: ""

            HalamanListProduk(
                viewModel = viewModel,
                kategori = kategori,
                idKategori = idKategori,
                onBackClick = {
                    navController.navigateUp()
                },
                onProdukClick = { produk ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("produk", produk)
                    navController.navigate("${DestinasiDetailProduk.route}/${produk.idProduk}")
                },
                onTambahProdukClick = {
                    navController.navigate(
                        "${DestinasiFormProduk.route}?" +
                                "${DestinasiFormProduk.isEditArg}=false&" +
                                "${DestinasiFormProduk.idKategoriArg}=$idKategori"
                    )
                }
            )
        }

        // Detail Produk Screen
        composable(
            route = "${DestinasiDetailProduk.route}/{${DestinasiDetailProduk.idProdukArg}}",
            arguments = listOf(
                navArgument(DestinasiDetailProduk.idProdukArg) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val viewModel: DetailProdukViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val produk = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<DataProduk>("produk")

            produk?.let { p ->
                HalamanDetailProduk(
                    viewModel = viewModel,
                    produk = p,
                    onBackClick = {
                        // PENTING: Set flag refresh untuk List Produk
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("refresh", true)
                        navController.navigateUp()
                    },
                    onEditClick = { prod ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("editProduk", prod)
                        navController.navigate(
                            "${DestinasiFormProduk.route}?" +
                                    "${DestinasiFormProduk.isEditArg}=true&" +
                                    "${DestinasiFormProduk.idKategoriArg}=${prod.idKategori}"
                        )
                    },
                    onDeleteSuccess = {
                        // Set refresh flag sebelum navigateUp
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("refresh", true)
                        navController.navigateUp()
                    }
                )
            }
        }

        // Form Produk Screen
        composable(
            route = "${DestinasiFormProduk.route}?" +
                    "${DestinasiFormProduk.isEditArg}={${DestinasiFormProduk.isEditArg}}&" +
                    "${DestinasiFormProduk.idKategoriArg}={${DestinasiFormProduk.idKategoriArg}}&" +
                    "${DestinasiFormProduk.idProdukArg}={${DestinasiFormProduk.idProdukArg}}",
            arguments = listOf(
                navArgument(DestinasiFormProduk.isEditArg) {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument(DestinasiFormProduk.idKategoriArg) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(DestinasiFormProduk.idProdukArg) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val viewModel: FormProdukViewModel = viewModel(factory = PenyediaViewModel.Factory)
            val isEdit = backStackEntry.arguments
                ?.getBoolean(DestinasiFormProduk.isEditArg) ?: false
            val idKategori = backStackEntry.arguments
                ?.getString(DestinasiFormProduk.idKategoriArg)
            val editProduk = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<DataProduk>("editProduk")

            HalamanFormProduk(
                viewModel = viewModel,
                isEdit = isEdit,
                idKategoriFromNav = idKategori,
                produkToEdit = editProduk,
                onBackClick = {
                    navController.navigateUp()
                },
                onSaveSuccess = {
                    navController.navigateUp()
                }
            )
        }
    }
}