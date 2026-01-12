<?php
include '../config/database.php';
include '../utils/jwt_helper.php';

header('Content-Type: application/json');

// Proteksi Token
$headers = getallheaders();
$token = $headers['Authorization'] ?? '';
if (!validateToken($token)) {
    http_response_code(401);
    echo json_encode(["status" => "error", "message" => "Unauthorized"]);
    exit();
}

// Ambil data dari POST
$nama   = $_POST['nama_produk'] ?? '';
$id_kat = $_POST['id_kategori'] ?? '';
$harga  = $_POST['harga'] ?? '';
$warna  = $_POST['warna'] ?? '';
$stok   = $_POST['stok_awal'] ?? 0;

// Validasi input wajib (REQ-PROD-ADD-02)
if (empty($nama) || empty($id_kat) || empty($harga)) {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "Nama, Kategori, dan Harga wajib diisi"]);
    exit();
}

// 1. Simpan ke tabel produk
$query_prod = "INSERT INTO produk (nama_produk, id_kategori, harga, warna) 
               VALUES ('$nama', '$id_kat', '$harga', '$warna')";

if (mysqli_query($kon, $query_prod)) {
    $id_produk_baru = mysqli_insert_id($kon);
    
    // 2. Simpan stok awal ke tabel stok (Sesuai Flowchart halaman 41) [cite: 980]
    mysqli_query($kon, "INSERT INTO stok (id_produk, jumlah_stok) VALUES ('$id_produk_baru', '$stok')");
    
    http_response_code(201);
    echo json_encode(["status" => "success", "message" => "Produk berhasil ditambahkan"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Gagal menyimpan produk"]);
}
?>