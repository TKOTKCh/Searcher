/*
 Navicat Premium Data Transfer

 Source Server         : MYSQL
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 04/04/2023 17:00:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_query
-- ----------------------------
DROP TABLE IF EXISTS `user_query`;
CREATE TABLE `user_query`  (
  `userid` int NOT NULL,
  `query` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `time` double NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_query
-- ----------------------------
INSERT INTO `user_query` VALUES (100, '777', 1000);
INSERT INTO `user_query` VALUES (100, '777', 1000);
INSERT INTO `user_query` VALUES (100, '777', 1000);
INSERT INTO `user_query` VALUES (100, '10', 6);
INSERT INTO `user_query` VALUES (100, '10', 6);
INSERT INTO `user_query` VALUES (101, '10', 6);
INSERT INTO `user_query` VALUES (666, '888', 1680598353162);
INSERT INTO `user_query` VALUES (666, '888', 1680598367166);

SET FOREIGN_KEY_CHECKS = 1;
