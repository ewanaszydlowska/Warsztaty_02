CREATE TABLE user_group(
	id INT AUTO_INCREMENT,
	name VARCHAR(255),
	PRIMARY KEY(id));

CREATE TABLE users(
	id BIGINT AUTO_INCREMENT,
	username VARCHAR(255),
	email VARCHAR(255) UNIQUE,
	password VARCHAR(245),
	user_group_id INT,
	PRIMARY KEY(id),
	FOREIGN KEY(user_group_id) REFERENCES user_group(id));
	
