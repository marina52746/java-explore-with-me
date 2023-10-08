package ru.practicum.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.hit.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.stat.StatDto(hit.app, hit.uri, count(hit)) " +
            "FROM Hit as hit " +
            "WHERE hit.timestamp " +
            "BETWEEN ?1 AND ?2 " +
            "AND hit.uri IN ?3 " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY count(hit) DESC")
    List<StatDto> findByStartEndUrisNotUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.stat.StatDto(hit.app, hit.uri, count(hit)) " +
            "FROM Hit as hit " +
            "WHERE hit.timestamp " +
            "BETWEEN ?1 AND ?2 " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY count(hit) DESC")
    List<StatDto> findByStartEndNotUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stat.StatDto(hit.app, hit.uri, count(distinct hit.ip)) " +
            "FROM Hit as hit " +
            "WHERE hit.timestamp " +
            "BETWEEN ?1 AND ?2 " +
            "AND hit.uri IN ?3 " +
            "ORDER BY count(distinct hit.ip) DESC")
    List<StatDto> findByStartEndUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stat.StatDto(hit.app, hit.uri, count(distinct hit.ip)) " +
            "FROM Hit as hit " +
            "WHERE hit.timestamp " +
            "BETWEEN ?1 AND ?2 " +
            "AND hit.uri IN ?3 " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY count(distinct hit.ip) DESC")
    List<StatDto> findByStartEndUrisUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris);
}
