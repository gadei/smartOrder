-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 02, 2014 at 10:47 AM
-- Server version: 5.6.12-log
-- PHP Version: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `smartorder`
--
CREATE DATABASE IF NOT EXISTS `smartorder` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `smartorder`;

-- --------------------------------------------------------

--
-- Table structure for table `drink`
--

CREATE TABLE IF NOT EXISTS `drink` (
  `drink_id` int(11) NOT NULL,
  `drink_name` varchar(100) NOT NULL,
  `drink_price` double NOT NULL,
  PRIMARY KEY (`drink_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `drink`
--

INSERT INTO `drink` (`drink_id`, `drink_name`, `drink_price`) VALUES
(1, 'Bier 0.5l', 3.6),
(2, 'Spritzer', 2.65),
(3, 'Coca Cola 0.5l', 3.2),
(4, 'Coca Cola 0.3l', 2.8),
(5, 'Bier 0.3l', 2.9),
(6, 'Apfelsaft 0.3l', 2.1);

-- --------------------------------------------------------

--
-- Table structure for table `food`
--

CREATE TABLE IF NOT EXISTS `food` (
  `food_id` int(11) NOT NULL,
  `food_name` varchar(100) NOT NULL,
  `food_price` double NOT NULL,
  PRIMARY KEY (`food_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `food`
--

INSERT INTO `food` (`food_id`, `food_name`, `food_price`) VALUES
(1, 'Toast', 3.9),
(2, 'Wiener Schnitzel', 8.9),
(3, 'Schweinsbraten', 9.1),
(4, 'Frankfurter mit Semmel', 3.8),
(5, 'Pizza Margaritha', 7.6),
(6, 'Pizza Calzone', 9.1);

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE IF NOT EXISTS `order` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_table` int(11) NOT NULL,
  `order_status` int(11) NOT NULL,
  `order_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_price` double DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=84 ;

-- --------------------------------------------------------

--
-- Table structure for table `orderitems`
--

CREATE TABLE IF NOT EXISTS `orderitems` (
  `order_id` int(11) NOT NULL,
  `food_id` int(11) DEFAULT NULL,
  `drink_id` int(11) DEFAULT NULL,
  `orderitems_payed` tinyint(1) NOT NULL,
  `orderitems_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`orderitems_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=76 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
