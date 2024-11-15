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

@Component
public class EstacionTrabajoTablero extends EstacionTrabajo {

    private final Sender sender;
    private final ExecutorService executorService;
    private final CountDownLatch latch;

    @Autowired
    public EstacionTrabajoTablero(BlockingQueue<Componente> buffer, Sender sender, CountDownLatch latch) {
        super(buffer);
        this.sender = sender;
        this.executorService = Executors.newFixedThreadPool(2);
        this.latch = latch;
    }

    @Override
    public Componente crearComponente() {
        executorService.submit(() -> {
            try {
                Tablero tablero = new Tablero();
                buffer.put(tablero);
                sender.sendMessage("Tablero producido");
                System.out.println("Tablero producido");
                latch.countDown(); // Indicar que el tablero ha sido producido
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        return null;
    }
}