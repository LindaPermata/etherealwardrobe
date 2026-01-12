<?php
include '../config/database.php';

header('Content-Type: application/json');

// Logout harus pakai POST sesuai SRS kamu
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(["status" => "error", "message" => "Metode harus POST"]);
    exit();
}

// Ambil token dari Header Authorization
$headers = getallheaders();
$token = $headers['Authorization'] ?? '';

if (!empty($token)) {
    // Hapus token dari tabel 'token' agar tidak bisa dipakai lagi (sesuai SRS 3.5)
    $query = "DELETE FROM token WHERE token = '$token'";
    
    if (mysqli_query($kon, $query)) {
        http_response_code(200);
        echo json_encode(["status" => "success", "message" => "Logout berhasil, sesi telah berakhir"]);
    } else {
        http_response_code(500);
        echo json_encode(["status" => "error", "message" => "Gagal menghapus sesi"]);
    }
} else {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "Token tidak ditemukan di header"]);
}
?>