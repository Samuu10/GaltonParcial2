package com.proyecto.galtonparcial2.util;

import org.springframework.stereotype.Component;

//Clase que representa un receptor de mensajes de RabbitMQ
@Component
public class Receiver {

    //Metodo que recibe un mensaje
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
    }
}