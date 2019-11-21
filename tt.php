<?php
header('Content-Type: application/json');
$response = array(
    'id' => null,
);

if (array_key_exists('product', $_POST)) {
    $product = $_POST['product'];

    // Now we fetch the product info from the database.
    // SELECT ID FROM product_info WHERE product = $product
    // Imagine the result is stored in $result
    $result = mysqli_query($conn,"select * from tbl_item_cat where tbl_item_sub_cat='$product'");

    $response['id'] = $result['id'];
}

echo json_encode($response);

?>