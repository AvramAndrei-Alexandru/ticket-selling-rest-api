package com.andrei.ticket_system.rest_controller;

import com.andrei.ticket_system.entity.Concert;
import com.andrei.ticket_system.rest_controller.dto.ConcertDTO;
import com.andrei.ticket_system.rest_controller.dto.ConcertDTOMapper;
import com.andrei.ticket_system.service.contracts.ConcertService;
import com.andrei.ticket_system.service.contracts.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/concert")
public class ConcertRestController {

    private ConcertService concertService;
    private TicketService ticketService;

    public ConcertRestController(@Autowired ConcertService concertService, @Autowired TicketService ticketService) {
        this.concertService = concertService;
        this.ticketService = ticketService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Concert>> getConcerts() {
        List<Concert> concerts = concertService.getAll();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "getConcerts");
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(concerts);
    }

    @PostMapping(value = "/createConcert", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Concert> createConcert(@RequestBody ConcertDTO concertDTO) {
        Concert concert = ConcertDTOMapper.convertConcertDTOToConcertCreateMode(concertDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "createConcert");
        if(concert != null && concertService.saveOrUpdate(concert)) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .headers(httpHeaders)
                    .body(concert);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .headers(httpHeaders)
                .body(null);
    }

    @DeleteMapping(value = "/deleteConcert/{id}")
    public ResponseEntity<Boolean> deleteConcert(@PathVariable int id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "deleteConcert");
        if(ticketService.existsTicketsForConcert(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(httpHeaders)
                    .body(false);
        }
        boolean deleted = concertService.delete(id);
        if(deleted) {
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

    @PutMapping(value = "/editConcert/{id}")
    public ResponseEntity<Concert> editConcert(@RequestBody ConcertDTO concertDTO, @PathVariable int id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "editConcert");
        if(ticketService.existsTicketsForConcert(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(httpHeaders)
                    .body(null);
        }
        Concert existingConcert = concertService.getById(id);
        Concert concert = ConcertDTOMapper.convertConcertDTOToConcertCreateMode(concertDTO);
        if(existingConcert == null || concert == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(httpHeaders)
                    .body(null);
        }
        concert.setId(existingConcert.getId());
        concertService.saveOrUpdate(concert);
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(concert);
    }
}
