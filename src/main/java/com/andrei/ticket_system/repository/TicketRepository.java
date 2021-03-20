package com.andrei.ticket_system.repository;

import com.andrei.ticket_system.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    boolean existsByConcertId(int concertId);
    List<Ticket> getAllByConcertIdOrderByOwner(int id);
}
