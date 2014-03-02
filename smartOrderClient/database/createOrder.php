<?php
$response = array();

// check for required fields
if (isset($_GET['order_table']) && ($_GET['order_status'])) 
{
    $order_table = $_GET['order_table'];
    $order_status = $_GET['order_status'];
    $order_food = $_GET['order_food'];
    $order_drink = $_GET['order_drink'];
	$order_price = $_GET['order_price'];

    // include db connect class
    require_once __DIR__ . '/dbConnect.php';

    // connecting to db
    $db = new DB_CONNECT();
	$con = $db->connect();

    // mysql inserting a new row
    $result = $con->query("INSERT INTO `order`(order_table, order_status, order_price) VALUES('$order_table', '$order_status', '$order_price')");

    // check if row inserted or not
    if (!$result) 
	{
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

        // echoing JSON response
        echo json_encode($response);
		return;
    }
	
	$order_id = $con->insert_id;
	
	if(strlen($order_food) > 0)
	{
		$list_food = explode(",", $order_food);
	
		for($i = 0; $i < count($list_food); $i++)
		{
			if(strlen($list_food[$i]) > 0)
			{
				$result = $con->query("INSERT INTO `orderitems`(order_id, food_id) VALUES('$order_id', '$list_food[$i]')");
			}
		}
	}
	
	
	if(strlen($order_drink) > 0)
	{
		$list_drink = explode(",", $order_drink);
	
		for($i = 0; $i < count($list_drink); $i++)
		{
			if(strlen($list_drink[$i]) > 0)
			{
				$result = $con->query("INSERT INTO `orderitems`(order_id, drink_id) VALUES('$order_id', '$list_drink[$i]')");
			}
		}
	}
	
	
	if($result)
	{
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

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
		return;
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