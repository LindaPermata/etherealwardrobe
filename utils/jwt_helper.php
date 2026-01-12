<?php
// Ganti 'KUNCI_RAHASIA_ETHEREAL_086' dengan kata kunci bebas yang sulit ditebak
$secret_key = "KUNCI_RAHASIA_ETHEREAL_086";

/**
 * Fungsi untuk membuat Token (saat login berhasil)
 */
function generateToken($user_id, $email) {
    global $secret_key;
    
    // Header & Payload (data yang disimpan dalam token)
    $header = json_encode(['typ' => 'JWT', 'alg' => 'HS256']);
    $payload = json_encode([
        'user_id' => $user_id,
        'email' => $email,
        'iat' => time(), // waktu dibuat
        'exp' => time() + (3600 * 24) // berlaku 24 jam
    ]);

    // Encode Base64
    $base64UrlHeader = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($header));
    $base64UrlPayload = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($payload));

    // Membuat Signature (tanda tangan keamanan)
    $signature = hash_hmac('sha256', $base64UrlHeader . "." . $base64UrlPayload, $secret_key, true);
    $base64UrlSignature = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($signature));

    // Gabungkan menjadi satu string Token
    return $base64UrlHeader . "." . $base64UrlPayload . "." . $base64UrlSignature;
}

/**
 * Fungsi untuk validasi Token (saat akses produk/kategori/stok)
 */
function validateToken($token) {
    global $secret_key;

    // Pisahkan token menjadi 3 bagian
    $tokenParts = explode('.', $token);
    if (count($tokenParts) !== 3) return false;

    $header = $tokenParts[0];
    $payload = $tokenParts[1];
    $signatureProvided = $tokenParts[2];

    // Cek Signature kembali
    $signature = hash_hmac('sha256', $header . "." . $payload, $secret_key, true);
    $base64UrlSignature = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($signature));

    // Jika signature cocok, berarti token asli
    if ($base64UrlSignature === $signatureProvided) {
        $payloadData = json_decode(base64_decode($payload), true);
        // Cek apakah sudah expired (waktu habis)
        if ($payloadData['exp'] < time()) {
            return false; 
        }
        return $payloadData; // Kembalikan data user jika valid
    }

    return false;
}
?>