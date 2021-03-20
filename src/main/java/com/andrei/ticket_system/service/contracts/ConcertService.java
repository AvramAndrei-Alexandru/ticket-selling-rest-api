package com.andrei.ticket_system.service.contracts;


import com.andrei.ticket_system.entity.Concert;


import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface ConcertService {
    List<Concert> getAll();
    Concert getById(int id);
    boolean saveOrUpdate(Concert concert);
    boolean delete(int id);
    boolean existsByArtistAndGenreAndDateAndTimeAndName(String artist, String genre, Date date, Time time, String name);
    boolean modifyNumberOfMaximumTicketsAvailable(int numberOfTicketsToBeSold, int concertId);
    void addTicketsBack(int numberOfPlacesRefunded, int concertId);
    Concert getByName(String name);
}
