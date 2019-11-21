<?php

include("connection.php");

    $query2="SELECT * FROM tbl_order_master WHERE cust_id='$_REQUEST[uid]' and status='cart' ";
    $result2=mysqli_query($con,$query2);
    $row2 = mysqli_fetch_array( $result2);
    $count=mysqli_num_rows($result2);
    	

        if($count==1)                 
        {
        	$query3="INSERT INTO tbl_order_child (om_id, item_id, o_qty, price) 
        			VALUES('$row2[id]', '$_REQUEST[itemID]', '$_POST[Qty]', '$_REQUEST[price]')";
        	mysqli_query($con,$query3);
        	echo $_REQUEST['price'];
        }
    	
        else
        {
        $query4="INSERT INTO tbl_order_master (cust_id,o_date,status) VALUES('$_REQUEST[uid]', now(), 'cart')";

            if($result4=mysqli_query($con,$query4))
            {
            	$oid=mysqli_insert_id($con);          // (item_id,om_id,order_qty,price) 
            	$query5="INSERT INTO tbl_order_child (om_id, item_id,o_qty,price) 
                    VALUES('$oid', '$_REQUEST[itemID]','$_POST[Qty]','$_REQUEST[price]')";
                $result5=mysqli_query($con,$query5);
                echo $_REQUEST['price'];
            }
            else
            {
            	echo "Error : ".mysqli_error($con);
            }

        }


?>