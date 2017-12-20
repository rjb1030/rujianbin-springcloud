CREATE TABLE `rjb_authority` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`authority_code`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=5
ROW_FORMAT=DYNAMIC
;
INSERT INTO `mytest`.`rjb_authority` (`id`, `authority_code`) VALUES ('1', 'p1:f1:read');
INSERT INTO `mytest`.`rjb_authority` (`id`, `authority_code`) VALUES ('2', 'p1:f2:read');
INSERT INTO `mytest`.`rjb_authority` (`id`, `authority_code`) VALUES ('3', 'p2:f3:edit');
INSERT INTO `mytest`.`rjb_authority` (`id`, `authority_code`) VALUES ('4', 'p2:f4:edit');





CREATE TABLE `rjb_role` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`role_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=4
ROW_FORMAT=DYNAMIC
;
INSERT INTO `mytest`.`rjb_role` (`id`, `role_name`) VALUES ('1', 'ROLE_admin');
INSERT INTO `mytest`.`rjb_role` (`id`, `role_name`) VALUES ('2', 'ROLE_admin2');
INSERT INTO `mytest`.`rjb_role` (`id`, `role_name`) VALUES ('3', 'ROLE_admin3');




CREATE TABLE `rjb_role_authority_rela` (
`role_id`  bigint(20) NOT NULL ,
`authority_code`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=DYNAMIC
;
INSERT INTO `mytest`.`rjb_role_authority_rela` (`role_id`, `authority_code`) VALUES ('1', 'p1:f1:read');
INSERT INTO `mytest`.`rjb_role_authority_rela` (`role_id`, `authority_code`) VALUES ('1', 'p1:f2:read');
INSERT INTO `mytest`.`rjb_role_authority_rela` (`role_id`, `authority_code`) VALUES ('2', 'p2:f3:edit');
INSERT INTO `mytest`.`rjb_role_authority_rela` (`role_id`, `authority_code`) VALUES ('3', 'p2:f4:edit');




CREATE TABLE `rjb_user` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`create_date`  datetime NOT NULL ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`username`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`password`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=4
ROW_FORMAT=DYNAMIC
;
INSERT INTO `mytest`.`rjb_user` (`id`, `create_date`, `name`, `username`, `password`) VALUES ('1', '2017-03-31 16:34:28', '汝建斌', 'rjb', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `mytest`.`rjb_user` (`id`, `create_date`, `name`, `username`, `password`) VALUES ('2', '2017-04-18 17:44:41', '汝张三', 'rjb2', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `mytest`.`rjb_user` (`id`, `create_date`, `name`, `username`, `password`) VALUES ('3', '2017-04-18 17:45:30', '汝王五', 'rjb3', 'e10adc3949ba59abbe56e057f20f883e');





CREATE TABLE `rjb_user_role_rela` (
`user_id`  bigint(20) NOT NULL ,
`role_id`  bigint(20) NOT NULL
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=DYNAMIC
;
INSERT INTO `mytest`.`rjb_user_role_rela` (`user_id`, `role_id`) VALUES ('1', '1');
INSERT INTO `mytest`.`rjb_user_role_rela` (`user_id`, `role_id`) VALUES ('1', '2');
INSERT INTO `mytest`.`rjb_user_role_rela` (`user_id`, `role_id`) VALUES ('2', '1');
INSERT INTO `mytest`.`rjb_user_role_rela` (`user_id`, `role_id`) VALUES ('2', '2');
INSERT INTO `mytest`.`rjb_user_role_rela` (`user_id`, `role_id`) VALUES ('3', '1');
INSERT INTO `mytest`.`rjb_user_role_rela` (`user_id`, `role_id`) VALUES ('3', '2');


