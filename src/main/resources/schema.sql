CREATE TABLE db_config (
  id   INTEGER      NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  data DOUBLE NOT NULL,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX unique_name ON db_config (name);