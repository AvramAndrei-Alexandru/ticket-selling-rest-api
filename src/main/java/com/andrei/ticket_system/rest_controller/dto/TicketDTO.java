package com.andrei.ticket_system.rest_controller.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
public class TicketDTO {

    private int id;
    private int concertId;
    private String ticketOwner;
    private int numberOfPlaces;
    private String concertName;
    private String artist;
    private String genre;
    private Date concertDate;
    private Time concertTime;
}
