<?php
$response = array();

// check for required fields
if (isset($_POST['order_id']) && isset($_POST['table_nr']) && isset($_POST['status'])) 
{
	$order_id = $_POST['order_id'];
    $table = $_POST['table_nr'];
    $status = $_POST['status'];
    $food = $_POST['food'];
    $drink = $_POST['drink'];

    // include db connect class
    require_once __DIR__ . '/dbConnect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysql_query("INSERT INTO `order`(order_id, table_nr, food, drink, status) VALUES('$order_id', '$table_nr', '$food', '$drink', '$status')");

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred. $table, $order_id, $food, $drink, $status";

        // echoing JSON response
        echo json_encode($response);
    }
} 
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>