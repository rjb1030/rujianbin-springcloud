CREATE TABLE `oauth_access_token` (
`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
`token_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`token`  blob NULL ,
`authentication_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`user_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`client_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`authentication`  blob NULL ,
`refresh_token`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
INDEX `token_id_index` (`token_id`) USING BTREE ,
INDEX `authentication_id_index` (`authentication_id`) USING BTREE ,
INDEX `user_name_index` (`user_name`) USING BTREE ,
INDEX `client_id_index` (`client_id`) USING BTREE ,
INDEX `refresh_token_index` (`refresh_token`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=DYNAMIC
;


CREATE TABLE `oauth_client_details` (
`client_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`resource_ids`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`client_secret`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`scope`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`authorized_grant_types`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`web_server_redirect_uri`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`authorities`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`access_token_validity`  int(11) NULL DEFAULT NULL ,
`refresh_token_validity`  int(11) NULL DEFAULT NULL ,
`additional_information`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
`archived`  tinyint(1) NULL DEFAULT 0 ,
`trusted`  tinyint(1) NULL DEFAULT 0 ,
`autoapprove`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'false' ,
PRIMARY KEY (`client_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=DYNAMIC
;

INSERT INTO `oauth2`.`oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `create_time`, `archived`, `trusted`, `autoapprove`) VALUES ('client_rjb', 'rujianbin-oauth2-resource', '123456', 'read,write', 'authorization_code,refresh_token,implicit,client_credentials', 'http://127.0.0.1:8080/rujianbin-thirdparty-web/common/index', 'p1:f1:read,p1:f1:edit', NULL, NULL, NULL, '2017-05-15 13:46:05', '0', '0', 'false');



CREATE TABLE `oauth_code` (
`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
`code`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`authentication`  blob NULL ,
INDEX `code_index` (`code`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=DYNAMIC
;


CREATE TABLE `oauth_refresh_token` (
`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
`token_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`token`  blob NULL ,
`authentication`  blob NULL ,
INDEX `token_id_index` (`token_id`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=DYNAMIC
;
