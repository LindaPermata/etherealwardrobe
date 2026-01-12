<?php
// tambah.php
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
$jumlah_tambah = $_POST['jumlah'] ?? 0;

if (empty($id_produk) || $jumlah_tambah <= 0) {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "ID Produk dan Jumlah harus valid"]);
    exit();
}

$query = "UPDATE stok SET jumlah_stok = jumlah_stok + $jumlah_tambah WHERE id_produk = '$id_produk'";

if (mysqli_query($kon, $query)) {
    http_response_code(200);
    echo json_encode(["status" => "success", "message" => "Stok berhasil ditambahkan"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Gagal update stok"]);
}
?>