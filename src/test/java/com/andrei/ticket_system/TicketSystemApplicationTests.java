package com.andrei.ticket_system;

import com.andrei.ticket_system.entity.Concert;
import com.andrei.ticket_system.entity.Ticket;
import com.andrei.ticket_system.rest_controller.dto.TicketDTO;
import com.andrei.ticket_system.service.contracts.ConcertService;
import com.andrei.ticket_system.service.contracts.TicketService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketSystemApplicationTests {

	@LocalServerPort
	private int port;

	private static final String concertName = "testing2021";
	private static final int numberOfTicketsAvailable = 100;

	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private TicketService ticketService;
	@Autowired
	private ConcertService concertService;


	@Test
	void contextLoads() {
	}



	@Test
	void testSecurityWrongCredentials() {
		ResponseEntity<String> result = testRestTemplate.withBasicAuth("Ana", "ana")
				.getForEntity("/api/users/getAll", String.class);
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	@Test
	void testSecurityCorrectCredentials() {
		ResponseEntity<String> result = testRestTemplate.withBasicAuth("Andrei", "andrei")
				.getForEntity("/api/users/getAll", String.class);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	void testTicketLimit() {
		setUpTestData();
		Concert concert = concertService.getByName(concertName);
		assertEquals(numberOfTicketsAvailable, concert.getMaximumNumberOfTickets());
		Ticket ticket = getTicket(numberOfTicketsAvailable - 20, concert);
		ResponseEntity<TicketDTO> result = testRestTemplate.withBasicAuth("Andrei", "andrei")
				.postForEntity("/api/tickets/sellTicket", ticket, TicketDTO.class);
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertEquals(numberOfTicketsAvailable - ticket.getNumberOfPlaces(), concertService.getByName(concertName).getMaximumNumberOfTickets());
		Ticket ticket1 = getTicket(20, concert);
		ResponseEntity<TicketDTO> result1 = testRestTemplate.withBasicAuth("Andrei", "andrei")
				.postForEntity("/api/tickets/sellTicket", ticket1, TicketDTO.class);
		assertEquals(HttpStatus.CREATED, result1.getStatusCode());
		assertEquals(concertService.getByName(concertName).getMaximumNumberOfTickets(), 0);
		Ticket ticket2 = getTicket(20, concert);
		ResponseEntity<TicketDTO> result2 = testRestTemplate.withBasicAuth("Andrei", "andrei")
				.postForEntity("/api/tickets/sellTicket", ticket2, TicketDTO.class);
		assertEquals(HttpStatus.NOT_ACCEPTABLE, result2.getStatusCode());
		assertEquals(concertService.getByName(concertName).getMaximumNumberOfTickets(), 0);
		removeTestData();
	}

	private void setUpTestData() {
		Concert concert = new Concert();
		concert.setArtist("test");
		concert.setName(concertName);
		concert.setGenre("test");
		concert.setMaximumNumberOfTickets(numberOfTicketsAvailable);
		concert.setTime(Time.valueOf("12:12:12"));
		concert.setDate(Date.valueOf("2021-08-12"));
		concertService.saveOrUpdate(concert);
	}


	private void removeTestData() {
		Concert concert = concertService.getByName(concertName);
		if(concert != null) {
			List<Ticket> tickets = ticketService.getAllByConcertId(concert.getId());
			tickets.forEach(ticket -> ticketService.delete(ticket.getId()));
			concertService.delete(concert.getId());
		}
	}

	private Ticket getTicket(int numberOfPlaces, Concert concert) {
		Ticket ticket = new Ticket();
		ticket.setConcertId(concert.getId());
		ticket.setNumberOfPlaces(numberOfPlaces);
		ticket.setOwner(RandomString.make(10));
		ticket.setId(0);
		return ticket;
	}

}
