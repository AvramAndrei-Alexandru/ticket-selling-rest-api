package com.andrei.ticket_system.util.data_generator;

import com.andrei.ticket_system.entity.Concert;
import com.andrei.ticket_system.entity.Ticket;
import com.andrei.ticket_system.entity.User;
import com.andrei.ticket_system.service.contracts.ConcertService;
import com.andrei.ticket_system.service.contracts.TicketService;
import com.andrei.ticket_system.service.contracts.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Date;
import java.util.*;


@Component
public class DataGenerator {
    @Bean
    CommandLineRunner init(ConcertService concertService, TicketService ticketService, UserService userService) {
        return args -> {
            Random random = new Random();
            User admin = new User();
            admin.setLogin("Andrei");
            admin.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("andrei"));
            admin.setId(0);
            admin.setRole("ROLE_ADMIN");
            userService.saveOrUpdate(admin);
            User cashier = new User();
            cashier.setLogin("Ana");
            cashier.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("ana"));
            cashier.setId(0);
            cashier.setRole("ROLE_CASHIER");
            userService.saveOrUpdate(cashier);
            List<String> artists = new ArrayList<>(List.of("ARMIN VAN BUUREN", "AFROJACK", "HARDWELL", "STEVE AOKI", "MARTIN GARRIX", "MARSHMELLO"));
            List<String> concertTIme = new ArrayList<>(List.of("02:20:00", "23:00:00", "19:00:00", "21:30:00", "17:20:00", "05:00:00"));
            List<String> concertDate = new ArrayList<>(List.of("2021-08-01", "2021-08-01", "2021-08-02", "2021-08-02", "2021-08-03", "2021-08-03"));
            List<String> genre = new ArrayList<>(List.of("EDM", "TRANCE"));
            List<String> concertName = new ArrayList<>(List.of("GREAT SPIRIT", "ROCK THE HOUSE", "YOUNG AGAIN", "MAMBO", "FEELING ME", "ALONE"));
            Map<String,String> artistConcertName = new HashMap<>();
            artists.forEach(a -> artistConcertName.put(a, concertName.get(artists.indexOf(a))));
            Map<String,String> artistGenre = new HashMap<>();
            artists.forEach(a -> artistGenre.put(a, genre.get(random.nextInt(2))));
            artists.forEach(artist -> {
                Concert concert = new Concert();
                concert.setId(0);
                concert.setArtist(artist);
                concert.setGenre(artistGenre.get(artist));
                concert.setName(artistConcertName.get(artist));
                concert.setMaximumNumberOfTickets(random.nextInt(25001-15000) + 15000);
                concert.setTime(Time.valueOf(concertTIme.get(artists.indexOf(artist))));
                concert.setDate(Date.valueOf(concertDate.get(artists.indexOf(artist))));
                concertService.saveOrUpdate(concert);
            });
            List<Concert> concerts = concertService.getAll();
            List<String> clients = new ArrayList<>(
                    List.of("Decebal Luminița", "Valerian Darius", "Amalia Beniamin", "Mihăiță Otilia", "Flaviu Eusebiu", "Romeo Adriana", "Traian Claudia", "Teodor Daciana", "Daria David"));
            clients.forEach(client ->{
                Ticket ticket = new Ticket();
                ticket.setId(0);
                ticket.setOwner(client);
                int concertID = concerts.get(random.nextInt(concerts.size())).getId();
                int numberOfPlaces = random.nextInt(9) + 1;
                ticket.setConcertId(concertID);
                ticket.setNumberOfPlaces(numberOfPlaces);
                concertService.modifyNumberOfMaximumTicketsAvailable(numberOfPlaces, concertID);
                ticketService.saveOrUpdate(ticket);
            });
        };
    }
}
