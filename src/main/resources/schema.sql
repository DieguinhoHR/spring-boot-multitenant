CREATE TABLE USUARIO (
	id IDENTITY PRIMARY KEY,
	email VARCHAR(255),
	senha VARCHAR(200),
	perfil CHAR(1)
);

CREATE TABLE DATASOURCECONFIG (
	id IDENTITY PRIMARY KEY,
	driverclassname VARCHAR(255),
	url VARCHAR(255),
	name VARCHAR(255),
	username VARCHAR(255),
	password VARCHAR(255),
	tenant_id INT,
	initialize BOOLEAN,
  FOREIGN KEY (tenant_id)
  REFERENCES USUARIO(id)
);

CREATE TABLE PRODUCT (
	id IDENTITY PRIMARY KEY,
	name VARCHAR(255)
);
