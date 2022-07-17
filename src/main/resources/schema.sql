DROP TABLE IF EXISTS mpa,genre,status_of_friend,user_friend,film_genre,film_like,films,users;

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id INTEGER   NOT NULL,
    mpa_name VARCHAR(50)   NOT NULL,
    CONSTRAINT "pk_MPA" PRIMARY KEY (mpa_id)
);

CREATE TABLE IF NOT EXISTS films (
    film_id LONG PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    release_date DATE,
    duration INTEGER,
    mpa INTEGER REFERENCES mpa(mpa_id),
    CONSTRAINT "pk_FILM" PRIMARY KEY (film_id)
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER   NOT NULL,
    genre_name VARCHAR(50)   NOT NULL,
    CONSTRAINT "pk_GENRE" PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id LONG REFERENCES films(film_id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genre(genre_id),
    CONSTRAINT "pk_FILM_GENRE" PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users (
        user_id LONG PRIMARY KEY AUTO_INCREMENT,
        email VARCHAR(100),
        login VARCHAR(100) NOT NULL ,
        name VARCHAR(100),
        birthday DATE,
        CONSTRAINT "pk_USER" PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS status_of_friend (
    status_id INTEGER  NOT NULL,
    status_name VARCHAR  NOT NULL,
    CONSTRAINT "pk_STATUS_OF_FRIEND" PRIMARY KEY (status_id)
);

CREATE TABLE IF NOT EXISTS user_friend (
    user_id LONG REFERENCES users(user_id) ON DELETE CASCADE,
    user_friend_id LONG REFERENCES users(user_id) ON DELETE CASCADE,
    status_id INTEGER REFERENCES status_of_friend(status_id) ,
    CONSTRAINT "pk_USER_Friend" PRIMARY KEY (user_id,user_friend_id)
);

CREATE TABLE IF NOT EXISTS film_like (
    film_id LONG  NOT NULL,
    user_id LONG  NOT NULL,
    CONSTRAINT "pk_FILM_LIKE" PRIMARY KEY (film_id , user_id)
);



