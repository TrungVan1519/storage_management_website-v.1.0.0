DROP DATABASE IF EXISTS `Storage_Management`;
CREATE DATABASE IF NOT EXISTS `Storage_Management`;
USE `Storage_Management`;

CREATE TABLE `user`(
	`id` 			int(11) not null auto_increment,
    `username` 		varchar(50)	not null, 
    `password` 		varchar(50) not null,
    `name` 			varchar(100) not null,
    `email` 		varchar(100) null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
  
CREATE TABLE `user_role`(
	`id` 			int(11) not null auto_increment,
	`user_id`		int(11) not null,
	`role_id`		int(11) not null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `user_role` ADD CONSTRAINT `userId_foreign_key` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE `user_role` ADD CONSTRAINT `roleId_foreign_key` FOREIGN KEY (`role_id`) REFERENCES `role`(`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;

  
CREATE TABLE `role`(
	`id` 			int(11) not null auto_increment,
	`role_name` 	varchar(50) not null,
    `description` 	varchar(200) null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
  
CREATE TABLE `auth`(
	`id` 			int(11) not null auto_increment,
	`role_id`		int(11) not null,
	`menu_id`		int(11) not null,
    `permission` 	int(1) not null default '1',
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `auth` ADD CONSTRAINT `roleId_auth_foreign_key` FOREIGN KEY (`role_id`) REFERENCES `role`(`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE `auth` ADD CONSTRAINT `menuId_auth_foreign_key` FOREIGN KEY (`menu_id`) REFERENCES `menu`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
  
CREATE TABLE `menu`(
	`id` 			int(11) not null auto_increment,
	`parent_menu_id` int(11) not null,
	`url`			varchar(100) not null,
    `name` 			varchar(100) not null,
    `order_index` 	int(1) not null default '0',
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
  
CREATE TABLE `category`(
	`id` 			int(11) not null auto_increment,
	`name` 			varchar(100) not null,
	`code`			varchar(50) not null,
    `description`	text,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
  
CREATE TABLE `product_in_stock`(
	`id` 			int(11) not null auto_increment,
	`product_id` 	int(11) not null ,
	`quantity`		int(11) not null,
    `price` 		decimal(15, 2) not null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `product_in_stock` ADD CONSTRAINT `productId_foreign_key` FOREIGN KEY (`product_id`) REFERENCES `product_info`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
  
CREATE TABLE `product_info`(
	`id` 			int(11) not null auto_increment,
	`category_id` 	int(11) not null ,
    `name`			varchar(100) not null,
	`code`			varchar(50) not null,
    `description` 	text,
    `image_url` 	varchar(200) null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `product_info` ADD CONSTRAINT `categoryId_foreign_key` FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
  
CREATE TABLE `history`(
	`id` 			int(11) not null auto_increment,
	`action_name` 	varchar(100) not null ,
	`type`			int(1) not null,
    `product_id` 	int(11) not null,
    `quantity` 		int(11) not null,
    `price` 		decimal(15, 2) not null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `history` ADD CONSTRAINT `productId_history_foreign_key` FOREIGN KEY (`product_id`) REFERENCES `product_info`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
  
CREATE TABLE `invoice`(
	`id` 			int(11) not null auto_increment,
	`code` 			varchar(50) not null ,
	`type`			int(1) not null,
    `product_id` 	int(11) not null,
    `quantity` 		int(11) not null,
    `price` 		decimal(15, 2) not null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `invoice` ADD CONSTRAINT `productId_invoice_foreign_key` FOREIGN KEY (`product_id`) REFERENCES `product_info`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;


select * from `product_info`;





