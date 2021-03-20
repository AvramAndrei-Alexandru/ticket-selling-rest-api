package com.andrei.ticket_system.rest_controller;

import com.andrei.ticket_system.entity.Concert;
import com.andrei.ticket_system.entity.Ticket;
import com.andrei.ticket_system.util.csv_export.CSVDocumentCreator;
import com.andrei.ticket_system.util.csv_export.DocumentCreator;
import com.andrei.ticket_system.rest_controller.dto.TicketDTO;
import com.andrei.ticket_system.rest_controller.dto.TicketDTOMapper;
import com.andrei.ticket_system.service.contracts.ConcertService;
import com.andrei.ticket_system.service.contracts.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {

    private TicketService ticketService;
    private ConcertService concertService;

    public TicketRestController(@Autowired TicketService ticketService, @Autowired ConcertService concertService) {
        this.ticketService = ticketService;
        this.concertService = concertService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<TicketDTO>> getAll() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "getAll");
        List<Ticket> ticketList = ticketService.getAll();
        List<TicketDTO> concertDTOs = ticketList.stream()
                .map(ticket -> TicketDTOMapper.getTicketDTOMapping(ticket, concertService.getById(ticket.getConcertId())))
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(concertDTOs);
    }

    @GetMapping(value = "/getAllTicketsForConcert/{id}")
    public ResponseEntity<List<TicketDTO>> getAllTicketsForConcert(@PathVariable int id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "getAllTicketsForConcert");
        List<Ticket> ticketList = ticketService.getAllByConcertId(id);
        List<TicketDTO> concertDTOs = ticketList.stream()
                .map(ticket -> TicketDTOMapper.getTicketDTOMapping(ticket, concertService.getById(ticket.getConcertId())))
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(concertDTOs);
    }

    @PostMapping(value = "/sellTicket", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TicketDTO> sellTicket(@RequestBody Ticket ticket) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "sellTicket");
        Concert wantedConcert = concertService.getById(ticket.getConcertId());
        if(wantedConcert != null) {
            if(concertService.modifyNumberOfMaximumTicketsAvailable(ticket.getNumberOfPlaces(), wantedConcert.getId())) {
                ticket.setId(0);
                ticketService.saveOrUpdate(ticket);
                TicketDTO ticketDTO = TicketDTOMapper.getTicketDTOMapping(ticket, wantedConcert);
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .headers(httpHeaders)
                        .body(ticketDTO);
            }
        }
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .headers(httpHeaders)
                .body(null);
    }

    @PutMapping(value = "/editTicket/{id}")
    public ResponseEntity<TicketDTO> editTicket(@PathVariable int id, @RequestBody Ticket ticket) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "editTicket");
        Ticket existingTicket = ticketService.getById(id);
        if(existingTicket != null) {
            //The user can only increase the number of seats sold for a concert
            if(ticket.getOwner().equals(existingTicket.getOwner()) &&
                    ticket.getConcertId() == existingTicket.getConcertId() &&
                    ticket.getNumberOfPlaces() > existingTicket.getNumberOfPlaces() &&
                    concertService.modifyNumberOfMaximumTicketsAvailable(ticket.getNumberOfPlaces() - existingTicket.getNumberOfPlaces(), ticket.getConcertId())) {
                ticket.setId(id);
                ticketService.saveOrUpdate(ticket);
                TicketDTO ticketDTO = TicketDTOMapper.getTicketDTOMapping(ticket, concertService.getById(ticket.getConcertId()));
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(httpHeaders)
                        .body(ticketDTO);
            }
        }
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .headers(httpHeaders)
                .body(null);
    }

    @DeleteMapping(value = "/deleteTicket/{id}")
    public ResponseEntity<Boolean> deleteTicket(@PathVariable int id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "deleteTicket");
        Ticket ticket = ticketService.getById(id);
        if(ticket != null) {
            concertService.addTicketsBack(ticket.getNumberOfPlaces(), ticket.getConcertId());
            ticketService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body(true);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(httpHeaders)
                .body(false);
    }

    @GetMapping(value = "/export/{id}")
    public ResponseEntity<Void> exportTicketsToCSV(@PathVariable int id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "exportTicketsToCSV");
        List<Ticket> ticketsSold = ticketService.getAllByConcertId(id);
        if(ticketsSold != null && ticketsSold.size() != 0) {
            List<TicketDTO> concertDTOs = ticketsSold.stream()
                    .map(ticket -> TicketDTOMapper.getTicketDTOMapping(ticket, concertService.getById(ticket.getConcertId())))
                    .collect(Collectors.toList());
            DocumentCreator<TicketDTO> ticketReportGenerator = new CSVDocumentCreator<>();
            ticketReportGenerator.getDocument().generateDocument(concertDTOs);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .headers(httpHeaders)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(httpHeaders)
                .build();

    }
}
