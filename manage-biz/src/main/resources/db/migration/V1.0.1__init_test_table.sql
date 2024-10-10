CREATE TABLE commonframework.test_table (
	id BIGINT auto_increment NOT NULL,
	tenant_code varchar(100) NOT NULL,
	test_name varchar(100) NOT NULL,
	test_code varchar(100) NOT NULL,
	CONSTRAINT id_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
