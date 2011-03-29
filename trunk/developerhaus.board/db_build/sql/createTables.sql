DROP TABLE POST IF EXISTS CASCADE;
DROP TABLE USERS IF EXISTS CASCADE;
DROP TABLE FILE IF EXISTS CASCADE;
DROP TABLE POST_FILE IF EXISTS CASCADE;

CREATE TABLE POST 
(	
	POST_SEQ INTEGER primary key,
	TITLE VARCHAR(50) NOT NULL,
	CONTENTS VARCHAR(4000),
	REG_USR INTEGER NOT NULL,
	REG_DT DATE NOT NULL,
	MOD_USR INTEGER,
	MOD_DT DATE,
	UP_POST_SEQ INTEGER,
	ODR INTEGER,
	LVL INTEGER,
	CONSTRAINT UP_POST_SEQ_FK_POST_SEQ FOREIGN KEY(UP_POST_SEQ) REFERENCES POST(POST_SEQ)
);

CREATE TABLE USERS 
(	
	SEQ INTEGER primary key,
	ID VARCHAR(50) NOT NULL,
	NAME VARCHAR(100) NOT NULL,
	PASSWORD VARCHAR(100) NOT NULL
);

CREATE TABLE FILE
(
	FILE_SEQ INTEGER primary key,
	FILE_NM VARCHAR(100) NOT NULL,
	FILE_PTH VARCHAR(100) NOT NULL,
	FILE_EXT VARCHAR(100) NOT NULL,
	FILE_SZ INTEGER DEFAULT 0,
	REG_USR INTEGER NOT NULL,
	REG_DT DATE NOT NULL
);

CREATE TABLE POST_FILE
(
	POST_SEQ INTEGER NOT NULL,
	FILE_SEQ INTEGER NOT NULL
);