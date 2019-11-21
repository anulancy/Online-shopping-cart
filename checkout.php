<?php


include("connection.php");

$uid = $_REQUEST['uid'];
$amount  = $_REQUEST['amount'];
$ptype = $_REQUEST['ptype'];


date_default_timezone_set('Asia/Kolkata');
$date = date('d-m-Y H:i:s');

$userDetailsResult = mysqli_query($con,"SELECT * FROM tbl_customer WHERE id='$uid'");
$uDRow = mysqli_fetch_assoc($userDetailsResult);

$sql1="INSERT INTO invoice (name, email, country, address, city, zipCode, phone_number, amount, payment_type, purchase_date) 
       VALUES ('$uDRow[cust_name]', '$uDRow[email]', 'IND', '$uDRow[Address]', '$uDRow[city]', '$uDRow[pincode]', '$uDRow[phn]', '$amount', '$ptype', '$date')";
mysqli_query($con, $sql1);

$da = date("Y-m-d");



$six_digit_random_number = mt_rand(100000, 999999);
$randNumResult = mysqli_query($con, "SELECT order_number FROM tbl_order_master ");

while($randNumRow = mysqli_fetch_assoc($randNumResult))
{
	$randumNo = $randNumRow['order_number'];
	if($six_digit_random_number == $randumNo)
	{
		$six_digit_random_number = mt_rand(100000, 999999);
	}
}


$query = "SELECT * FROM tbl_order_master WHERE cust_id='$uid' and status='cart'";
$Omid_result = mysqli_query($con,$query);

$OmidRow = mysqli_fetch_assoc($Omid_result);

$om_id = $OmidRow['id'];

mysqli_query($con, "UPDATE tbl_order_master SET status='PURCHASED',o_date='$da',order_number='$six_digit_random_number' WHERE cust_id='$uid' and id='$om_id'"); 

    $omid = "SELECT * FROM tbl_order_master where cust_id='$uid'";
    $omresult = mysqli_query($con,$omid);
    $omrow = mysqli_fetch_array($omresult);
    $ocselectc = "SELECT * FROM tbl_order_child where om_id='$omrow[id]'";

    $ocresult = mysqli_query($con,$ocselectc);
    while($ocrow = mysqli_fetch_array($ocresult))
    {
        $itemselect = "SELECT * FROM tbl_item where id='$ocrow[item_id]'";
        $itemresult = mysqli_query($con, $itemselect);
        $itemrow = mysqli_fetch_array($itemresult);
        $old = $itemrow['qty']; 
        $new = $old - $ocrow['o_qty'];

        $updatestock = "UPDATE tbl_item SET qty = $new WHERE id = '$ocrow[item_id]' ";
        $updateresult = mysqli_query($con, $updatestock);
    }

    echo $six_digit_random_number;



?>