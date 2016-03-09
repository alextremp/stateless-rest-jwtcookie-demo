CREATE TABLE USERS (
	id INT PRIMARY KEY AUTO_INCREMENT, 
	username VARCHAR NOT NULL,
	password VARCHAR NOT NULL);

CREATE TABLE ROLES (
	id VARCHAR PRIMARY KEY,
	description VARCHAR);

CREATE TABLE USERS_ROLES (
	user_id INT NOT NULL,
	role_id VARCHAR NOT NULL,
	PRIMARY KEY (user_id, role_id),
	FOREIGN KEY(user_id) REFERENCES USERS(ID),
	FOREIGN KEY(role_id) REFERENCES ROLES(ID));


-- password is test
INSERT INTO USERS (username, password) VALUES ('user', '$2a$10$zNyCQEhWsMOQEChsGmGNq.gO7NU1RDL6vN3uwxZRWnLz/m9JVmFxy');
INSERT INTO USERS (username, password) VALUES ('admin', '$2a$10$zNyCQEhWsMOQEChsGmGNq.gO7NU1RDL6vN3uwxZRWnLz/m9JVmFxy');

INSERT INTO ROLES (id, description) VALUES ('ADMIN', 'Administrator');
INSERT INTO ROLES (id, description) VALUES ('USER', 'User');

INSERT INTO USERS_ROLES (user_id, role_id) SELECT id, 'USER' FROM USERS WHERE username='user';
INSERT INTO USERS_ROLES (user_id, role_id) SELECT id, 'USER' FROM USERS WHERE username='admin';
INSERT INTO USERS_ROLES (user_id, role_id) SELECT id, 'ADMIN' FROM USERS WHERE username='admin';

