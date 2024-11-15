package com.proyecto.galtonparcial2.factory;

import com.proyecto.galtonparcial2.model.Componente;
import com.proyecto.galtonparcial2.model.Tablero;
import com.proyecto.galtonparcial2.util.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Clase que representa una estación de trabajo que produce tableros
@Component
public class EstacionTrabajoTablero extends EstacionTrabajo {

    //Variable que representa el emisor de mensajes
    private final Sender sender;
    //Variable que representa el servicio de ejecución
    private final ExecutorService executorService;

    //Constructor
    @Autowired
    public EstacionTrabajoTablero(BlockingQueue<Componente> buffer, Sender sender) {
        super(buffer);
        this.sender = sender;
        this.executorService = Executors.newFixedThreadPool(2); // Use 2 threads for parallel processing
    }

    //Metodo para crear un tablero y agregarlo al buffer
    @Override
    public Componente crearComponente() {
        executorService.submit(() -> {
            try {
                Tablero tablero = new Tablero();
                buffer.put(tablero);
                sender.sendMessage("Tablero producido");
                System.out.println("Tablero producido");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        return null;
    }
}