<?php
include '../config/database.php';
header('Content-Type: application/json');

parse_str(file_get_contents("php://input"), $_DELETE);
$id = $_DELETE['id_kategori'] ?? '';

if (empty($id)) {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "ID kategori diperlukan"]);
    exit();
}

$query = "DELETE FROM kategori WHERE id_kategori = '$id'";
if (mysqli_query($kon, $query)) {
    http_response_code(200);
    echo json_encode(["status" => "success", "message" => "Kategori berhasil dihapus"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Gagal menghapus kategori"]);
}
?>