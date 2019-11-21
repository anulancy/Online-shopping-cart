<?php

include("connection.php");

$uid = $_REQUEST['uid'];

$query = "SELECT * FROM tbl_order_master WHERE cust_id='$uid' and status='cart'";
$Omid_result = mysqli_query($con,$query);

$OmidRow = mysqli_fetch_assoc($Omid_result);

$om_id = $OmidRow['id'];

$sql = "SELECT * FROM tbl_order_child WHERE om_id='$om_id'";
$result = mysqli_query($con,$sql);

$count = mysqli_num_rows($result);

echo $count;

?>