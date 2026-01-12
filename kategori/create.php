<?php
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

$nama = $_POST['nama_kategori'] ?? '';

if (empty($nama)) {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "Nama kategori wajib diisi"]);
    exit();
}

// Cek duplikasi (REQ-KAT-ADD-02) [cite: 306]
$cek = mysqli_query($kon, "SELECT * FROM kategori WHERE nama_kategori = '$nama'");
if (mysqli_num_rows($cek) > 0) {
    http_response_code(409);
    echo json_encode(["status" => "error", "message" => "Kategori sudah ada"]);
    exit();
}

$query = "INSERT INTO kategori (nama_kategori) VALUES ('$nama')";
if (mysqli_query($kon, $query)) {
    http_response_code(201);
    echo json_encode(["status" => "success", "message" => "Kategori berhasil ditambahkan"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Gagal menyimpan"]);
}
?>