DROP TABLE BOARD IF EXISTS CASCADE;
DROP TABLE USERS IF EXISTS CASCADE;

CREATE TABLE BOARD 
(	
	POST_SEQ INTEGER primary key,
	TITLE VARCHAR(50) NOT NULL,
	CONTENTS VARCHAR(4000),
	REG_USR INTEGER NOT NULL,
	REG_DT DATE NOT NULL,
	MOD_USR INTEGER,
	MOD_DT DATE
);

CREATE TABLE USERS 
(	
	SEQ INTEGER primary key,
	ID VARCHAR(50) NOT NULL,
	NAME VARCHAR(100) NOT NULL,
	PASSWORD VARCHAR(100) NOT NULL
);
