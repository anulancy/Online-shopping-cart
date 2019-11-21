<?php

include("connection.php");

$query="UPDATE tbl_order_master SET order_rating = '$_REQUEST[rating]' WHERE id = '$_REQUEST[orderId]';";
    
    if(mysqli_query($con, $query))
    	echo "Order Rated Sucessfully";


?>