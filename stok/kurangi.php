<?php
// kurangi.php
include '../config/database.php';
include '../utils/jwt_helper.php';

header('Content-Type: application/json');

$headers = getallheaders();
$token = $headers['Authorization'] ?? '';
if (!validateToken($token)) {
    http_response_code(401);
    echo json_encode(["status" => "error", "message" => "Unauthorized"]);
    exit();
}

// GANTI JADI $_POST
$id_produk = $_POST['id_produk'] ?? '';
$jumlah_kurang = $_POST['jumlah'] ?? 0;

if (empty($id_produk) || $jumlah_kurang <= 0) {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "ID Produk dan Jumlah harus valid"]);
    exit();
}

$cek_stok = mysqli_query($kon, "SELECT jumlah_stok FROM stok WHERE id_produk = '$id_produk'");
$data = mysqli_fetch_assoc($cek_stok);

if (!$data) {
    http_response_code(404);
    echo json_encode(["status" => "error", "message" => "Data stok produk tidak ditemukan"]);
    exit();
}

if ($data['jumlah_stok'] < $jumlah_kurang) {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "Gagal! Stok tidak boleh negatif"]);
    exit();
}

$query = "UPDATE stok SET jumlah_stok = jumlah_stok - $jumlah_kurang WHERE id_produk = '$id_produk'";

if (mysqli_query($kon, $query)) {
    http_response_code(200);
    echo json_encode(["status" => "success", "message" => "Stok berhasil dikurangi"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Gagal update stok"]);
}
?>