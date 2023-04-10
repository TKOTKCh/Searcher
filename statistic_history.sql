/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 11/04/2023 03:35:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for statistic_history
-- ----------------------------
DROP TABLE IF EXISTS `statistic_history`;
CREATE TABLE `statistic_history`  (
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of statistic_history
-- ----------------------------
INSERT INTO `statistic_history` VALUES ('2023-04-03 ', '0');
INSERT INTO `statistic_history` VALUES ('2023-04-04 ', '98');
INSERT INTO `statistic_history` VALUES ('2023-04-05 ', '128');
INSERT INTO `statistic_history` VALUES ('2023-04-06 ', '99');
INSERT INTO `statistic_history` VALUES ('2023-04-07 ', '16');
INSERT INTO `statistic_history` VALUES ('2023-04-08 ', '256');
INSERT INTO `statistic_history` VALUES ('2023-04-09 ', '79');
INSERT INTO `statistic_history` VALUES ('2023-04-10 ', '100');
INSERT INTO `statistic_history` VALUES ('2023-04-11', '8');
INSERT INTO `statistic_history` VALUES ('total_click', '784');
INSERT INTO `statistic_history` VALUES ('user_count', '3');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `career` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `interests` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (6, 'Yu', '$2a$10$YdIjo81kchBkI3k/5TInm.63d.KReBfhhRaHy/MyCxYvzO/WdSc3m', NULL, NULL, '北京海淀', '学生', 24, '男', NULL, NULL, NULL);
INSERT INTO `user` VALUES (9, '弦一郎', '$2a$10$Ahe6DMxBkzg0tfD4Cf1HvOMe6nCOXcXkoSh9y4jdTmIYpJUmsaCjS', NULL, NULL, '芦名城', '武士', 29, '男', NULL, NULL, NULL);
INSERT INTO `user` VALUES (10, '小安', '$2a$10$EzYbq5JEIPDr61dVB6do..H2PoxgmoM0pUj.j4iJCQC/DHHsSwcWy', NULL, NULL, '四川成都', '会计', 28, '女', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for user_query
-- ----------------------------
DROP TABLE IF EXISTS `user_query`;
CREATE TABLE `user_query`  (
  `userid` int(11) NOT NULL,
  `query` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` double NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `user_query` VALUES (5, '北京', 1681131256234);
INSERT INTO `user_query` VALUES (5, '北京', 1681131256346);
INSERT INTO `user_query` VALUES (5, '广东', 1681128566798);
INSERT INTO `user_query` VALUES (5, '北京', 1681131163147);
INSERT INTO `user_query` VALUES (5, '北京', 1681131219613);
INSERT INTO `user_query` VALUES (6, '成都', 1681154619914);
INSERT INTO `user_query` VALUES (6, '上海', 1681154998377);
INSERT INTO `user_query` VALUES (6, '1', 1681149027013);
INSERT INTO `user_query` VALUES (6, '北京', 1681149038572);
INSERT INTO `user_query` VALUES (6, '北京西站', 1681154244511);

SET FOREIGN_KEY_CHECKS = 1;
