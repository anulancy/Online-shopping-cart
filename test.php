<?php

$conn = new mysqli('localhost', 'root', '', 'grocerystore')
or die ('Cannot connect to db');

$result = mysqli_query($conn,"select * from tbl_item_cat");

 //after submitting the form you get the value of selected product
if(isset($_GET['id'])){
   $product_id=$_GET['ID'];
   $productQuery = mysqli_query($conn,"select * from tbl_item_cat WHERE ID='".$product_id."'");
  $productResult = $productQuery ->fetch_assoc();
  //use the value of productResult in your textbooxes
}else{ 
   $productResult =array();
}
?>
<form method='get' action=''>
  <select name='ID' onchange="this.form.submit()">
 <?php
     while ($row = $result->fetch_assoc()) {
        unset($id, $name);
        $id = $row['id'];
        $name = $row['catagory_name']; 
        echo '<option value="'.$id.'">'.$name.'</option>';
  }?>
 </select>
</form>