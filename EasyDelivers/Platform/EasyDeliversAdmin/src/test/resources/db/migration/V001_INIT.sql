CREATE TABLE rider  (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NULL DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `telephone` char(11) NOT NULL,
  `delivery_radius` int NOT NULL DEFAULT 5,
  `transportation` varchar(255) NOT NULL,
  `salary` double NOT NULL DEFAULT 0,
  `score` double NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE,
  UNIQUE INDEX `telephone`(`telephone`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
);
