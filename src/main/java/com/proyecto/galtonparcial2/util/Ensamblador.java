package com.proyecto.galtonparcial2.util;

import com.proyecto.galtonparcial2.model.Bola;
import com.proyecto.galtonparcial2.model.Componente;
import com.proyecto.galtonparcial2.model.Tablero;
import com.proyecto.galtonparcial2.view.Visualizacion;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class Ensamblador {

    private final BlockingQueue<Componente> buffer;
    private boolean tableroCreado = false;
    private int bolasCreadas = 0;
    private final int maxBolas = 100;

    public Ensamblador(BlockingQueue<Componente> buffer) {
        this.buffer = buffer;
    }

    @RabbitListener(queues = "componentQueue")
    public void receiveMessage(String message) {
        if (tableroCreado && bolasCreadas >= maxBolas) {
            return;
        }

        System.out.println("Received <" + message + ">");
        try {
            if (message.equals("Bola producida") && bolasCreadas < maxBolas) {
                Bola bola = new Bola();
                buffer.put(bola);
                bolasCreadas++;
                Visualizacion.getInstance().agregarBola(bola);
            } else if (message.equals("Tablero producido") && !tableroCreado) {
                Tablero tablero = new Tablero();
                buffer.put(tablero);
                tableroCreado = true;
                Visualizacion.getInstance().agregarTablero(tablero);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error al ensamblar componente: " + e.getMessage());
        }
    }
}