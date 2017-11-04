-- phpMyAdmin SQL Dump
-- version 4.4.14
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 2016-11-26 05:00:45
-- 服务器版本： 5.6.26
-- PHP Version: 5.6.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `musixise`
--

-- --------------------------------------------------------

--
-- 表的结构 `audience`
--

CREATE TABLE IF NOT EXISTS `audience` (
  `id` bigint(20) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `tel` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- 表的结构 `DATABASECHANGELOG`
--

CREATE TABLE IF NOT EXISTS `DATABASECHANGELOG` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `DATABASECHANGELOG`
--

INSERT INTO `DATABASECHANGELOG` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`) VALUES
('00000000000001', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', '2016-09-16 10:26:33', 1, 'EXECUTED', '7:d5c631098d677dbf91668d1efad18dc7', 'createTable, createIndex (x2), createTable (x2), addPrimaryKey, addForeignKeyConstraint (x2), loadData, dropDefaultValue, loadData (x2), createTable (x2), addPrimaryKey, createIndex (x2), addForeignKeyConstraint, createTable, addUniqueConstraint (x2)', '', NULL, '3.4.2', NULL, NULL),
('20160508010035', 'jhipster', 'config/liquibase/changelog/20160508010035_added_entity_Musixiser.xml', '2016-09-16 10:26:33', 2, 'EXECUTED', '7:5f5b4fb6ac3e2ddbbaabf30ff2214948', 'createTable', '', NULL, '3.4.2', NULL, NULL),
('20160508023025', 'jhipster', 'config/liquibase/changelog/20160508023025_added_entity_Stages.xml', '2016-09-16 10:26:34', 3, 'EXECUTED', '7:ebbc7596a0d1fa8d98bd810c50aafbe2', 'createTable', '', NULL, '3.4.2', NULL, NULL),
('20160508024110', 'jhipster', 'config/liquibase/changelog/20160508024110_added_entity_WorkList.xml', '2016-09-16 10:26:34', 4, 'EXECUTED', '7:d94c57c4e1a82937c4643ff196ddc6d6', 'createTable', '', NULL, '3.4.2', NULL, NULL),
('20160508032832', 'jhipster', 'config/liquibase/changelog/20160508032832_added_entity_WorkListFollow.xml', '2016-09-16 10:26:35', 5, 'EXECUTED', '7:92726956d86434984bfe14f5ae480462', 'createTable, addForeignKeyConstraint (x2)', '', NULL, '3.4.2', NULL, NULL),
('20160515081001', 'jhipster', 'config/liquibase/changelog/20160515081001_added_entity_Audience.xml', '2016-09-16 10:26:35', 6, 'EXECUTED', '7:ca3c62a4a781d605f5cdac8991154ea3', 'createTable', '', NULL, '3.4.2', NULL, NULL),
('20160515111152', 'jhipster', 'config/liquibase/changelog/20160515111152_added_entity_StagesFollow.xml', '2016-09-16 10:26:36', 7, 'EXECUTED', '7:aa89e0c84fb1cf243793fd3a4a86d987', 'createTable, dropDefaultValue (x2)', '', NULL, '3.4.2', NULL, NULL),
('00000000000009', 'zhaowei', 'config/liquibase/changelog/change_table_struck_09162016.xml', '2016-11-12 15:19:14', 8, 'EXECUTED', '7:a057e3a39671ea0ac70e67fa77a12d81', 'sql (x3)', '', NULL, '3.4.2', NULL, NULL),
('20161113133906', 'jhipster', 'classpath:config/liquibase/changelog/20161113133906_added_entity_MusixiserFollow.xml', '2016-11-13 22:15:28', 9, 'EXECUTED', '7:48953bc38b41a98ab403511cadb2024d', 'createTable', '', NULL, '3.4.2', NULL, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `DATABASECHANGELOGLOCK`
--

CREATE TABLE IF NOT EXISTS `DATABASECHANGELOGLOCK` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `DATABASECHANGELOGLOCK`
--

INSERT INTO `DATABASECHANGELOGLOCK` (`ID`, `LOCKED`, `LOCKGRANTED`, `LOCKEDBY`) VALUES
(1, b'1', '2016-11-13 22:49:16', '192.168.0.106 (192.168.0.106)');

-- --------------------------------------------------------

--
-- 表的结构 `jhi_authority`
--

CREATE TABLE IF NOT EXISTS `jhi_authority` (
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `jhi_authority`
--

INSERT INTO `jhi_authority` (`name`) VALUES
('ROLE_ADMIN'),
('ROLE_USER');

-- --------------------------------------------------------

--
-- 表的结构 `jhi_persistent_audit_event`
--

CREATE TABLE IF NOT EXISTS `jhi_persistent_audit_event` (
  `event_id` bigint(20) NOT NULL,
  `principal` varchar(255) NOT NULL,
  `event_date` timestamp NULL DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `jhi_persistent_audit_event`
--

INSERT INTO `jhi_persistent_audit_event` (`event_id`, `principal`, `event_date`, `event_type`) VALUES
(1, 'admin', '2016-11-19 08:01:51', 'AUTHENTICATION_SUCCESS');

-- --------------------------------------------------------

--
-- 表的结构 `jhi_persistent_audit_evt_data`
--

CREATE TABLE IF NOT EXISTS `jhi_persistent_audit_evt_data` (
  `event_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- 表的结构 `jhi_social_user_connection`
--

CREATE TABLE IF NOT EXISTS `jhi_social_user_connection` (
  `id` bigint(20) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `provider_id` varchar(255) NOT NULL,
  `provider_user_id` varchar(255) NOT NULL,
  `rank` bigint(20) NOT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `profile_url` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `access_token` varchar(255) NOT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `expire_time` bigint(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `jhi_social_user_connection`
--

INSERT INTO `jhi_social_user_connection` (`id`, `user_id`, `provider_id`, `provider_user_id`, `rank`, `display_name`, `profile_url`, `image_url`, `access_token`, `secret`, `refresh_token`, `expire_time`) VALUES
(1, '1447503750', 'weibo', '1447503750', 1, '木木_______', 'http://www.zhaowei.me', 'http://tva3.sinaimg.cn/crop.0.0.180.180.180/56472786jw1e8qgp5bmzyj2050050aa8.jpg', '2.00gqZxZBxvYuhB869e729edblnlryD', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `jhi_user`
--

CREATE TABLE IF NOT EXISTS `jhi_user` (
  `id` bigint(20) NOT NULL,
  `login` varchar(100) NOT NULL,
  `password_hash` varchar(60) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(5) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NOT NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `jhi_user`
--

INSERT INTO `jhi_user` (`id`, `login`, `password_hash`, `first_name`, `last_name`, `email`, `activated`, `lang_key`, `activation_key`, `reset_key`, `created_by`, `created_date`, `reset_date`, `last_modified_by`, `last_modified_date`) VALUES
(1, 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'System', 'System', 'system@localhost', b'1', 'en', NULL, NULL, 'system', '2016-09-16 02:26:30', NULL, NULL, NULL),
(2, 'anonymousUser', '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO', 'Anonymous', 'User', 'anonymous@localhost', b'1', 'en', NULL, NULL, 'system', '2016-09-16 02:26:30', NULL, NULL, NULL),
(3, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator', 'admin@localhost', b'1', 'en', NULL, NULL, 'system', '2016-09-16 02:26:30', NULL, NULL, NULL),
(4, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User', 'User', 'user@localhost', b'1', 'en', NULL, NULL, 'system', '2016-09-16 02:26:30', NULL, NULL, NULL),
(5, 'string', '$2a$10$dwFJytMPNEcubGqoetWihO1K0MK6od47O7cEGN2hnMsGFBqsLd4..', NULL, NULL, 'string@sina.com', b'1', 'zh-cn', NULL, NULL, 'admin', '2016-11-19 12:53:28', NULL, 'admin', '2016-11-19 12:53:28'),
(6, '1447503750', '$2a$10$1SWXGZh0LY43vEVw18bx8uhDSitoBROW6ihp18wcKpsbLSRVEoImK', '木木_______', NULL, '1447503750@musixise.com', b'1', 'en', NULL, NULL, 'admin', '2016-11-20 05:52:47', NULL, 'admin', '2016-11-20 05:52:47');

-- --------------------------------------------------------

--
-- 表的结构 `jhi_user_authority`
--

CREATE TABLE IF NOT EXISTS `jhi_user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `jhi_user_authority`
--

INSERT INTO `jhi_user_authority` (`user_id`, `authority_name`) VALUES
(1, 'ROLE_ADMIN'),
(3, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(3, 'ROLE_USER'),
(4, 'ROLE_USER'),
(5, 'ROLE_USER'),
(6, 'ROLE_USER');

-- --------------------------------------------------------

--
-- 表的结构 `musixiser`
--

CREATE TABLE IF NOT EXISTS `musixiser` (
  `id` bigint(20) NOT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `tel` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `birth` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `small_avatar` varchar(255) DEFAULT NULL,
  `large_avatar` varchar(255) DEFAULT NULL,
  `nation` varchar(255) DEFAULT NULL,
  `is_master` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `musixiser`
--

INSERT INTO `musixiser` (`id`, `realname`, `tel`, `email`, `birth`, `gender`, `small_avatar`, `large_avatar`, `nation`, `is_master`, `user_id`) VALUES
(1, '222', '22', '22', NULL, NULL, NULL, NULL, NULL, NULL, 3),
(2, 'string', 'string', 'string@sina.com', 'string', 'string', 'string', 'string', 'string', NULL, 5),
(4, '木木_______', NULL, NULL, NULL, NULL, 'http://tva3.sinaimg.cn/crop.0.0.180.180.50/56472786jw1e8qgp5bmzyj2050050aa8.jpg', 'http://tva3.sinaimg.cn/crop.0.0.180.180.50/56472786jw1e8qgp5bmzyj2050050aa8.jpg', NULL, NULL, 6);

-- --------------------------------------------------------

--
-- 表的结构 `musixiser_follow`
--

CREATE TABLE IF NOT EXISTS `musixiser_follow` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  `follow_uid` bigint(20) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `musixiser_follow`
--

INSERT INTO `musixiser_follow` (`id`, `user_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `follow_uid`) VALUES
(2, 3, 'admin', '2016-11-13 14:25:45', 'admin', '2016-11-13 14:25:45', 4),
(3, 3, '', NULL, NULL, '0000-00-00 00:00:00', 2),
(4, 3, 'admin', '2016-11-14 15:43:05', 'admin', '2016-11-14 15:43:05', 1);

-- --------------------------------------------------------

--
-- 表的结构 `stages`
--

CREATE TABLE IF NOT EXISTS `stages` (
  `id` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `createtime` date NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `audience_num` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- 表的结构 `stages_follow`
--

CREATE TABLE IF NOT EXISTS `stages_follow` (
  `id` bigint(20) NOT NULL,
  `musixiser_uid` bigint(20) DEFAULT NULL,
  `audience_uid` bigint(20) DEFAULT NULL,
  `stages_id` bigint(20) DEFAULT NULL,
  `timestamp` timestamp NULL,
  `updatetime` timestamp NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- 表的结构 `work_list`
--

CREATE TABLE IF NOT EXISTS `work_list` (
  `id` bigint(20) NOT NULL,
  `content` longtext NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `status` tinyint(3) unsigned DEFAULT '0' COMMENT '0=正常，1=私有，2=删除',
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `work_list`
--

INSERT INTO `work_list` (`id`, `content`, `url`, `user_id`, `status`, `created_by`, `created_date`, `reset_date`, `last_modified_by`, `last_modified_date`) VALUES
(1, '11111', '111', 3, 1, '', '2016-11-13 13:05:42', NULL, NULL, NULL),
(2, '33', '33', 3, 0, '', '2016-11-13 06:21:39', NULL, NULL, NULL),
(3, 'ww', 'w', 3, 0, '', '2016-11-13 06:21:39', NULL, NULL, NULL),
(5, 'string', NULL, 3, NULL, 'admin', '2016-11-13 07:24:49', NULL, 'admin', '2016-11-13 07:24:49'),
(6, 'strieeng', NULL, 3, 0, 'admin', '2016-11-13 07:31:39', NULL, 'admin', '2016-11-13 07:31:39'),
(7, 'strieeng', NULL, 3, 0, 'admin', '2016-11-13 07:45:47', NULL, 'admin', '2016-11-13 07:45:47'),
(8, 'strieeng', NULL, 3, 0, 'admin', '2016-11-13 07:56:26', NULL, 'admin', '2016-11-13 07:56:26');

-- --------------------------------------------------------

--
-- 表的结构 `work_list_follow`
--

CREATE TABLE IF NOT EXISTS `work_list_follow` (
  `id` bigint(20) NOT NULL,
  `work_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `work_list_follow`
--

INSERT INTO `work_list_follow` (`id`, `work_id`, `user_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`) VALUES
(2, 2, 3, '', NULL, NULL, NULL),
(4, 3, 3, '', NULL, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `audience`
--
ALTER TABLE `audience`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `DATABASECHANGELOGLOCK`
--
ALTER TABLE `DATABASECHANGELOGLOCK`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `jhi_authority`
--
ALTER TABLE `jhi_authority`
  ADD PRIMARY KEY (`name`);

--
-- Indexes for table `jhi_persistent_audit_event`
--
ALTER TABLE `jhi_persistent_audit_event`
  ADD PRIMARY KEY (`event_id`),
  ADD KEY `idx_persistent_audit_event` (`principal`,`event_date`);

--
-- Indexes for table `jhi_persistent_audit_evt_data`
--
ALTER TABLE `jhi_persistent_audit_evt_data`
  ADD PRIMARY KEY (`event_id`,`name`),
  ADD KEY `idx_persistent_audit_evt_data` (`event_id`);

--
-- Indexes for table `jhi_social_user_connection`
--
ALTER TABLE `jhi_social_user_connection`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`provider_id`,`provider_user_id`),
  ADD UNIQUE KEY `user_id_2` (`user_id`,`provider_id`,`rank`);

--
-- Indexes for table `jhi_user`
--
ALTER TABLE `jhi_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `login` (`login`),
  ADD UNIQUE KEY `idx_user_login` (`login`),
  ADD UNIQUE KEY `idx_user_email` (`email`);

--
-- Indexes for table `jhi_user_authority`
--
ALTER TABLE `jhi_user_authority`
  ADD PRIMARY KEY (`user_id`,`authority_name`),
  ADD KEY `fk_authority_name` (`authority_name`);

--
-- Indexes for table `musixiser`
--
ALTER TABLE `musixiser`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `musixiser_follow`
--
ALTER TABLE `musixiser_follow`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_user_id` (`user_id`,`follow_uid`) USING BTREE,
  ADD KEY `follow_uid` (`follow_uid`);

--
-- Indexes for table `stages`
--
ALTER TABLE `stages`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `stages_follow`
--
ALTER TABLE `stages_follow`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `work_list`
--
ALTER TABLE `work_list`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `work_list_follow`
--
ALTER TABLE `work_list_follow`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`work_id`) USING BTREE,
  ADD KEY `fk_worklistfollow_work_id` (`work_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `audience`
--
ALTER TABLE `audience`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `jhi_persistent_audit_event`
--
ALTER TABLE `jhi_persistent_audit_event`
  MODIFY `event_id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `jhi_social_user_connection`
--
ALTER TABLE `jhi_social_user_connection`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `jhi_user`
--
ALTER TABLE `jhi_user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `musixiser`
--
ALTER TABLE `musixiser`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `musixiser_follow`
--
ALTER TABLE `musixiser_follow`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `stages`
--
ALTER TABLE `stages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `stages_follow`
--
ALTER TABLE `stages_follow`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `work_list`
--
ALTER TABLE `work_list`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `work_list_follow`
--
ALTER TABLE `work_list_follow`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- 限制导出的表
--

--
-- 限制表 `jhi_persistent_audit_evt_data`
--
ALTER TABLE `jhi_persistent_audit_evt_data`
  ADD CONSTRAINT `fk_evt_pers_audit_evt_data` FOREIGN KEY (`event_id`) REFERENCES `jhi_persistent_audit_event` (`event_id`);

--
-- 限制表 `jhi_user_authority`
--
ALTER TABLE `jhi_user_authority`
  ADD CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`);

--
-- 限制表 `musixiser_follow`
--
ALTER TABLE `musixiser_follow`
  ADD CONSTRAINT `musixiser_follow_ibfk_1` FOREIGN KEY (`follow_uid`) REFERENCES `jhi_user` (`id`);

--
-- 限制表 `work_list_follow`
--
ALTER TABLE `work_list_follow`
  ADD CONSTRAINT `fk_worklistfollow_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`),
  ADD CONSTRAINT `fk_worklistfollow_work_id` FOREIGN KEY (`work_id`) REFERENCES `work_list` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


ALTER TABLE `mu_user_bind` ADD `access_token` VARCHAR(255) NOT NULL DEFAULT '''''' AFTER `provider`, ADD `expires_in` INT NOT NULL DEFAULT '0' AFTER `access_token`, ADD `refresh_token` VARCHAR(255) NOT NULL DEFAULT '''''' AFTER `expires_in`;
