/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : proudpapers

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 16/06/2021 18:09:35
*/

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS cart
CREATE TABLE cart  (
  `id` int NOT NULL AUTO_INCREMENT,
  `client` int NOT NULL,
  PRIMARY KEY (`id`, `client`) USING BTREE,
  INDEX `client`(`client`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`client`) REFERENCES `client` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Table structure for cart_products
-- ----------------------------

DROP TABLE IF EXISTS cart_product
CREATE TABLE cart_product  (
  `cart` int NOT NULL,
  `product` int NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`cart`, `product`) USING BTREE,
  INDEX `product`(`product`) USING BTREE,
  CONSTRAINT `cart_products_ibfk_1` FOREIGN KEY (`cart`) REFERENCES `cart` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cart_products_ibfk_2` FOREIGN KEY (`product`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS client
CREATE TABLE client  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `telephone` char(13) NOT NULL,
  `payment_method` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE,
  INDEX `payment_method`(`payment_method`) USING BTREE,
  CONSTRAINT `client_ibfk_1` FOREIGN KEY (`payment_method`) REFERENCES `payment_method` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Table structure for delivery
-- ----------------------------
DROP TABLE IF EXISTS delivery
CREATE TABLE delivery  (
  `id` int NOT NULL AUTO_INCREMENT,
  `total_price` decimal(10, 2) NOT NULL DEFAULT 0.00,
  `state` varchar(255) NOT NULL DEFAULT 'awaiting_processing',
  `client` int NOT NULL,
  `id_delivery_store` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `client`(`client`) USING BTREE,
  CONSTRAINT `delivery_ibfk_1` FOREIGN KEY (`client`) REFERENCES `client` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);
-- ----------------------------
-- Table structure for payment_method
-- ----------------------------
DROP TABLE IF EXISTS payment_method
CREATE TABLE payment_method  (
  `id` int NOT NULL AUTO_INCREMENT,
  `card_number` char(16) NOT NULL,
  `card_expiration_month` varchar(2) NOT NULL,
  `cvc` char(3) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS product
CREATE TABLE product  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  `description` text NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
);

-- ----------------------------
-- Table structure for products_of_delivery
-- ----------------------------
DROP TABLE IF EXISTS products_of_delivery
CREATE TABLE products_of_delivery  (
  `product` int NOT NULL,
  `delivery` int NOT NULL,
  `quantity` int NOT NULL,
  INDEX `product`(`product`) USING BTREE,
  INDEX `delivery`(`delivery`) USING BTREE,
  CONSTRAINT `products_of_delivery_ibfk_1` FOREIGN KEY (`product`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `products_of_delivery_ibfk_2` FOREIGN KEY (`delivery`) REFERENCES `delivery` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);
-- ----------------------------
-- Table structure for state
-- ----------------------------
DROP TABLE IF EXISTS state
CREATE TABLE state  (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `delivery` int NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `delivery`(`delivery`) USING BTREE,
  CONSTRAINT `state_ibfk_1` FOREIGN KEY (`delivery`) REFERENCES `delivery` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);