/*
 Navicat Premium Data Transfer

 Source Server         : 本地5.7
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:8081
 Source Schema         : spring

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 06/06/2021 17:51:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sign
-- ----------------------------
DROP TABLE IF EXISTS `sign`;
CREATE TABLE `sign`  (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
  `contactTag` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '近14天内您是否接触新冠肺炎确诊或疑似患者？',
  `statusTag` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前状况（可多选）',
  `temperature` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '体温',
  `userId` bigint(20) NOT NULL,
  `time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE,
  INDEX `sign_user`(`userId`) USING BTREE,
  CONSTRAINT `sign_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sign
-- ----------------------------
INSERT INTO `sign` VALUES (1, '河北省', '1', '1', '36.5', 1, '2021-06-02 22:15:13');
INSERT INTO `sign` VALUES (2, '河北省', '1', '1', '36.3', 1, '2021-06-01 22:17:06');
INSERT INTO `sign` VALUES (3, '河南省', '1', '1', '36.2', 1, '2021-05-31 11:30:59');
INSERT INTO `sign` VALUES (4, '河南省', '1', '1', '36.5', 1, '2021-05-30 00:32:25');
INSERT INTO `sign` VALUES (5, '河北省', '1', '1', '37', 1, '2021-05-29 00:32:52');
INSERT INTO `sign` VALUES (14, '河北省石家市桥西区', '0', '0', '36.5', 1, '2021-06-05 17:37:02');
INSERT INTO `sign` VALUES (15, '河北省石家市桥西区', '0', '0', '36.5', 12, '2021-06-05 21:45:43');
INSERT INTO `sign` VALUES (16, '河北省石家庄市桥西区', '0', '0', '36.5', 1, '2021-06-06 00:52:42');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `telephone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `birthday` datetime NULL DEFAULT NULL COMMENT '生日',
  `gender` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `address` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `idcard` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码(加密处理)',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'xiaoming', '17634556789', NULL, '1', '河北省', '130234567890123456', '8aFV4Kj42RhFzR3juuiIAA==', '小明');
INSERT INTO `user` VALUES (2, 'xiaogang', '17634556780', NULL, '1', '河北省', '130234567890123457', '3fQu+1z2SQlkuufbLVlVHg==', '小刚');
INSERT INTO `user` VALUES (5, 'xiaoming1', '17634556781', NULL, '1', '河北省', '130234567890123459', '8aFV4Kj42RjCRml7nuscnA==', 'xiaoming1');
INSERT INTO `user` VALUES (12, 'xiaoming2', '17634567890', NULL, '1', '河北省', '123456789012345678', '8aFV4Kj42RhFzR3juuiIAA==', 'xiaoming2');

SET FOREIGN_KEY_CHECKS = 1;
