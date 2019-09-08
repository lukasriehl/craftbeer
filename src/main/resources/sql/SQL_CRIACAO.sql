CREATE SCHEMA craft_beer;

USE craft_beer;
    
CREATE TABLE beer (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name varchar(60) NOT NULL,
    ingredients varchar(100),
    alcohol_content VARCHAR(80) NOT NULL,
    price NUMERIC(6,2) NOT NULL,
    category VARCHAR(80),
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
