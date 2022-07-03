# java-filmorate
Template repository for Filmorate project.

Файл с картинкой ER-схемы БД Filmorate находится в ветке add-friends-likes в папке 
java-filmorate/src/main/resources/QuickDBD-export.png

Я приняла решение хранить в отдельных таблицах данные пользователей (USER) и данные о фильмах (FILM).

Так же в отдельных таблицах хранится рейтинг фильма (RATING), жанр фильма (GENRE) и статус дружбы (STATUS_OF_FRIEND) - это позволит просто заменять названия рейтингов, жанров   и статуса.

Таблица USER_FRIEND хранит данные о проведенных и подтвержденных пользователем запросов на дружбу. Ключом выступает идентификатор записи.
Реализация взаимной дружбы будет происходить на уровне приложения. При поступлении запроса на дружбу будет формироваться две строки в таблице USER_FRIEND:
          - одна о дружбе между User1 и User2,
          - вторая о дружбе между User2 и User1.
          
Таблица FILM_LIKE будет соединительной для таблиц FILM и USER. В этой таблице связывается идентификатор понравившегося фильма с идентификатором пользователя, причём каждая такая пара будет уникальной (это составной ключ).

Примеры запросов:
 - получение всех фильмов 
 SELECT name
 FROM film
 
 - получение всех пользователей
 SELECT name
 FROM user
 
 -получение 10 наиболее популярных фильмов
 SELECT f.name,
        COUNT (fl.user_id)
 FROM film_like AS fl LEFT INNER JOIN film AS f ON fl.film_id = f.film_id
 GROUP BY fl.film_id
 ORDER BY COUNT(fl.user_id) DESC
 LIMIT 10
 
 -получение списка общих друзей для user1(id=1) и user2(id=2)
 SELECT u.name
 FROM user_friend AS uf
 LEFT OUTER JOIN user AS u ON u.user_id = uf.user_friend_id
 WHERE uf.user_id = 1
 AND user_friend_id IN (SELECT uf.user_friend_id
                          FROM uf
                          WHERE uf.user_id = 2)
 
 
 
 
 

