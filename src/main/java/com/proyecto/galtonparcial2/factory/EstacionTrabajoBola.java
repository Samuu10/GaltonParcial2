package com.proyecto.galtonparcial2.factory;

import com.proyecto.galtonparcial2.model.Bola;
import com.proyecto.galtonparcial2.model.Componente;
import com.proyecto.galtonparcial2.util.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Clase que representa una estación de trabajo que produce bolas
@Component
public class EstacionTrabajoBola extends EstacionTrabajo {

    //Variable que representa la cantidad de bolas a producir
    private int cantidad;
    //Variable que representa el emisor de mensajes
    private final Sender sender;
    //Variable que representa el servicio de ejecución
    private final ExecutorService executorService;

    //Constructor
    @Autowired
    public EstacionTrabajoBola(BlockingQueue<Componente> buffer, Sender sender) {
        super(buffer);
        this.sender = sender;
        this.executorService = Executors.newFixedThreadPool(4); // Use 4 threads for parallel processing
    }

    //Metodo setter de la cantidad de bolas a producir
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    //Metodo para crear una bola y agregarla al buffer
    @Override
    public Componente crearComponente() {
        for (int i = 0; i < cantidad; i++) {
            executorService.submit(() -> {
                try {
                    Bola bola = new Bola();
                    buffer.put(bola);
                    sender.sendMessage("Bola producida");
                    System.out.println("Bola producida");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            });
        }
        return null;
    }
}