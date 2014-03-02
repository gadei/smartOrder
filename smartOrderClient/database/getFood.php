<?php
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/dbConnect.php';
 
// connecting to db
$db = new DB_CONNECT();
$con = $db->connect();
 
// get all products from products table
$result = $con->query("SELECT * FROM food") or die(mysql_error());
 
// check for empty result
if ($result->num_rows > 0) 
{
    // looping through all results
    // products node
    $response["food"] = array();
 
    while ($row = $result->fetch_array()) {
        // temp user array
        $food = array();
        $food["food_id"] = $row["food_id"];
        $food["food_name"] = $row["food_name"];
        $food["food_price"] = $row["food_price"];
        
        // push single product into final response array
        array_push($response["food"], $food);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No products found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>