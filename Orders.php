<?php

include("connection.php");

$data = array();

$uid = $_REQUEST['uid'];


$sql = "SELECT * FROM tbl_order_master WHERE cust_id='$uid' AND status='PURCHASED'";
$result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($connection));
    
While($row = mysqli_fetch_assoc($result))
{
	$query1 = "SELECT * FROM tbl_order_child WHERE om_id='$row[id]'";
	$result1 = mysqli_query($con,$query1);

	$amount = 0;
	While($rowItem = mysqli_fetch_assoc($result1))
	{
		$itemsQuery = "SELECT * FROM tbl_item WHERE id='$rowItem[item_id]'";
		$amount = $amount + $rowItem['price'];
	}

 	$data['Order'][$row['id']]  = array( 'id' => $row['id'], 'date' => $row['o_date'], 'status' => $row['status'], 'order_number' => $row['order_number'],'order_rating' => $row['order_rating'], 'amount' => $amount);

}

$data['Order'] = array_values( $data['Order']);	
echo json_encode($data); 

?>