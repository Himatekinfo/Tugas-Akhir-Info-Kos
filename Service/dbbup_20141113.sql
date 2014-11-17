/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.6.12-log : Database - db_inforumahsewa
*********************************************************************
*/
/*Table structure for table `tbl_rating` */

DROP TABLE IF EXISTS `tbl_rating`;

CREATE TABLE `tbl_rating` (
  `id_rating` varchar(50) NOT NULL,
  `rate` tinyint(3) unsigned NOT NULL,
  `id_rumahsewa` varchar(50) NOT NULL,
  PRIMARY KEY (`id_rating`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `tbl_rating` */

/*Table structure for table `tbl_rumahsewa` */

DROP TABLE IF EXISTS `tbl_rumahsewa`;

CREATE TABLE `tbl_rumahsewa` (
  `id_rumahSewa` varchar(50) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `namapemilik` varchar(50) DEFAULT NULL,
  `alamat` varchar(500) NOT NULL,
  `no_telp` varchar(15) NOT NULL,
  `hargasewa` bigint(20) NOT NULL,
  `foto` varchar(500) NOT NULL,
  `fasilitas` varchar(500) NOT NULL,
  `deskripsi` varchar(500) NOT NULL,
  `created_date` varchar(20) NOT NULL,
  PRIMARY KEY (`id_rumahSewa`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `tbl_rumahsewa` */

insert  into `tbl_rumahsewa`(`id_rumahSewa`,`latitude`,`longitude`,`namapemilik`,`alamat`,`no_telp`,`hargasewa`,`foto`,`fasilitas`,`deskripsi`,`created_date`) values ('ce64c3c941096e281678828',-6.2197921,106.7415521,'dhd hd h','f jg hdd','566468',56846,'/svc_rumahsewa/uploads/20130919_184158.jpg','dhchshd','fjcdhc','678828'),('ce64c3c941096e282683145',-6.2197921,106.7415521,'dhxush','cjchs','86864',83498,'/svc_rumahsewa/uploads/20130715_153626.jpg','shchd','chdhd','683145');

/*Table structure for table `tbl_user` */

DROP TABLE IF EXISTS `tbl_user`;

CREATE TABLE `tbl_user` (
  `id_user` bigint(50) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(15) NOT NULL,
  `nama` varchar(40) NOT NULL,
  `password` char(40) NOT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `foto` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `tbl_user` */

insert  into `tbl_user`(`id_user`,`username`,`nama`,`password`,`alamat`,`foto`) values (1,'bluespy','Blue Spy','a16e52d13673760a99bfb218da7c8898f1c769cb',NULL,NULL);
