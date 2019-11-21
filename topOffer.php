<?php

include("connection.php");

	$sql = "SELECT * FROM tbl_item ORDER BY offer DESC LIMIT 8";
    $result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($connection));

    
	 $data = array();

    while($row =mysqli_fetch_assoc($result))
    {
        $data['data'][] = $row;
		
    }
    	
	 $data['status']['success']="true";
	//$emparray['status']['error'] ="";
	echo json_encode($data);


?>