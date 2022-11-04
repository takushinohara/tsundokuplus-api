CREATE TABLE user(
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    created_at datetime,
    updated_at datetime,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE book(
    id int NOT NULL AUTO_INCREMENT,
    user_id int NOT NULL,
    title varchar(255) NOT NULL,
    author varchar(255),
    publisher varchar(255),
    thumbnail varchar(255),
    small_thumbnail varchar(255),
    created_at datetime,
    updated_at datetime,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE note(
    id int NOT NULL AUTO_INCREMENT,
    book_id int NOT NULL,
    contents varchar(2048),
    created_at datetime,
    updated_at datetime,
    PRIMARY KEY (id),
    FOREIGN KEY (book_id) REFERENCES book(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
