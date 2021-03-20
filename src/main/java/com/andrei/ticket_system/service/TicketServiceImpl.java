package com.andrei.ticket_system.service;

import com.andrei.ticket_system.entity.Ticket;
import com.andrei.ticket_system.repository.TicketRepository;
import com.andrei.ticket_system.service.contracts.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;

    public TicketServiceImpl(@Autowired TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getById(int id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public boolean saveOrUpdate(Ticket ticket) {
        ticketRepository.save(ticket);
        return true;
    }

    @Override
    public boolean delete(int id) {
        Ticket ticket = getById(id);
        if(ticket != null) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsTicketsForConcert(int concertId) {
        return ticketRepository.existsByConcertId(concertId);
    }

    @Override
    public List<Ticket> getAllByConcertId(int id) {
        return ticketRepository.getAllByConcertIdOrderByOwner(id);
    }
}
