package com.andrei.ticket_system.service;

import com.andrei.ticket_system.entity.Concert;
import com.andrei.ticket_system.repository.ConcertRepository;
import com.andrei.ticket_system.service.contracts.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConcertServiceImpl implements ConcertService {

    private ConcertRepository concertRepository;

    public ConcertServiceImpl(@Autowired ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Override
    public List<Concert> getAll() {
        return concertRepository.findAll();
    }

    @Override
    public Concert getById(int id) {
        Optional<Concert> concert =  concertRepository.findById(id);
        return concert.orElse(null);
    }

    @Override
    public boolean saveOrUpdate(Concert concert) {
        if(!existsByArtistAndGenreAndDateAndTimeAndName(concert.getArtist(), concert.getGenre(), concert.getDate(), concert.getTime(), concert.getName())) {
            concertRepository.save(concert);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        if(getById(id) != null) {
            concertRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByArtistAndGenreAndDateAndTimeAndName(String artist, String genre, Date date, Time time, String name) {
        return concertRepository.existsByArtistAndGenreAndDateAndTimeAndName(artist, genre,  date,  time, name);
    }

    @Override
    public boolean modifyNumberOfMaximumTicketsAvailable(int numberOfTicketsToBeSold, int concertId) {
        Concert concert = getById(concertId);
        if(concert != null) {
            int numberOfAvailableTickets = concert.getMaximumNumberOfTickets();
            if(numberOfTicketsToBeSold <= numberOfAvailableTickets) {
                concert.setMaximumNumberOfTickets(numberOfAvailableTickets - numberOfTicketsToBeSold);
                saveOrUpdate(concert);
                return true;
            }
        }
        return false;
    }

    @Override
    public void addTicketsBack(int numberOfPlacesRefunded, int concertId) {
        Concert concert = getById(concertId);
        if(concert != null) {
            concert.setMaximumNumberOfTickets(concert.getMaximumNumberOfTickets() + numberOfPlacesRefunded);
            saveOrUpdate(concert);
        }
    }

    @Override
    public Concert getByName(String name) {
        return concertRepository.getByName(name);
    }


}
