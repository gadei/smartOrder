<?php
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/dbConnect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all products from products table
$result = mysql_query("SELECT * FROM food") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["food"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $food = array();
        $food["food_id"] = $row["food_id"];
        $food["name"] = $row["name"];
        $food["price"] = $row["price"];
        
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