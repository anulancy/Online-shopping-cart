<?php


$fid = $_POST['fid'];
$image = $_POST['image'];

$filename = "userimage";
$path = "admin/tbl_customer/uploads/$filename";

file_put_contents($path.".jpeg",base64_decode($image));
echo "Successfully Uploaded";

?>