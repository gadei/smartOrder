<?php
$response = array();

// check for required fields
if (isset($_GET['order_id']))
{
    $order_id = $_GET['order_id'];
	$STATUS = 0;

    // include db connect class
    require_once __DIR__ . '/dbConnect.php';

    // connecting to db
    $db = new DB_CONNECT();
	$con = $db->connect();

    $result = $con->query("UPDATE `order` SET order_status=$STATUS WHERE order_id=$order_id");

    if ($result) 
	{
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";

        // echoing JSON response
        echo json_encode($response);
    } 
	else 
	{
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

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