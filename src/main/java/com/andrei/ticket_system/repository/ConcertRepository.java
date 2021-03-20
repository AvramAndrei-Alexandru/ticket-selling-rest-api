package com.andrei.ticket_system.repository;

import com.andrei.ticket_system.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Integer> {
    boolean existsByArtistAndGenreAndDateAndTimeAndName(String artist, String genre, Date date, Time time, String name);
    Concert getByName(String name);
}
