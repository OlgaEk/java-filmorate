package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaDao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("filmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,GenreDao genreDao,MpaDao mpaDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
    }

    // Сейчас проверка на корректность MPA и genre выполняется внутри класса FilmDbStorage
    // Для текущего задания этого мне кажется достаточно, но при расширении функционала считаю
    // эти валидации надо перенести в класс Film как кастомные валидации

    @Override
    public Film add(Film film) {
        if(film.getGenres() != null){
            film.getGenres().stream()
                    .forEach((k)-> {
                        if(!genreDao.containsGenreId(k.getId())){
                            throw new NoSuchGenreIdException("Жанр по ID = " + k.getId() + " не найден");
                        }
                    });
        }

        if(!mpaDao.containsMpaId(film.getMpa().getId())){
            throw new NoSuchMpaIdException("MPA по ID = " + film.getMpa().getId() + " не найден");
        }
        final String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa) " +
                "VALUES (?, ?, ?, ? ,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        film.setMpa(mpaDao.getMpa(film.getMpa().getId()));
        if(film.getGenres() != null) genreDao.addGenres(film);
        return film;
    }

    @Override
    public Film get(Long id) {
        final String sql = "SELECT * FROM films AS f " +
                "LEFT JOIN mpa AS m ON f.mpa = m.mpa_id " +
                "WHERE film_id = ?";
         try {
             Film film = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
             film.setGenres(genreDao.getGenreByFilmID(id));
             return film;
         } catch (EmptyResultDataAccessException ex){
             throw new NoSuchFilmIdException("Фильм по ID = " + id + " не найден");
         }
    }

    @Override
    public Film delete(Film film) {
        final String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql,film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        Film filmInBase = get(film.getId());
        if(!filmInBase.getGenres().equals(film.getGenres()) && !filmInBase.getGenres().isEmpty()) {
            genreDao.removeGenres(filmInBase);
        }
        if(!mpaDao.containsMpaId(film.getMpa().getId())){
            throw new NoSuchMpaIdException("MPA по ID = " + film.getMpa().getId() + " не найден");
        }
        String sql = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sql,
                 film.getName(),
                 film.getDescription(),
                 film.getReleaseDate(),
                 film.getDuration(),
                 film.getMpa().getId(),
                 film.getId());
        if(film.getGenres() != null) genreDao.addGenres(film);
        film.setGenres(genreDao.getGenreByFilmID(film.getId()));
        film.setMpa(mpaDao.getMpa(film.getMpa().getId()));
        return film;
    }

    @Override
    public List<Film> getAll() {
        final String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public boolean containsFilmId(Long id) {
        final String sql = "SELECT name FROM films WHERE film_id = ?";
        return jdbcTemplate.queryForList(sql, id).size()>0;
    }

    @Override
    public boolean addLike(Long idFilm, Long IdUser) {
        final String sql = "INSERT INTO film_like (film_id,user_id) VALUES ( ?, ? )";
        try {
            return jdbcTemplate.update(sql,idFilm,IdUser) > 0;
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
    public List<Film> getSortedByLikesFilm(Integer count) {
        final String sql = "SELECT f.film_id " +
                "FROM films AS f LEFT JOIN film_like AS fl ON f.film_id = fl.film_id " +
                "GROUP BY f.film_id ORDER BY COUNT(user_id) DESC LIMIT ?";
        List<Long> result = new ArrayList<>();
        jdbcTemplate.query(sql,(rs, rowNum) -> result.add(rs.getLong("films.film_id")), count);
        return result.stream().
                map(this::get)
                .collect(Collectors.toList());
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(rs.getInt("mpa"))
                .build();
        Film film =  Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .build();
        mpa.setName(mpaDao.getMpa(mpa.getId()).getName());
        return film;
    }
}