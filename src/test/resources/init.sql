CREATE TABLE IF NOT EXISTS pets (
    id INT(64) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS photos (
    id INT(64) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    pet_id INT(64),
    url TEXT NOT NULL
);

TRUNCATE TABLE photos;
TRUNCATE TABLE pets;