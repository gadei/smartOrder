<?php
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/dbConnect.php';
 
// connecting to db
$db = new DB_CONNECT();
$con = $db->connect();
$TAG_OPEN = 1;

if (isset($_GET['order_table']))
{
	$order_table = $_GET['order_table'];
 
	// get all products from products table
	$result = $con->query("SELECT * FROM `order` WHERE order_status=$TAG_OPEN AND order_table=$order_table") or die(mysql_error());
 
	// check for empty result
	if ($result->num_rows > 0) 
	{
		// looping through all results
		// products node
		$response["order"] = array();
 
		while ($row = $result->fetch_array()) 
		{
			// temp user array
			$order = array();
			$order["order_id"] = $row["order_id"];
			$order["order_status"] = $row["order_status"];
			$order["order_table"] = $row["order_table"];
			$order["order_timestamp"] = $row["order_timestamp"];
			
			
			$result_menu = $con->query("SELECT food_id,drink_id FROM `orderitems` WHERE order_id=$row[order_id]");
			
			if($result_menu->num_rows > 0)
			{
				$order_food = array();
				$order_drink = array();
				while($row_menu = $result_menu->fetch_array())
				{
					if($row_menu["food_id"] != null)
					{
						array_push($order_food, $row_menu["food_id"]);
					}
					if($row_menu["drink_id"] != null)
					{
						array_push($order_drink, $row_menu["drink_id"]);
					}
				}
				$order["order_food"] = $order_food;
				$order["order_drink"] = $order_drink;
			}
			
        
			// push single product into final response array
			array_push($response["order"], $order);
		}

				// success
		$response["success"] = 1;
 
		// echoing JSON response
		echo json_encode($response);	
	}
	else 	
	{
		// no products found
		$response["success"] = 0;
		$response["message"] = "No products found";
 
		// echo no users JSON
		echo json_encode($response);
	}
}
else
{
	$response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echo no users JSON
    echo json_encode($response);
}

?>