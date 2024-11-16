package com.proyecto.galtonparcial2.factory;

import com.proyecto.galtonparcial2.model.Componente;
import com.proyecto.galtonparcial2.model.Tablero;
import com.proyecto.galtonparcial2.util.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Clase que representa una estación de trabajo que produce tableros
@Component
public class EstacionTrabajoTablero extends EstacionTrabajo {

    //Variable que representa el emisor de mensajes
    private final Sender sender;
    //Variable que representa el servicio de ejecución de tareas
    private final ExecutorService executorService;
    //Variable que representa el latch (cerrojo) que se encarga de sincronizar la producción de tableros
    private final CountDownLatch latch;

    //Constructor
    @Autowired
    public EstacionTrabajoTablero(BlockingQueue<Componente> buffer, Sender sender, CountDownLatch latch) {
        super(buffer);
        this.sender = sender;
        this.executorService = Executors.newFixedThreadPool(2);
        this.latch = latch;
    }

    //Metodo para crear un tablero
    @Override
    public Componente crearComponente() {
        //Se crea una tarea que se encarga de producir un tablero y almacenarlo en el buffer
        executorService.submit(() -> {
            try {
                Tablero tablero = new Tablero();
                buffer.put(tablero);
                sender.sendMessage("Tablero producido");
                System.out.println("Tablero producido");
                //Se libera el latch para permitir la producción de bolas
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        return null;
    }
}