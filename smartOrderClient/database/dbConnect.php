<?php
class DB_CONNECT {
 
	private $con;
 
    // constructor
    function __construct() {
        $this->connect();
    }
 
    // destructor
    function __destruct() {
        $this->close();
    }
 
    /**
     * Function to connect with database
     */
    function connect() {
        // import database connection variables
        require_once __DIR__ . '/dbConfig.php';
 
        // Connecting to mysql database
        $this->con = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die(mysql_error());
 
		if ($this->con->connect_error) 
		{
			die('Connect Error (' . $this->con->connect_errno . ') '. $this->con->connect_error);
		}
 
        // returing connection cursor
        return $this->con;
    }
 
    /**
     * Function to close db connection
     */
    function close() 
	{
        $this->con->close();
    }
 
}
 
?>