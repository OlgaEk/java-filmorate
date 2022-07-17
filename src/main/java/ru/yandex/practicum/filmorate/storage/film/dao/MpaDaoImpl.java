package ru.yandex.practicum.filmorate.storage.film.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAllMpa(){
        final String sql = "Select * From mpa";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    public Mpa getMpa(Integer id){
        final String sql = "Select * From mpa WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa,id);
    }

    public boolean containsMpaId(Integer id) {
        final String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.queryForList(sql, id).size()>0;
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
        return mpa;
    }


}
