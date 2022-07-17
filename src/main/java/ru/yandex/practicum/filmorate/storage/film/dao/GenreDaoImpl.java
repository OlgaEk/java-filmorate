package ru.yandex.practicum.filmorate.storage.film.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenre(){
        final String sql = "Select * From genre";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    public Genre getGenre(Integer id){
        final String sql = "Select * From genre WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre,id);
    }

    public void addGenres (Film film){
        final String sql = "MERGE INTO film_genre (film_id,genre_id) VALUES (?, ?)";
        film.getGenres().stream()
                .forEach((k)-> {
                    jdbcTemplate.update(sql,film.getId(),k.getId());
                    k.setName(getGenre(k.getId()).getName());
                });
    }
    public void removeGenres(Film film){
        final String sql = "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?";
        film.getGenres().stream()
                .forEach((k)-> jdbcTemplate.update(sql,film.getId(),k.getId()));
    }

    public List<Genre> getGenreByFilmID(Long id){
        final String sql = "SELECT * FROM film_genre AS f " +
                "LEFT JOIN genre AS g ON f.genre_id = g.genre_id " +
                "WHERE film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, this::mapRowToGenre, id);
        if (genres.size() == 0){
            return new ArrayList<>();
        }
        return genres;
    }

    public boolean containsGenreId(Integer id) {
        final String sql = "SELECT * FROM genre WHERE genre_id = ?";
        return jdbcTemplate.queryForList(sql, id).size()>0;
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
        return genre;
    }
}
