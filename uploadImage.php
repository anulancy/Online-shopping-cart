<?php

include("connection.php");

if(isset($_POST['image']))
{
	$image = $_POST['image'];
	$uid = $_POST['uid'];
	$fname = $_POST['fname'];
	$lname = $_POST['lname'];
	$email = $_POST['email'];

	date_default_timezone_set('Asia/Kolkata');
	$date = date('s');
	 
	$filename = "userimage"."$date"."_$uid.jpg";
	$path = "admin/tbl_customer/uploads/$filename";


		$sql = "UPDATE tbl_customer SET cust_name='$fname', cust_lname='$lname', email='$email', cust_image='$filename' WHERE id='$uid'";
		
		if(mysqli_query($con,$sql)){
		 	 file_put_contents($path,base64_decode($image));
			 echo "Successfully Uploaded";
		}

}
else{

	$uid = $_POST['uid'];
	$fname = $_POST['fname'];
	$lname = $_POST['lname'];
	$email = $_POST['email'];


		$sql1 = "UPDATE tbl_customer SET cust_name='$fname', cust_lname='$lname', email='$email' WHERE id='$uid'";
		
		if(mysqli_query($con,$sql1))
			echo "Successfully Updated";

}

?>