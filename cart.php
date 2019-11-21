<?php

include("connection.php");


$query= "SELECT * FROM tbl_order_master WHERE cust_id='$_REQUEST[uid]' and status='cart'"; 
$result=mysqli_query($con,$query);
$r = mysqli_fetch_assoc($result);

$data = array();


	$sql = "SELECT * FROM tbl_order_child WHERE om_id='$r[id]'";
	$om_id = $r['id'];
 	$result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($connection));

 	if(mysqli_num_rows($result) > 0 )
 	{
	
		 while($row =mysqli_fetch_assoc($result))
   		 {
				 $data['data'][$row['id']]  = $row;
				//$resultArr['data']= array('id'= $row['id'], 'group_name' = $row['group_name']);

			//Anwser table results
			$sql2 = "SELECT * FROM tbl_item WHERE id='$row[item_id]'";
			// echo $sql2;
 			$result2 = mysqli_query($con, $sql2) or die("Error in Selecting " . mysqli_error($connection));
			while($row2 = mysqli_fetch_assoc($result2)) {
				 $data['data'][$row['id']]['item'][] = $row2;
			}
		}

	  $query5 = "select SUM(o_qty*price) as Total,COUNT(id) as count FROM tbl_order_child WHERE om_id='$r[id]'";
      $result5=mysqli_query($con,$query5);
	  $row5=mysqli_fetch_assoc($result5);

	   $data['AMOUNT'][] = $row5;
	   $data['Cart'][] = array( 'cartid' => $om_id);

		 $data['data'] = array_values( $data['data']);
	
	echo json_encode($data);   

}

?>