<?php
include '../config/database.php';
include '../utils/jwt_helper.php';

header('Content-Type: application/json');

parse_str(file_get_contents("php://input"), $_PUT);

$id     = $_PUT['id_produk'] ?? '';
$nama   = $_PUT['nama_produk'] ?? '';
$harga  = $_PUT['harga'] ?? '';
$warna  = $_PUT['warna'] ?? '';

if (empty($id)) {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "ID Produk diperlukan"]);
    exit();
}

$query = "UPDATE produk SET nama_produk='$nama', harga='$harga', warna='$warna' WHERE id_produk='$id'";

if (mysqli_query($kon, $query)) {
    http_response_code(200);
    echo json_encode(["status" => "success", "message" => "Data produk berhasil diperbarui"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Gagal update"]);
}
?>