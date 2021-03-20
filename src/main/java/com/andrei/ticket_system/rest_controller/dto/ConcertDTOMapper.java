package com.andrei.ticket_system.rest_controller.dto;

import com.andrei.ticket_system.entity.Concert;

import java.util.Calendar;

public class ConcertDTOMapper {
    public static Concert convertConcertDTOToConcertCreateMode(ConcertDTO concertDTO) {

        if(isConcertDTOValid(concertDTO)) {
            Concert concert = new Concert();
            concert.setId(0);
            concert.setName(concertDTO.getName());
            concert.setArtist(concertDTO.getArtist());
            concert.setDate(concertDTO.getDate());
            concert.setTime(concertDTO.getTime());
            concert.setGenre(concertDTO.getGenre());
            concert.setMaximumNumberOfTickets(concertDTO.getMaximumNumberOfTickets());
            return concert;
        }
       return null;
    }

    private static boolean isConcertDTOValid(ConcertDTO concertDTO) {
        if(concertDTO.getArtist().equals("") || concertDTO.getDate().before(Calendar.getInstance().getTime()) ||
        concertDTO.getGenre().equals("") || concertDTO.getMaximumNumberOfTickets() <= 0 || concertDTO.getName().equals("")) {
            return false;
        }
        return true;
    }

}
