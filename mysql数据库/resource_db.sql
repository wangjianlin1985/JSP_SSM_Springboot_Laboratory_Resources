/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : resource_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-02-05 04:04:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_borrow`
-- ----------------------------
DROP TABLE IF EXISTS `t_borrow`;
CREATE TABLE `t_borrow` (
  `borrowId` int(11) NOT NULL auto_increment COMMENT '借阅id',
  `resourceObj` varchar(20) NOT NULL COMMENT '借阅的资源',
  `userObj` varchar(20) NOT NULL COMMENT '借阅用户',
  `borrowNum` int(11) NOT NULL COMMENT '借阅数量',
  `borrowTime` varchar(20) default NULL COMMENT '借阅时间',
  `returnTime` varchar(20) default NULL COMMENT '归还时间',
  `memo` varchar(2000) default NULL COMMENT '附加信息',
  PRIMARY KEY  (`borrowId`),
  KEY `resourceObj` (`resourceObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_borrow_ibfk_1` FOREIGN KEY (`resourceObj`) REFERENCES `t_resourceinfo` (`resourceNo`),
  CONSTRAINT `t_borrow_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_borrow
-- ----------------------------
INSERT INTO `t_borrow` VALUES ('1', 'ZY001', 'user1', '1', '2017-11-02 16:09:29', '2017-11-06 16:09:34', '小伙子厉害啊');
INSERT INTO `t_borrow` VALUES ('3', 'ZY001', 'user2', '2', '2017-11-03 23:31:17', '2017-11-07 23:31:37', 'test');
INSERT INTO `t_borrow` VALUES ('4', 'ZY002', 'user1', '1', '2017-11-01 23:32:06', '2017-11-09 23:32:20', 'test');
INSERT INTO `t_borrow` VALUES ('5', 'ZY003', 'user1', '2', '2017-11-28 00:28:57', '2017-11-29 00:30:06', 'test借阅测试');
INSERT INTO `t_borrow` VALUES ('6', 'ZY001', 'user1', '2', '2018-02-05 04:00:35', '--', 'test');

-- ----------------------------
-- Table structure for `t_receive`
-- ----------------------------
DROP TABLE IF EXISTS `t_receive`;
CREATE TABLE `t_receive` (
  `receiveId` int(11) NOT NULL auto_increment COMMENT '领用id',
  `resourceObj` varchar(20) NOT NULL COMMENT '领用的资源',
  `userObj` varchar(20) NOT NULL COMMENT '领用用户',
  `receiveNum` int(11) NOT NULL COMMENT '领用数量',
  `receiveTime` varchar(20) default NULL COMMENT '领用时间',
  `purpose` varchar(2000) default NULL COMMENT '领用用途',
  PRIMARY KEY  (`receiveId`),
  KEY `resourceObj` (`resourceObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_receive_ibfk_1` FOREIGN KEY (`resourceObj`) REFERENCES `t_resourceinfo` (`resourceNo`),
  CONSTRAINT `t_receive_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_receive
-- ----------------------------
INSERT INTO `t_receive` VALUES ('1', 'ZY001', 'user1', '20', '2017-11-07 16:21:56', '给学生讲课');
INSERT INTO `t_receive` VALUES ('3', 'ZY001', 'user1', '20', '2017-11-02 23:34:18', '学生上课');
INSERT INTO `t_receive` VALUES ('4', 'ZY002', 'user1', '1', '2017-11-08 23:35:36', 'test');
INSERT INTO `t_receive` VALUES ('5', 'ZY002', 'user1', '3', '2017-11-14 00:30:38', 'test');

-- ----------------------------
-- Table structure for `t_resourceinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_resourceinfo`;
CREATE TABLE `t_resourceinfo` (
  `resourceNo` varchar(20) NOT NULL COMMENT 'resourceNo',
  `resourceTypeObj` int(11) NOT NULL COMMENT '资源类型',
  `name` varchar(80) NOT NULL COMMENT '资源名称',
  `resourcePhoto` varchar(60) NOT NULL COMMENT '资源图片',
  `numberLimit` varchar(20) NOT NULL COMMENT '数量限制',
  `resourceNum` int(11) NOT NULL COMMENT '资源库存',
  `addDate` varchar(20) default NULL COMMENT '加入日期',
  `resourceDesc` varchar(2000) default NULL COMMENT '资源描述',
  PRIMARY KEY  (`resourceNo`),
  KEY `resourceTypeObj` (`resourceTypeObj`),
  CONSTRAINT `t_resourceinfo_ibfk_1` FOREIGN KEY (`resourceTypeObj`) REFERENCES `t_resourcetype` (`typeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_resourceinfo
-- ----------------------------
INSERT INTO `t_resourceinfo` VALUES ('ZY001', '1', 'HTML5网站开发实践', 'upload/6da3aac0-ff9d-4c49-ad7b-4d28281b47b5.jpg', '是', '78', '2017-11-01', '教你怎么开发手机电脑适配网站');
INSERT INTO `t_resourceinfo` VALUES ('ZY002', '2', '2016届学长计算机接口笔记', 'upload/fa16275a-001d-479f-b94f-ab1cce0bfb9e.jpg', '否', '999', '2017-11-04', '这个是笔记是学霸学长王大树在2016输入电脑保存的！');
INSERT INTO `t_resourceinfo` VALUES ('ZY003', '1', '安卓程序开发详解', 'upload/71fad05a-4a76-47f7-b117-eaedc95a3750.jpg', '是', '20', '2017-11-28', '双鱼林大神带你走入安卓手机app世界，哈哈');

-- ----------------------------
-- Table structure for `t_resourcetype`
-- ----------------------------
DROP TABLE IF EXISTS `t_resourcetype`;
CREATE TABLE `t_resourcetype` (
  `typeId` int(11) NOT NULL auto_increment COMMENT '类型id',
  `typeName` varchar(50) NOT NULL COMMENT '类型名称',
  `typeDesc` varchar(500) default NULL COMMENT '类别描述',
  `addDate` varchar(20) default NULL COMMENT '加入日期',
  PRIMARY KEY  (`typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_resourcetype
-- ----------------------------
INSERT INTO `t_resourcetype` VALUES ('1', '图书', '图书是一本好书，带你走入知识的海洋', '2017-11-02');
INSERT INTO `t_resourcetype` VALUES ('2', '电子资料', '一些宝贵的，学姐学哥留下的学习笔记电子档', '2017-11-03');

-- ----------------------------
-- Table structure for `t_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_userinfo`;
CREATE TABLE `t_userinfo` (
  `user_name` varchar(20) NOT NULL COMMENT 'user_name',
  `password` varchar(20) NOT NULL COMMENT '登录密码',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `sex` varchar(4) NOT NULL COMMENT '性别',
  `birthday` varchar(20) default NULL COMMENT '出生日期',
  `userPhoto` varchar(60) NOT NULL COMMENT '用户照片',
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_userinfo
-- ----------------------------
INSERT INTO `t_userinfo` VALUES ('user1', '123', '双鱼林', '男', '2017-11-01', 'upload/7b8eacc4-b07f-4305-8025-04b0b4cb9e41.jpg');
INSERT INTO `t_userinfo` VALUES ('user2', '123', '张小倩', '女', '2017-11-02', 'upload/f20b3ba4-b92c-4514-95fb-dff6eb3d5136.jpg');
