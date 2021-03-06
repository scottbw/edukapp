-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 29, 2012 at 01:04 PM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `edukapptests`
--

-- --------------------------------------------------------

--
-- Table structure for table `accountinfo`
--

CREATE TABLE IF NOT EXISTS `accountinfo` (
  `id` int(11) NOT NULL,
  `realname` varchar(35) DEFAULT NULL,
  `website` varchar(256) DEFAULT NULL,
  `joined` timestamp NULL DEFAULT NULL,
  `lastseen` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `shortbio` varchar(2056) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `activities`
--

CREATE TABLE IF NOT EXISTS `activities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activitytext` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2657 ;

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE IF NOT EXISTS `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commenttext` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3803 ;

-- --------------------------------------------------------

--
-- Table structure for table `ltiprovider`
--

CREATE TABLE IF NOT EXISTS `ltiprovider` (
  `id` int(11) NOT NULL,
  `consumerKey` varchar(255) DEFAULT NULL,
  `consumerSecret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_LTPRVDR_ID` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `openjpa_sequence_table`
--

CREATE TABLE IF NOT EXISTS `openjpa_sequence_table` (
  `ID` tinyint(4) NOT NULL,
  `SEQUENCE_VALUE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2606 ;

-- --------------------------------------------------------

--
-- Table structure for table `tags`
--

CREATE TABLE IF NOT EXISTS `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tagtext` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3855 ;

-- --------------------------------------------------------

--
-- Table structure for table `types`
--

CREATE TABLE IF NOT EXISTS `types` (
  `id` int(11) NOT NULL,
  `typetext` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_TYPES_ID` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `useraccount`
--

CREATE TABLE IF NOT EXISTS `useraccount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(256) NOT NULL,
  `salt` varchar(256) NOT NULL DEFAULT '01',
  `token` varchar(256),
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3552 ;

-- --------------------------------------------------------

--
-- Table structure for table `useraccount_roles`
--

CREATE TABLE IF NOT EXISTS `useraccount_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `useractivities`
--

CREATE TABLE IF NOT EXISTS `useractivities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) NOT NULL,
  `activity` varchar(25) NOT NULL,
  `object_id` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=321 ;

-- --------------------------------------------------------

--
-- Table structure for table `userratings`
--

CREATE TABLE IF NOT EXISTS `userratings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `rating` tinyint(4) NOT NULL,
  `widgetprofile_id` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `widgetprofile_id` (`widgetprofile_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3402 ;

-- --------------------------------------------------------

--
-- Table structure for table `userreview`
--

CREATE TABLE IF NOT EXISTS `userreview` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `comment_id` int(11) NOT NULL,
  `widgetprofile_id` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `comment_id` (`comment_id`),
  KEY `widgetprofile_id` (`widgetprofile_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3753 ;

-- --------------------------------------------------------

--
-- Table structure for table `widgetactivities`
--

CREATE TABLE IF NOT EXISTS `widgetactivities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `widgetprofile_id` int(11) NOT NULL,
  `activity_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `widgetprofile_id` (`widgetprofile_id`),
  KEY `activity_id` (`activity_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=80 ;

-- --------------------------------------------------------

--
-- Table structure for table `widgetprofiles`
--

CREATE TABLE IF NOT EXISTS `widgetprofiles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `wid_id` varchar(150) NOT NULL COMMENT 'either wookie_id or gadget url',
  `w3c_or_os` tinyint(1) NOT NULL,
  `featured` tinyint(1) DEFAULT '0',
  `DESCRIPTION_WID_ID` int(11) DEFAULT NULL,
  `icon` varchar(255),
  `created` date NOT NULL,
  `updated` date NOT NULL,
  `author` varchar(1024),
  `license` varchar(2048),
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2566 ;

-- --------------------------------------------------------

--
-- Table structure for table `widgetprofiles_tags`
--

CREATE TABLE IF NOT EXISTS `widgetprofiles_tags` (
  `widgetprofile_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`widgetprofile_id`,`tag_id`),
  KEY `widgetprofile_id` (`widgetprofile_id`),
  KEY `tag_id` (`tag_id`),
  KEY `widgetprofile_id_2` (`widgetprofile_id`),
  KEY `tag_id_2` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `widgetstats`
--

CREATE TABLE IF NOT EXISTS `widgetstats` (
  `wid_id` int(11) NOT NULL,
  `downloads` int(11) DEFAULT NULL,
  `embeds` int(11) DEFAULT NULL,
  `views` int(11) DEFAULT NULL,
  `averageRating` decimal(10,0) DEFAULT NULL,
  `totalRatings` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`wid_id`),
  KEY `wid_id` (`wid_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `widget_descriptions`
--

CREATE TABLE IF NOT EXISTS `widget_descriptions` (
  `wid_id` int(11) NOT NULL,
  `description` text,
  KEY `wid_id` (`wid_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Constraints for dumped tables
--

