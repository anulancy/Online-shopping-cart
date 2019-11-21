<?php

include("connection.php");

$itemID = $_REQUEST['ItemId'];

	$sql = "SELECT * FROM tbl_item WHERE id='$itemID'";
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