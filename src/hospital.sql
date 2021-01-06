CREATE DATABASE  IF NOT EXISTS `hospital` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `hospital`;
-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: localhost    Database: hospital
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.39-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bed`
--

DROP TABLE IF EXISTS `bed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bed` (
  `id` int(11) NOT NULL,
  `nurse_id` int(10) DEFAULT NULL,
  `patient_id` int(18) DEFAULT NULL,
  `level` enum('1','2','3') DEFAULT '1',
  `bed_id` int(11) NOT NULL,
  `ward_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `nurse_id` (`nurse_id`),
  KEY `bed_ibfk_2` (`patient_id`),
  CONSTRAINT `bed_ibfk_1` FOREIGN KEY (`nurse_id`) REFERENCES `user` (`id`),
  CONSTRAINT `bed_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bed`
--

LOCK TABLES `bed` WRITE;
/*!40000 ALTER TABLE `bed` DISABLE KEYS */;
INSERT INTO `bed` VALUES (1,8,16,'1',1,201),(2,8,17,'1',2,201),(3,8,18,'1',3,201),(4,14,19,'1',4,201),(5,14,20,'1',1,202),(6,14,21,'1',2,202),(7,19,NULL,'1',3,202),(8,19,NULL,'1',4,202),(9,15,22,'2',1,301),(10,15,23,'2',2,301),(11,16,24,'2',1,302),(12,16,NULL,'2',2,302),(13,17,25,'3',1,401),(14,18,26,'3',1,402);
/*!40000 ALTER TABLE `bed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daily_status`
--

DROP TABLE IF EXISTS `daily_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `daily_status` (
  `patient_id` int(18) NOT NULL,
  `date` date NOT NULL,
  `temperature` float NOT NULL,
  `life_state` enum('0','1','2') DEFAULT '0',
  `NA_result` enum('0','1') DEFAULT '1',
  `symptom` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`patient_id`,`date`),
  CONSTRAINT `daily_status_f1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_status`
--

LOCK TABLES `daily_status` WRITE;
/*!40000 ALTER TABLE `daily_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `daily_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `na_sheet`
--

DROP TABLE IF EXISTS `na_sheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `na_sheet` (
  `patient_id` int(18) NOT NULL,
  `date` date NOT NULL,
  `NA_result` enum('0','1') DEFAULT '1',
  `illness_level` enum('1','2','3') DEFAULT '1',
  PRIMARY KEY (`patient_id`,`date`),
  CONSTRAINT `f1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `na_sheet`
--

LOCK TABLES `na_sheet` WRITE;
/*!40000 ALTER TABLE `na_sheet` DISABLE KEYS */;
/*!40000 ALTER TABLE `na_sheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notify`
--

DROP TABLE IF EXISTS `notify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notify` (
  `user_id` int(10) NOT NULL,
  `content` varchar(500) NOT NULL,
  `is_read` enum('0','1') DEFAULT '0',
  `date` date NOT NULL,
  PRIMARY KEY (`user_id`,`date`),
  CONSTRAINT `notify_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notify`
--

LOCK TABLES `notify` WRITE;
/*!40000 ALTER TABLE `notify` DISABLE KEYS */;
/*!40000 ALTER TABLE `notify` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `id` int(18) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `life_state` enum('0','1','2') DEFAULT '0',
  `area_level` enum('0','1','2','3','4') DEFAULT '0',
  `illness_level` enum('0','1','2','3') DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (16,'ptt1','1','1','1'),(17,'ptt2','1','1','1'),(18,'ptt3','1','1','1'),(19,'ptt4','1','1','1'),(20,'ptt5','1','1','1'),(21,'ptt6','1','1','1'),(22,'ptt7','1','2','2'),(23,'ptt8','1','2','2'),(24,'ptt9','1','2','2'),(25,'ptt10','1','3','3'),(26,'ptt11','1','3','3'),(27,'ptt12','1','0','3');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('0','1','2','3') DEFAULT '3',
  `area` enum('0','1','2','3') DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (6,'doctor1','$2a$10$vznhUblODHhPcv5cZ5tbKOF1E6Z60OoopPuWzIeFize1JqgokGFrS','0','1'),(7,'hnurse1','$2a$10$nXNmy8eGv3k1KZr2nPqj0uiubc/5CDMG.t2uK/lqLUUlM9TqpbVpG','1','1'),(8,'wnurse1','$2a$10$kufY7xzjfZxz62LK7fFxg.r9tm1Th26b6J0yXl8Xj2D3cdwQEuBLS','2','1'),(9,'enurse1','$2a$10$2qAwsMqfxSgX7j36SE9Rq.IB5LH70AlHSgi459AlxI8eQaOPjQgC6','3','0'),(10,'ftang','$2a$10$uUFxJ3SnErn43xvx3ghyzegZkhaRMTjJoFhgQqvcvXB26koTOV9Eq','0','2'),(11,'jinax','$2a$10$OPP4V8bmNBJaSSaTOHMV/uW687dEn8T2v2wt0QxRKbnXxyg74bR9.','0','3'),(12,'anna','$2a$10$LEl3CmWIUNFdV7ih29dX4e76RicJKQ5l/6GMA2GyOB6ZhTa9RMUyO','1','2'),(13,'david','$2a$10$WgJ/ZgkbIU3F1It0ZcLNsu.Kd2LWiCqyFqc3XEcMhBWFOoKy9ktJq','1','3'),(14,'rose','$2a$10$M5plHz/pTgrPzDOAI8ZGTueWUcyPR6oj3ogYOtS4ymF1S8C3rXeyW','2','1'),(15,'tina','$2a$10$MR2DayAou5W..RZL.AmBDO3wQAS52Q1K.AHsZeNMXk6P7c1g9YApK','2','2'),(16,'peter','$2a$10$9tlnWXLdqrPR3DQ7I.onmuNh3nDJxGb6.NYyHDp.aPIO2ehwZ.N6C','2','2'),(17,'alice','$2a$10$1iG8Nxgwda3unVbut/tSR.MvZPMx8WRzlhc6EMhT.yN8.lBaSu.Mi','2','3'),(18,'ilana','$2a$10$GTj.Cdb4sj48fnYdbz9mFOJb5Ks3vFHaQvNY9JdBR1665juQnzCB2','2','3'),(19,'kris','$2a$10$joNs36RfiHAUYznzYIe94eAP5WWip/cH1crLUcf1MzA9jynXIMMJa','2','1');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-01-03 20:17:48
