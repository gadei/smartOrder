<?php
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/dbConnect.php';
 
// connecting to db
$db = new DB_CONNECT();
$TAG_OPEN = 1;

if (isset($_GET['table_nr']))
{
	$table_nr = $_GET['table_nr'];
 
	// get all products from products table
	$result = mysql_query("SELECT * FROM `order` WHERE status=$TAG_OPEN AND table_nr=$table_nr") or die(mysql_error());
 
	// check for empty result
	if (mysql_num_rows($result) > 0) 
	{
		// looping through all results
		// products node
		$response["order"] = array();
 
		while ($row = mysql_fetch_array($result)) 
		{
			// temp user array
			$order = array();
			$order["order_id"] = $row["order_id"];
			$order["status"] = $row["status"];
			$order["table_nr"] = $row["table_nr"];
			$order["food"] = $row["food"];
			$order["drink"] = $row["drink"];
			$order["timestamp"] = $row["timestamp"];
        
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