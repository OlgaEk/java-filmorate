package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private HashMap<Long, Film> filmBase = new HashMap<>();
    private HashMap<Long, Set<Long>> filmLikes = new HashMap<>();
    private Long LastAssignedId = 0l;

    // Я приняла решение не использовать отсортированный TreeMap при хранении лайков к фильмам:
    //  - я считаю логичным хранение : ключ - это Id фильма,  а значение - это Id пользователей, которые поставили лайк;
    //  - сортировка TreeMap подразумевает именно сортировку по ключу (найденные мною способы сортировки по значению,
    //   я посчитала избыточным по ресурсам( способ перкладывания в другой TreeSet или LinkedTreeSet для сортировки)
    //   и все мои попытки написать логичный comparator не увенчались успехом((;
    //   - и я приняла решение, что сортировка именно выдываемого списка при помощи stream() очень крависвое
    //   и понятное решение;
    //   - при наличии в ТЗ условия, что рейтинг фильмов будет запрашиваться регулярно, я бы избыточно создала
    //     TreeMap с ключом - рейтингом и значание ID фильма
    // Мне интересно Ваше мнение насчет принятого мною решения. Спасибо.
    public List<Film> getSortedByLikesFilm(Integer count) {
        return filmLikes.entrySet()
                .stream()
                .sorted((e1,e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .map(Map.Entry::getKey)
                .limit(count)
                .map(filmBase::get)
                .collect(Collectors.toList());
    }

    public Film add(Film film ){
        film.setId(++LastAssignedId);
        filmBase.put(LastAssignedId,film);
        filmLikes.put(film.getId(),new HashSet<Long>());
        log.info("Фильм {} добавлен в базу",film);
        return film;
    }

    public Film delete(Film film){
        filmBase.remove(film.getId());
        filmLikes.remove(film.getId());
        log.info("Фильм {} удален из базы",film);
        return film;
    }

    public Film update(Film film){
        filmBase.put(film.getId(),film);
        log.info("Фильм {} обновлен",film);
        return film;
    }

    public Film get (Long id){
        return filmBase.get(id);
    }


    public List<Film> getAll(){
        return filmBase.values().stream().collect(Collectors.toList());
    }

    public boolean containsFilmId (Long id){
        return filmBase.containsKey(id);
    }

    public boolean addLike (Long idFilm, Long IdUser){
        return filmLikes.get(idFilm).add(IdUser);
    }

    public boolean deleteLike (Long idFilm, Long idUser){
        return filmLikes.get(idFilm).remove(idUser);
    }

    public Map<Long, Set<Long>> getAllLikes (){
        return filmLikes;
    }

}
