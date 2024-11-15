package com.proyecto.galtonparcial2.util;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//Clase que representa un emisor de mensajes de RabbitMQ
@Component
public class Sender {

    //Variable que representa el template de RabbitMQ
    private final RabbitTemplate rabbitTemplate;

    //Constructor
    @Autowired
    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //Metodo para enviar un mensaje
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("componentQueue", message);
        System.out.println("Sent <" + message + ">");
    }
}