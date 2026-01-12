<?php
include '../config/database.php';
include '../utils/jwt_helper.php';

header('Content-Type: application/json');

// Pastikan metode adalah POST
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(["status" => "error", "message" => "Metode harus POST"]);
    exit();
}

/** * BAGIAN TAMBAHAN: Membaca input JSON (Postman raw JSON)
 * Jika $_POST kosong, sistem akan mencoba mengambil dari php://input
 */
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

$email = $_POST['email'] ?? $input['email'] ?? '';
$password = $_POST['password'] ?? $input['password'] ?? '';

if (empty($email) || empty($password)) {
    http_response_code(400);
    echo json_encode(["status" => "error", "message" => "Email dan Password wajib diisi"]);
    exit();
}

// Enkripsi password dengan MD5
$hashed_password = md5($password);

// DISESUAIKAN: Nama tabel di screenshot kamu adalah 'user' (bukan 'users')
$query = "SELECT * FROM user WHERE email = '$email' AND password = '$hashed_password'";
$result = mysqli_query($kon, $query);

if (mysqli_num_rows($result) > 0) {
    $user = mysqli_fetch_assoc($result);
    
    // Generate Token
    $token = generateToken($user['id_user'], $user['email']);
    
    // Simpan token ke tabel 'tokens'
    $user_id = $user['id_user'];
    mysqli_query($kon, "INSERT INTO token (id_user, token) VALUES ('$user_id', '$token')");

    http_response_code(200);
    echo json_encode([
        "status" => "success",
        "message" => "Login Berhasil",
        "data" => [
            "id_user" => $user['id_user'],
            "nama_lengkap" => $user['nama_lengkap'],
            "token" => $token
        ]
    ]);
} else {
    http_response_code(401);
    echo json_encode(["status" => "error", "message" => "Email atau Password salah"]);
}
?>