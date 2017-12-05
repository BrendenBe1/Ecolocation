<?php
require "conn.php";
$location_lat = $_POST["user_name"];
$location_lon = $_POST["password"];

$query = "SELECT genus, species FROM iucn";
$result = mysql_query($conn, $query);

if(mysqli_num_rows($result) > 0){
	echo "Received Animal Data";
}
else{
	echo "Failed to get data";
}
