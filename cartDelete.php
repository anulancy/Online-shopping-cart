<?php

include("connection.php");

$query = "DELETE FROM tbl_order_child WHERE id='$_REQUEST[did]'";
mysqli_query($con,$query);

echo "Deleted";

?>