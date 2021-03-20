package com.andrei.ticket_system.rest_controller.dto;

import com.andrei.ticket_system.entity.Concert;
import com.andrei.ticket_system.entity.Ticket;

public class TicketDTOMapper {
    public static TicketDTO getTicketDTOMapping(Ticket ticket, Concert concert) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setArtist(concert.getArtist());
        ticketDTO.setConcertDate(concert.getDate());
        ticketDTO.setConcertName(concert.getName());
        ticketDTO.setConcertTime(concert.getTime());
        ticketDTO.setGenre(concert.getGenre());
        ticketDTO.setNumberOfPlaces(ticket.getNumberOfPlaces());
        ticketDTO.setTicketOwner(ticket.getOwner());
        ticketDTO.setConcertId(concert.getId());
        ticketDTO.setId(ticket.getId());
        return ticketDTO;
    }
}
