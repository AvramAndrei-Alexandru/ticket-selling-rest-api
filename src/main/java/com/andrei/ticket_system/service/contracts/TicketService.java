package com.andrei.ticket_system.service.contracts;


import com.andrei.ticket_system.entity.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> getAll();
    Ticket getById(int id);
    boolean saveOrUpdate(Ticket ticket);
    boolean delete(int id);
    boolean existsTicketsForConcert(int concertId);
    List<Ticket> getAllByConcertId(int id);
}
