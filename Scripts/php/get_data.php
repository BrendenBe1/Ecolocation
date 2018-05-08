<?php
	$lat = $_POST["latitude"];
	$lon = $_POST["longitude"];
	
	$con = mysqli_connect("ecolocation.c09lpapromur.us-east-2.rds.amazonaws.com:3306", "TeamEcolocation", "EcolocationData", "ecolocation_data");
	if(mysqli_connect_errno()){
		echo "Failed to connect: ". mysqli_connect_error();
	}

	mysqli_set_charset($con, 'utf8');
	
	$sql = "Select binomial, common_name, mass, code, wiki_link, description FROM iucn WHERE mass >= 1000 AND st_contains(boundaries, GeomFromText('Point($lon $lat)'))";

	$result = mysqli_query($con, $sql);
	while($row = mysqli_fetch_assoc($result)){
		$array[] = $row;
	}
	header('Content-Type:Application/json');
	echo json_encode($array);
	mysqli_free_result($result);
	mysqli_close($con);
?>
