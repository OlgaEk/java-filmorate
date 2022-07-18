package ru.yandex.practicum.filmorate.storage.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FriendshipDaoImpl implements FriendshipDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addFriend(Long id, Long idFriend) {
        final String sqlHaveFriend = "SELECT * FROM user_friend WHERE user_id = ? AND user_friend_id = ?";
        SqlRowSet i = jdbcTemplate.queryForRowSet(sqlHaveFriend, id, idFriend);
        SqlRowSet j = jdbcTemplate.queryForRowSet(sqlHaveFriend , idFriend, id);
        if( i.next()  ) return false;
        if( j.next()  ){
            final String sqlUpdate = "UPDATE user_friend SET status_id = 1 WHERE user_id = ? AND user_friend_id = ?";
            jdbcTemplate.update(sqlUpdate,idFriend,id);
            final String sqlInsert = "INSERT INTO user_friend (user_id, user_friend_id,status_id) VALUES (?,?,1)";
            jdbcTemplate.update(sqlInsert , id, idFriend);
            return true;
        } else {
            final String sql = "INSERT INTO user_friend (user_id, user_friend_id,status_id) VALUES (?,?,0)";
            jdbcTemplate.update(sql, id, idFriend);
            return true;
        }
    }

    @Override
    public boolean deleteFriend(Long id, Long idFriend) {
        final String sql = "DELETE FROM user_friend WHERE user_id = ? AND user_friend_id = ? ";
        return jdbcTemplate.update(sql,id,idFriend) > 0;
    }

    @Override
    public List<Long> getFriends(Long id) {
        final String sql = "SELECT user_friend_id FROM user_friend WHERE user_id = ?";
        List<Long> result = new ArrayList<>();
        jdbcTemplate.query(sql,(rs, rowNum) -> result.add(rs.getLong("user_friend_id")), id);
        return result;
    }


    @Override
    public List<Long> commonFriends(Long id, Long otherId) {
        final String sql = "SELECT user_friend_id FROM user_friend " +
                "WHERE user_id = ? AND user_friend_id IN (SELECT user_friend_id FROM user_friend WHERE user_id = ?)";
        List<Long> result = new ArrayList<>();
        jdbcTemplate.query(sql,(rs, rowNum) -> result.add(rs.getLong("user_friend_id")), id,otherId);
        return result;
    }


    @Override
    public Integer statusOfFriendship(Long id, Long idFriend){
        final String sql = "SELECT status_id FROM user_friend WHERE user_id = ? AND user_friend_id = ?";
        return jdbcTemplate.query(sql,(rs, rowNum) -> rs.getInt("status_id"), id,idFriend).get(0);
    }
}
