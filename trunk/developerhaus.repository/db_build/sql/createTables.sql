DROP TABLE USERS IF EXISTS CASCADE;

CREATE TABLE USERS 
(	
	SEQ INTEGER primary key,
	ID VARCHAR(50) NOT NULL,
	NAME VARCHAR(100) NOT NULL,
	PASSWORD VARCHAR(100) NOT NULL
);
