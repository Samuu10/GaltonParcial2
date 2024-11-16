package com.proyecto.galtonparcial2.factory;

import com.proyecto.galtonparcial2.model.Bola;
import com.proyecto.galtonparcial2.model.Componente;
import com.proyecto.galtonparcial2.util.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.*;

//Clase que representa una estación de trabajo que produce bolas
@Component
public class EstacionTrabajoBola extends EstacionTrabajo {

    //Variable que representa la cantidad de bolas a producir
    private int cantidad;
    //Variable que representa el emisor de mensajes
    private final Sender sender;
    //Variable que representa el planificador de tareas
    private final ScheduledExecutorService scheduler;
    //Variable que representa el latch (cerrojo) que se encarga de sincronizar la producción de bolas
    private final CountDownLatch latch;

    //Constructor
    @Autowired
    public EstacionTrabajoBola(BlockingQueue<Componente> buffer, Sender sender, CountDownLatch latch) {
        super(buffer);
        this.sender = sender;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.latch = latch;
    }

    //Setter para la cantidad de bolas a producir
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    //Metodo para crear una bola
    @Override
    public Componente crearComponente() {
        //Se espera a que el latch (ocupado por la creación del tablero) se libere para comenzar la producción de bolas
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        //Bucle que se encarga de producir la cantidad de bolas especificada
        for (int i = 0; i < cantidad; i++) {
            //Se programa la producción de una bola con un retraso de 100 milisegundos
            int delay = i * 100;
            //Se crea una tarea que se encarga de producir una bola y almacenarla en el buffer
            scheduler.schedule(() -> {
                try {
                    Bola bola = new Bola();
                    buffer.put(bola);
                    sender.sendMessage("Bola producida");
                    System.out.println("Bola producida");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }, delay, TimeUnit.MILLISECONDS);
        }
        return null;
    }
}