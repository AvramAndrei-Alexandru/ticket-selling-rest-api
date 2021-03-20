package com.andrei.ticket_system.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "concert")
@Data
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String artist;
    private String genre;
    private Date date;
    private Time time;
    private int maximumNumberOfTickets;

}
