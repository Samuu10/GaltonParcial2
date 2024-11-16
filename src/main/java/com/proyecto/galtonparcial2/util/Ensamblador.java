package com.proyecto.galtonparcial2.util;

import com.proyecto.galtonparcial2.model.Bola;
import com.proyecto.galtonparcial2.model.Componente;
import com.proyecto.galtonparcial2.model.Tablero;
import com.proyecto.galtonparcial2.view.Visualizacion;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

//Clase que representa un ensamblador de componentes
@Component
public class Ensamblador {

    //Variable que representa el buffer en el que se almacenan los componentes
    private final BlockingQueue<Componente> buffer;
    //Variable que representa si el tablero ya fue creado
    private boolean tableroCreado = false;
    //Variable que representa la cantidad de bolas creadas
    private int bolasCreadas = 0;
    //Variable que representa la cantidad máxima de bolas a crear
    private final int maxBolas = 100;

    //Constructor
    public Ensamblador(BlockingQueue<Componente> buffer) {
        this.buffer = buffer;
    }

    //Metodo para recibir un mensaje de RabbitMQ
    @RabbitListener(queues = "componentQueue")
    public void receiveMessage(String message) {
        //Si ya se creó el tablero y se crearon todas las bolas, no se crea nada más
        if (tableroCreado && bolasCreadas >= maxBolas) {
            return;
        }

        System.out.println("Received <" + message + ">");
        try {
            //Si el mensaje es "Bola producida" y no se han creado todas las bolas, se crea una bola
            if (message.equals("Bola producida") && bolasCreadas < maxBolas) {
                Bola bola = new Bola();
                buffer.put(bola);
                bolasCreadas++;
                //Se agrega la bola a la visualización
                Visualizacion.getInstance().agregarBola(bola);
            //Si el mensaje es "Tablero producido" y no se ha creado el tablero, se crea un tablero
            } else if (message.equals("Tablero producido") && !tableroCreado) {
                Tablero tablero = new Tablero();
                buffer.put(tablero);
                tableroCreado = true;
                //Se agrega el tablero a la visualización
                Visualizacion.getInstance().agregarTablero(tablero);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error al ensamblar componente: " + e.getMessage());
        }
    }
}