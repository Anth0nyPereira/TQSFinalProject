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

 Date: 18/06/2021 21:48:48
*/

CREATE DATABASE proudpapers;
USE proudpapers;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS cart;
CREATE TABLE cart  (
  `id` int NOT NULL AUTO_INCREMENT,
  `client` int NOT NULL,
  PRIMARY KEY (`id`, `client`) USING BTREE,
  INDEX `client`(`client`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`client`) REFERENCES `client` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for cart_products
-- ----------------------------
DROP TABLE IF EXISTS cart_products;
CREATE TABLE cart_products  (
  `cart` int NOT NULL,
  `product` int NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`cart`, `product`) USING BTREE,
  INDEX `product`(`product`) USING BTREE,
  CONSTRAINT `cart_products_ibfk_1` FOREIGN KEY (`cart`) REFERENCES `cart` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cart_products_ibfk_2` FOREIGN KEY (`product`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of cart_products
-- ----------------------------

-- ----------------------------
-- Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS client;
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
-- Records of client
-- ----------------------------

-- ----------------------------
-- Table structure for delivery
-- ----------------------------
DROP TABLE IF EXISTS delivery;
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
-- Records of delivery
-- ----------------------------

-- ----------------------------
-- Table structure for payment_method
-- ----------------------------
DROP TABLE IF EXISTS payment_method;
CREATE TABLE payment_method  (
  `id` int NOT NULL AUTO_INCREMENT,
  `card_number` char(16) NOT NULL,
  `card_expiration_month` varchar(2) NOT NULL,
  `cvc` char(3) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
);

-- ----------------------------
-- Records of payment_method
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS product;
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
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 'Atmamun', 15.99, 99, 'The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God');
INSERT INTO `product` VALUES (2, 'Sabbatical Of The Mind', 7.99, 99, 'While racing around the Washington Beltway on his way to a successful career, the author seemed to misplace his personal peace, purpose for living and a dream or two. Panic attacks and several humorous events led him to the conclusion that he needed to stop the world and get off for a time. David Winters\' Sabbatical of the Mind chronicles his adventures in temporary unemployment, searching for answers to life\'s big questions and eventually, a successful re-entry into work and life. Come along for the ride and you just might conquer a few fears of your own.');
INSERT INTO `product` VALUES (3, 'According To The Daily Mail', 88.50, 99, 'A jolly jape. A darkly comic crime caper with a salient social message. Predicated on the puerile piffle characteristic of the tabloid press, this is a refreshingly unique novel. Sharply witty, it brings you suspense, ingenuity, adventure, romance, laugh-out-loud comedy and some sporadic eroticism. Jonathon is cross about the deleterious effect on impressionable people of the shallow tabloid media and he sets about recruiting some disaffected ex-servicemen to blow up their printing-presses. Another recruit erases all their websites. An unfortunate coincidence leads Detective Inspector Foot to his door, but Jonathon has a cast-iron alibi and at first he gets away with it. He also recruits someone to hack into some TV programs, and people see titles like \'why are you watching this rubbish?\' appear on their screens. Interspersed, he enjoys various adventures with his two children and his naughty new girlfriend, Bianca, and months later the crimes remain unsolved. Detective Inspector Foot, however, remains sceptical about Jonathon, assumes he is guilty, and sets about uncovering some evidence...');
INSERT INTO `product` VALUES (4, 'Harry Potter And The Cursed Child', 7.80, 99, 'The Eighth Story. Nineteen Years Later. Based on an original story by J.K. Rowling, John Tiffany, and Jack Thorne, a play by Jack Thorne.\r\nIt was always difficult being Harry Potter and it isn’t much easier now that he is an overworked employee of the Ministry of Magic, a husband, and father of three school-age children.While Harry grapples with a past that refuses to stay where it belongs, his youngest son, Albus, must struggle with the weight of a family legacy he never wanted. As past and present fuse ominously, both father and son learn the uncomfortable truth: Sometimes, darkness comes from unexpected places.The playscript for Harry Potter and the Cursed Child was originally released as a \"special rehearsal edition\" alongside the opening of Jack Thorne’s play in London’s West End in summer 2016. Based on an original story by J.K. Rowling, John Tiffany, and Jack Thorne, the play opened to rapturous reviews from theatergoers and critics alike, while the official playscript became an immediate global bestseller.');
INSERT INTO `product` VALUES (5, 'Malibu Rising', 14.83, 99, 'Read with Jenna Book Club Pick as Featured on Today • From the New York Times bestselling author of Daisy Jones & The Six and The Seven Husbands of Evelyn Hugo . . . \r\n\r\nFour famous siblings throw an epic party to celebrate the end of the summer. But over the course of twenty-four hours, their lives will change forever.');
INSERT INTO `product` VALUES (6, 'The Last Thing He Told Me', 16.50, 99, 'Instant #1 New York Times bestseller\r\nNow a Reese Witherspoon Book Club Selection!\r\n\r\nA gripping mystery about a woman who thinks she’s found the love of her life—until he disappears.');
INSERT INTO `product` VALUES (7, 'How the Word Is Passed', 14.99, 99, '\"We need this book.\" —Ibram X. Kendi, #1 New York Times bestselling author of How to be an Anti-Racist\r\n\r\nThe Atlantic staff writer and poet Clint Smith\'s revealing, contemporary portrait of America as a slave owning nation');
INSERT INTO `product` VALUES (8, 'Greenlights', 12.99, 99, '#1 NEW YORK TIMES BESTSELLER • Over one million copies sold! From the Academy Award®–winning actor, an unconventional memoir filled with raucous stories, outlaw wisdom, and lessons learned the hard way about living with greater satisfaction\r\n \r\n“Unflinchingly honest and remarkably candid, Matthew McConaughey’s book invites us to grapple with the lessons of his life as he did—and to see that the point was never to win, but to understand.”—Mark Manson, author of The Subtle Art of Not Giving a F*ck');
INSERT INTO `product` VALUES (9, 'What Happened to You', 15.89, 99, '#1 NEW YORK TIMES BESTSELLER\r\n\r\nOur earliest experiences shape our lives far down the road, and What Happened to You? provides powerful scientific and emotional insights into the behavioral patterns so many of us struggle to understand.\r\n\r\n“Through this lens we can build a renewed sense of personal self-worth and ultimately recalibrate our responses to circumstances, situations, and relationships. It is, in other words, the key to reshaping our very lives.”—Oprah Winfrey\r\n\r\nThis book is going to change the way you see your life.');
INSERT INTO `product` VALUES (10, 'Atomic Habits', 11.99, 99, 'THE PHENOMENAL INTERNATIONAL BESTSELLER: 1 MILLION COPIES SOLD\r\n\r\nTransform your life with tiny changes in behaviour, starting now.\r\n\r\nPeople think that when you want to change your life, you need to think big. But world-renowned habits expert James Clear has discovered another way. He knows that real change comes from the compound effect of hundreds of small decisions: doing two push-ups a day, waking up five minutes early, or holding a single short phone call.\r\n\r\nHe calls them atomic habits.\r\n\r\nIn this ground-breaking book, Clears reveals exactly how these minuscule changes can grow into such life-altering outcomes. He uncovers a handful of simple life hacks (the forgotten art of Habit Stacking, the unexpected power of the Two Minute Rule, or the trick to entering the Goldilocks Zone), and delves into cutting-edge psychology and neuroscience to explain why they matter. Along the way, he tells inspiring stories of Olympic gold medalists, leading CEOs, and distinguished scientists who have used the science of tiny habits to stay productive, motivated, and happy.\r\n\r\nThese small changes will have a revolutionary effect on your career, your relationships, and your life.');
INSERT INTO `product` VALUES (11, 'The Four Agreements', 6.12, 99, 'In The Four Agreements, don Miguel Ruiz reveals the source of self-limiting beliefs that rob us of joy and create needless suffering. Based on ancient Toltec wisdom, The Four Agreements offer a powerful code of conduct that can rapidly transform our lives to a new experience of freedom, true happiness, and love.\r\n\r\n• A New York Times bestseller for over a decade\r\n• An international bestseller translated into 46 languages worldwide\r\n\r\n\"This book by don Miguel Ruiz, simple yet so powerful, has made a tremendous difference in how I think and act in every encounter.\" — Oprah Winfrey\r\n\r\n\"Don Miguel Ruiz’s book is a roadmap to enlightenment and freedom.” — Deepak Chopra, Author, The Seven Spiritual Laws of Success\r\n\r\n“An inspiring book with many great lessons.” — Wayne Dyer, Author, Real Magic\r\n\r\n“In the tradition of Castaneda, Ruiz distills essential Toltec wisdom, expressing with clarity and impeccability what it means for men and women to live as peaceful warriors in the modern world.” — Dan Millman, Author, Way of the Peaceful Warrior');


-- ----------------------------
-- Table structure for products_of_delivery
-- ----------------------------
DROP TABLE IF EXISTS products_of_delivery;
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
-- Records of products_of_delivery
-- ----------------------------

-- ----------------------------
-- Table structure for state
-- ----------------------------
DROP TABLE IF EXISTS state;
CREATE TABLE state  (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `delivery` int NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `delivery`(`delivery`) USING BTREE,
  CONSTRAINT `state_ibfk_1` FOREIGN KEY (`delivery`) REFERENCES `delivery` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of state
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
