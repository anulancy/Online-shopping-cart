<?php

include("connection.php");

$name=$_POST['name'];
$email=$_POST['email'];
$phone=$_POST['phone'];
$uname=$_POST['uname'];
$password=$_POST['password'];

/*INSERT INTO tbl_customer(cust_name, phn, email, password) VALUES ([value-1],[value-2],[value-3],[value-4],[value-5],[value-6],[value-7],[value-8],[value-9],[value-10],[value-11],[value-12],[value-13],[value-14])*/

$res=mysqli_query($con,"select * from tbl_customer where email='$email' or username='$uname'");
	if(mysqli_num_rows($res)>0)
	{		
		echo "Exist";
	}
	else{
		$res1=mysqli_query($con,"INSERT INTO tbl_customer(cust_name, phn, email, username, password) values('$name','$phone','$email','$uname','$password')" );
		echo "Inserted";
	}
	
mysqli_close($con);

?>