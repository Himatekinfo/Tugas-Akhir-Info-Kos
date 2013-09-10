/*
SQLyog Ultimate v9.02 
MySQL - 5.5.32 : Database - db_inforumahsewa
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_inforumahsewa` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `db_inforumahsewa`;

/*Table structure for table `tbl_rating` */

DROP TABLE IF EXISTS `tbl_rating`;

CREATE TABLE `tbl_rating` (
  `id_rating` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rate` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_rating`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `tbl_rating` */

/*Table structure for table `tbl_rumahsewa` */

DROP TABLE IF EXISTS `tbl_rumahsewa`;

CREATE TABLE `tbl_rumahsewa` (
  `id_rumahSewa` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `latitude` decimal(10,0) DEFAULT NULL,
  `longitude` decimal(10,0) DEFAULT NULL,
  `namapemilik` varchar(30) DEFAULT NULL,
  `alamat` char(255) DEFAULT NULL,
  `no_telp` int(12) DEFAULT NULL,
  `hargasewa` int(10) DEFAULT NULL,
  `foto` tinyint(4) DEFAULT NULL,
  `fasilitas` varchar(255) DEFAULT NULL,
  `deskripsi` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_rumahSewa`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `tbl_rumahsewa` */

/*Table structure for table `tbl_user` */

DROP TABLE IF EXISTS `tbl_user`;

CREATE TABLE `tbl_user` (
  `id_user` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(15) NOT NULL,
  `nama` varchar(40) NOT NULL,
  `alamat` char(255) DEFAULT NULL,
  `foto` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `tbl_user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
