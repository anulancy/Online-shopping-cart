<?php

include("connection.php");

$data = array();

$query = "SELECT * FROM tbl_seller_category";
$result = mysqli_query($con,$query);

While($row = mysqli_fetch_assoc($result))
{

 	$data['sellercat'][]  = $row;

}

// echo json_encode($data);

		 $data['sellercat'] = array_values( $data['sellercat']);
	
	echo json_encode($data); 

?>