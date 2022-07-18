package ru.yandex.practicum.filmorate.storage.film.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LikeFilmDaoImpl implements LikeFilmDao{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeFilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addLike(Long idFilm, Long idUser) {
        final String sql = "INSERT INTO film_like (film_id,user_id) VALUES ( ?, ? )";
        try {
            return jdbcTemplate.update(sql,idFilm,idUser) > 0;
        } catch (DuplicateKeyException ex){
            return false;
        }
    }

    @Override
    public boolean deleteLike(Long idFilm, Long idUser) {
        final String sql = "DELETE FROM film_like WHERE film_id = ? AND user_id = ? ";
        return jdbcTemplate.update(sql,idFilm,idUser) > 0;
    }

    @Override
    public List<Long> getSortedByLikesFilm(Integer count) {
        final String sql = "SELECT f.film_id " +
                "FROM films AS f LEFT JOIN film_like AS fl ON f.film_id = fl.film_id " +
                "GROUP BY f.film_id ORDER BY COUNT(user_id) DESC LIMIT ?";
        List<Long> result = new ArrayList<>();
        jdbcTemplate.query(sql,(rs, rowNum) -> result.add(rs.getLong("films.film_id")), count);
        return result;
    }
}
