<?php
$response = array();

// check for required fields
if (isset($_GET['table_nr']) && ($_GET['status'])) 
{
    $table_nr = $_GET['table_nr'];
    $status = $_GET['status'];
    $food = $_GET['food'];
    $drink = $_GET['drink'];
	$price_sum = $_GET['price_sum'];

    // include db connect class
    require_once __DIR__ . '/dbConnect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysql_query("INSERT INTO `order`(table_nr, food, drink, status, price_sum) VALUES('$table_nr', '$food', '$drink', '$status', '$price_sum')");

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