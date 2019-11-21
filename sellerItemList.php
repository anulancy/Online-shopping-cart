<?php

include("connection.php");

$query="SELECT * FROM tbl_seller_item WHERE scat_id='$_REQUEST[scatid]'";
    $result = mysqli_query($con, $query) or die("Error in Selecting " . mysqli_error($connection));
    
	 $data = array();

    while($row =mysqli_fetch_assoc($result))
    {
        $data['data'][] = $row;
		
    }
    	
	echo json_encode($data);

?>