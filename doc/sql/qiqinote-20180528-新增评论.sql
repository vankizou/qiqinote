/*
 Navicat Premium Data Transfer

 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Database       : qiqinote

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : utf-8

 Date: 05/28/2018 14:51:30 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `root_id` bigint(20) DEFAULT NULL COMMENT '评论根',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
  `path` varchar(200) DEFAULT NULL COMMENT '链路路径',
  `type` tinyint(2) DEFAULT NULL COMMENT '评论类型。1 笔记',
  `target_id` bigint(20) DEFAULT NULL COMMENT '目标id。如 type=笔记时，为笔记id',
  `target_uri` varchar(64) DEFAULT NULL COMMENT '目标请求uri，如笔记：有的用id，有的用idLink',
  `target_user_id` bigint(20) DEFAULT NULL COMMENT '目标所属用户。如 type=笔记时，为创建该笔记的用户id',
  `from_user_id` bigint(20) DEFAULT NULL COMMENT '创建该条评论的用户',
  `to_user_id` bigint(20) DEFAULT NULL COMMENT '评论谁',
  `content` varchar(555) DEFAULT NULL COMMENT '评论内容',
  `sub_num` int(11) DEFAULT NULL COMMENT '下级评论数量',
  `create_datetime` datetime DEFAULT NULL COMMENT '创建时间',
  `is_del` tinyint(2) DEFAULT '0' COMMENT '是否删除。0否，1是',
  PRIMARY KEY (`id`),
  KEY `target_id` (`target_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `env`
-- ----------------------------
DROP TABLE IF EXISTS `env`;
CREATE TABLE `env` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `k` varchar(64) DEFAULT NULL COMMENT 'key',
  `v` varchar(200) DEFAULT NULL COMMENT 'value',
  `create_datetime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `note`
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_link` varchar(64) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT '-1' COMMENT '父ID',
  `path` varchar(255) DEFAULT NULL COMMENT '目录树路径, 如: -1_父父id_父id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `type` int(2) DEFAULT NULL COMMENT '笔记类型. 1普通笔记, 2Markdown',
  `note_num` int(11) DEFAULT NULL COMMENT '笔记数量. 只当是目录时有值',
  `note_content_num` int(11) DEFAULT NULL COMMENT '笔记内容段数量',
  `secret` int(2) DEFAULT NULL COMMENT '私密状态. 0公开访问, 1密码访问, 2私密访问, 3链接访问',
  `password` varchar(64) DEFAULT NULL COMMENT '密码, 笔记为私密时有值',
  `title` varchar(255) DEFAULT NULL COMMENT '笔记标题',
  `keyword` varchar(127) DEFAULT NULL COMMENT '关键词，多个以空隔分隔',
  `sequence` int(11) DEFAULT NULL COMMENT '序号',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `view_num` bigint(20) DEFAULT '0' COMMENT '浏览数',
  `digest` varchar(255) DEFAULT NULL COMMENT '摘要',
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `origin_url` varchar(500) DEFAULT NULL COMMENT '文章来源',
  `status` int(2) DEFAULT '2' COMMENT '文章状态. -1待审核，0不通过，1通过',
  `status_description` varchar(500) DEFAULT NULL COMMENT '审核理由',
  `create_datetime` datetime DEFAULT NULL COMMENT '创建时间',
  `update_datetime` datetime DEFAULT NULL COMMENT '更新时间',
  `is_del` int(2) DEFAULT '0' COMMENT '是否删除. 0否 1是. 默认0',
  PRIMARY KEY (`id`),
  KEY `索引1` (`user_id`),
  KEY `索引2` (`parent_id`),
  KEY `id_link` (`id_link`)
) ENGINE=InnoDB AUTO_INCREMENT=287 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `note_detail`
-- ----------------------------
DROP TABLE IF EXISTS `note_detail`;
CREATE TABLE `note_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `note_id` bigint(20) DEFAULT NULL COMMENT '笔记id',
  `content` text COMMENT '笔记内容',
  `type` int(2) DEFAULT '1' COMMENT '类型. 1普通笔记, 2Markdown',
  `sequence` int(11) DEFAULT NULL COMMENT '序号',
  `create_datetime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `索引` (`note_id`)
) ENGINE=InnoDB AUTO_INCREMENT=267 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `picture`
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `name` varchar(127) DEFAULT NULL COMMENT '图片上传名称',
  `path` varchar(127) DEFAULT NULL COMMENT '图片相对路径',
  `width` int(6) DEFAULT NULL COMMENT '图片宽',
  `height` int(6) DEFAULT NULL COMMENT '图片高',
  `size` bigint(20) DEFAULT NULL COMMENT '图片大小',
  `type` varchar(16) DEFAULT NULL COMMENT '图片类型. 如: jpg gif png jpeg',
  `use_type` int(2) DEFAULT NULL COMMENT '图片用处. 1笔记 2头像',
  `create_datetime` datetime DEFAULT NULL COMMENT '创建时间',
  `is_del` int(2) DEFAULT '0' COMMENT '是否删除. 0否 1是. 默认0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `security_question`
-- ----------------------------
DROP TABLE IF EXISTS `security_question`;
CREATE TABLE `security_question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `question` varchar(64) DEFAULT NULL COMMENT '问题',
  `answer` varchar(64) DEFAULT NULL COMMENT '答案',
  `create_datetime` datetime DEFAULT NULL COMMENT '创建时间',
  `is_del` int(2) DEFAULT '0' COMMENT '是否删除. 0否 1是. 默认0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar_id` bigint(20) DEFAULT NULL COMMENT '头像关联图片id',
  `name` varchar(64) DEFAULT NULL COMMENT '登录名, 唯一, 可空',
  `alias` varchar(64) DEFAULT NULL COMMENT '别名',
  `password` varchar(127) DEFAULT NULL COMMENT '密码',
  `gender` int(2) DEFAULT '0' COMMENT '性别. 0保密, 1男, 2女. 默认0',
  `status` int(2) DEFAULT '1' COMMENT '状态. 0禁用, 1启用. 默认1',
  `motto` varchar(200) DEFAULT NULL COMMENT '格言',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `qq` varchar(32) DEFAULT NULL COMMENT 'QQ',
  `weixin` varchar(64) DEFAULT NULL COMMENT '微信',
  `weibo` varchar(64) DEFAULT NULL COMMENT '微博',
  `birthday` datetime DEFAULT NULL,
  `register_origin` int(2) DEFAULT NULL COMMENT '注册来源: 1无, 2手机号, 3微信, 4QQ, 5微博',
  `register_ip` varchar(32) DEFAULT NULL COMMENT '注册IP',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_datetime` datetime DEFAULT NULL COMMENT '创建时间',
  `is_del` int(2) DEFAULT '0' COMMENT '是否删除. 0否, 1是. 默认0',
  PRIMARY KEY (`id`),
  KEY `索引` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1026 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `user_login_record`
-- ----------------------------
DROP TABLE IF EXISTS `user_login_record`;
CREATE TABLE `user_login_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(2) DEFAULT NULL COMMENT '用户id',
  `origin` int(2) DEFAULT '1' COMMENT '登录来源: 0自动登录, 1无, 2手机号, 3微信, 4QQ, 5微博',
  `ip` varchar(32) DEFAULT NULL,
  `protocol` varchar(127) DEFAULT NULL,
  `scheme` varchar(127) DEFAULT NULL,
  `server_name` varchar(127) DEFAULT NULL,
  `remote_addr` varchar(32) DEFAULT NULL,
  `remote_host` varchar(32) DEFAULT NULL,
  `character_encoding` varchar(32) DEFAULT NULL,
  `accept` varchar(255) DEFAULT NULL,
  `accept_encoding` varchar(64) DEFAULT NULL,
  `accept_language` varchar(255) DEFAULT NULL,
  `user_agent` varchar(500) DEFAULT NULL,
  `connection` varchar(32) DEFAULT NULL,
  `create_datetime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=381 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `word`
-- ----------------------------
DROP TABLE IF EXISTS `word`;
CREATE TABLE `word` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `from` varchar(100) NOT NULL,
  `word` varchar(500) NOT NULL,
  `create_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `word` (`word`)
) ENGINE=InnoDB AUTO_INCREMENT=971 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
