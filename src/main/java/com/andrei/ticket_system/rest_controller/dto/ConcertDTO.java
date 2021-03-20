package com.andrei.ticket_system.rest_controller.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
public class ConcertDTO {
    private String name;
    private String artist;
    private String genre;
    private Date date;
    private Time time;
    private int maximumNumberOfTickets;
}
