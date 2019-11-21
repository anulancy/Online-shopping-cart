<?php

include("connection.php");

$data = array();

$omid = $_REQUEST['omid'];


$sql = "SELECT * FROM tbl_order_child WHERE om_id='$omid'";
$result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($con));
    
While($row = mysqli_fetch_assoc($result))
{
	$query1 = "SELECT * FROM tbl_item WHERE id='$row[item_id]'";
	$result1 = mysqli_query($con,$query1);

	While($rowItem = mysqli_fetch_assoc($result1))
	{
		$data['OrderItem'][] = $rowItem;
	}

}

$data['OrderItem'] = array_values( $data['OrderItem']);	
echo json_encode($data); 

?>