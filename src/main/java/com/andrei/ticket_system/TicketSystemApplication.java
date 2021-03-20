package com.andrei.ticket_system;

import com.andrei.ticket_system.entity.Concert;
import com.andrei.ticket_system.entity.User;
import com.andrei.ticket_system.util.csv_export.CSVDocumentCreator;
import com.andrei.ticket_system.util.csv_export.Document;
import com.andrei.ticket_system.util.csv_export.DocumentCreator;
import com.andrei.ticket_system.service.contracts.ConcertService;
import com.andrei.ticket_system.service.contracts.TicketService;
import com.andrei.ticket_system.service.contracts.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

@SpringBootApplication()
public class TicketSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketSystemApplication.class, args);
	}

}
