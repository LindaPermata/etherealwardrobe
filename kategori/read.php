<?php
include '../config/database.php';
include '../utils/jwt_helper.php';

header('Content-Type: application/json');

// Proteksi Token (Opsional untuk Read, tapi disarankan)
$headers = getallheaders();
$token = $headers['Authorization'] ?? '';
if (!validateToken($token)) {
    http_response_code(401);
    echo json_encode(["status" => "error", "message" => "Unauthorized"]);
    exit();
}

$query = "SELECT * FROM kategori ORDER BY created_at DESC";
$result = mysqli_query($kon, $query);

$data = [];
while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

http_response_code(200);
echo json_encode(["status" => "success", "data" => $data]);
?>