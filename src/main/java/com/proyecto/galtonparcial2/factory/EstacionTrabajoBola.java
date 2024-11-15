package com.proyecto.galtonparcial2.factory;

import com.proyecto.galtonparcial2.model.Bola;
import com.proyecto.galtonparcial2.model.Componente;
import com.proyecto.galtonparcial2.util.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.*;

@Component
public class EstacionTrabajoBola extends EstacionTrabajo {

    private int cantidad;
    private final Sender sender;
    private final ScheduledExecutorService scheduler;
    private final CountDownLatch latch;

    @Autowired
    public EstacionTrabajoBola(BlockingQueue<Componente> buffer, Sender sender, CountDownLatch latch) {
        super(buffer);
        this.sender = sender;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.latch = latch;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public Componente crearComponente() {
        try {
            latch.await(); // Esperar a que el tablero sea producido
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        for (int i = 0; i < cantidad; i++) {
            int delay = i * 100; // Delay in milliseconds
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