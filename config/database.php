<?php
$host = "localhost";
$user = "root";
$pass = "";
$db   = "ethereal_wardrobe_db"; // Sesuaikan dengan nama database di phpMyAdmin

$kon = mysqli_connect($host, $user, $pass, $db);

if (!$kon) {
    die("Koneksi gagal: " . mysqli_connect_error());
}
?>