<?php
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/dbConnect.php';
 
// connecting to db
$db = new DB_CONNECT();
$con = $db->connect();
 
// get all products from products table
$result = $con->query("SELECT * FROM drink") or die(mysql_error());

// check for empty result
if (($result->num_rows) > 0) {
    // looping through all results
    // products node
    $response["drink"] = array();
 
    while ($row = $result->fetch_array()) 
	{
        // temp user array
        $drink = array();
        $drink["drink_id"] = $row["drink_id"];
        $drink["drink_name"] = $row["drink_name"];
        $drink["drink_price"] = $row["drink_price"];
        
        // push single product into final response array
        array_push($response["drink"], $drink);
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