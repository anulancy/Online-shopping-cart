<?php

include("connection.php");

$sql = "UPDATE tbl_customer SET Address='$_REQUEST[address]', street='$_REQUEST[area]', city='$_REQUEST[city]', state='$_REQUEST[state]' , pincode='$_REQUEST[pincode]' 
		WHERE id='$_REQUEST[uid]' ";

mysqli_query($con,$sql);


?>