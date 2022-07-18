package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendshipDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component("userDbStorage")
@Primary
public class UserDbStorage implements UserStorage{
    JdbcTemplate jdbcTemplate;
    FriendshipDao friendshipDao;

    @Autowired
    public UserDbStorage (JdbcTemplate jdbcTemplate, FriendshipDao friendshipDao){

        this.jdbcTemplate = jdbcTemplate;
        this.friendshipDao = friendshipDao;
    }

    @Override
    public User add(User user) {
        final String sqlQuery = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3,user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User delete(User user) {
        final String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql,user.getId());
        return user;

    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
                );
        return user;
    }

    @Override
    public List<User> getAll() {
        final String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User get(Long id) {
        try {
            final String sql = "SELECT * FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NoSuchUserIdException("Пользователь по ID = " + id + " не найден");
        }
    }

    @Override
    public boolean containsUserId(Long id) {
        final String sql = "SELECT name FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, id).size()>0;
    }

    //Сразу не поняла как обновлять статус. Потом нашла вариант обновления в slack
    //Обновление статуса : Если User1 добавляет в друзья User2 статус дружбы User1->User2 "Запрошено"
    // Если потом User2 добавляет User1 в друзья статус дружбы  изменяется на
    // User1->User2 "Подтверждено" и User2->User1 "Подтвержденно"
    // Выполнена вся необходимая подготовка для внесения в бизнес-логику

    @Override
    public boolean addFriend(Long id, Long idFriend) {
        return friendshipDao.addFriend(id,idFriend);
    }

    @Override
    public boolean deleteFriend(Long id, Long idFriend) {
        return friendshipDao.deleteFriend(id,idFriend);
    }

    @Override
    public List<User> getFriends(Long id) {
        return friendshipDao.getFriends(id).stream().
                map(this::get)
                .collect(Collectors.toList());
    }


    @Override
    public List<User> commonFriends(Long id, Long otherId) {
        return friendshipDao.commonFriends(id,otherId).stream().
                map(this::get)
                .collect(Collectors.toList());
    }


    @Override
    public Integer statusOfFriendship(Long id, Long idFriend){
        return friendshipDao.statusOfFriendship(id,idFriend);
    }




    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
