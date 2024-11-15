package com.proyecto.galtonparcial2.util;

import com.proyecto.galtonparcial2.model.Bola;
import com.proyecto.galtonparcial2.model.Componente;
import com.proyecto.galtonparcial2.model.Tablero;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.BlockingQueue;

//Clase que representa el ensamblador de los componentes
@Component
public class Ensamblador {

    //Variable que representa el buffer en el que se almacenan los componentes
    private final BlockingQueue<Componente> buffer;
    //Variable que representa si el tablero ya fue creado
    private boolean tableroCreado = false;
    //Variable que representa la cantidad de bolas creadas
    private int bolasCreadas = 0;
    //Variable que representa la cantidad máxima de bolas
    private final int maxBolas = 100;

    //Constructor
    public Ensamblador(BlockingQueue<Componente> buffer) {
        this.buffer = buffer;
    }

    //Metodo para ensamblar los componentes
    @RabbitListener(queues = "componentQueue")
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        try {
            if (message.equals("Bola producida") && bolasCreadas < maxBolas) {
                Bola bola = new Bola();
                buffer.put(bola);
                bolasCreadas++;
            } else if (message.equals("Tablero producido") && !tableroCreado) {
                Tablero tablero = new Tablero();
                buffer.put(tablero);
                tableroCreado = true;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error al ensamblar componente: " + e.getMessage());
        }
    }
}