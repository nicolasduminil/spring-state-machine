DROP TABLE STATE_MACHINE;
CREATE TABLE STATE_MACHINE
(
  MACHINE_ID VARCHAR2(255 CHAR) NOT NULL ENABLE,
  STATE VARCHAR2(255 CHAR),
  STATE_MACHINE_CONTEXT BLOB,
  PRIMARY KEY (MACHINE_ID)
);