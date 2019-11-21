<?php

include("connection.php");

$data = array();

$query = "SELECT *  FROM tbl_category ";
$result = mysqli_query($con,$query);

While($row = mysqli_fetch_assoc($result))
{

 	$data['catagory'][$row['id']]  = $row;

	$query1 = "SELECT * FROM tbl_subcategory WHERE cat_id='$row[id]'";
	$result1 = mysqli_query($con,$query1);

	While($row1 = mysqli_fetch_assoc($result1))
	{
			 $data['catagory'][$row['id']]['subcat'][] = $row1;
			// $data['catagory'][] = $row;
	}

}

// echo json_encode($data);

		 $data['catagory'] = array_values( $data['catagory']);
	
	echo json_encode($data); 

?>