<?php
include '../config/database.php';
include '../utils/jwt_helper.php';

header('Content-Type: application/json');

// Validasi Token
$headers = getallheaders();
$token = $headers['Authorization'] ?? '';
if (!validateToken($token)) {
    http_response_code(401);
    echo json_encode(["status" => "error", "message" => "Unauthorized"]);
    exit();
}

$id_kategori = $_GET['id_kategori'] ?? '';

// QUERY DENGAN JOIN KE TABEL STOK
if (!empty($id_kategori)) {
    // Get produk by kategori dengan stok
    $query = "SELECT p.*, s.jumlah_stok 
              FROM produk p 
              LEFT JOIN stok s ON p.id_produk = s.id_produk 
              WHERE p.id_kategori = '$id_kategori'";
} else {
    // Get all produk dengan stok
    $query = "SELECT p.*, s.jumlah_stok 
              FROM produk p 
              LEFT JOIN stok s ON p.id_produk = s.id_produk";
}

$result = mysqli_query($kon, $query);

if ($result) {
    $data = [];
    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = $row;
    }
    
    http_response_code(200);
    echo json_encode(["status" => "success", "data" => $data]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Gagal mengambil data"]);
}
?>