-- data.sql
DROP TABLE IF EXISTS student;

CREATE TABLE student (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         age INT NOT NULL
);

INSERT INTO student (name, age) VALUES ('Alice', 20);
INSERT INTO student (name, age) VALUES ('Bob', 22);
INSERT INTO student (name, age) VALUES ('Charlie', 21);
