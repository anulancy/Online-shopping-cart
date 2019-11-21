<?php

include("connection.php");

$username=$_POST['username'];
$password=$_POST['password'];


$res=mysqli_query($con,"SELECT * from tbl_customer WHERE username='$username' && password='$password'");

	if(mysqli_num_rows($res)>0){

		while($row = mysqli_fetch_array($res))
		{
			echo $row['id'];
		}
		
	
	}	
	else{
		echo "Unsucessfull";
	}



mysqli_close($con);

?>