<?php
include '../config/database.php';
include '../utils/jwt_helper.php';

header('Content-Type: application/json');

// Ambil input PUT
parse_str(file_get_contents("php://input"), $_PUT);

$id = $_PUT['id_kategori'] ?? '';
$nama = $_PUT['nama_kategori'] ?? '';

if (empty($id) || empty($nama)) {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "ID dan Nama wajib diisi"]);
    exit();
}

$query = "UPDATE kategori SET nama_kategori = '$nama' WHERE id_kategori = '$id'";
if (mysqli_query($kon, $query)) {
    http_response_code(200);
    echo json_encode(["status" => "success", "message" => "Kategori berhasil diperbarui"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Gagal update"]);
}
?>