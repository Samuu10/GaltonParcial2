package com.proyecto.galtonparcial2.util;

import com.proyecto.galtonparcial2.factory.EstacionTrabajo;
import com.proyecto.galtonparcial2.factory.EstacionTrabajoBola;
import com.proyecto.galtonparcial2.factory.EstacionTrabajoTablero;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Scheduler {

    private final List<EstacionTrabajo> estacionesTrabajo;
    private final ExecutorService executorService;
    private boolean tableroCreado = false;
    private int bolasCreadas = 0;
    private final int maxBolas = 100;

    public Scheduler(List<EstacionTrabajo> estacionesTrabajo) {
        this.estacionesTrabajo = estacionesTrabajo;
        this.executorService = Executors.newFixedThreadPool(estacionesTrabajo.size());
    }

    public void startProduction() {
        for (EstacionTrabajo estacion : estacionesTrabajo) {
            if (estacion instanceof EstacionTrabajoTablero && !tableroCreado) {
                executorService.submit(estacion::crearComponente);
                tableroCreado = true;
            } else if (estacion instanceof EstacionTrabajoBola && bolasCreadas < maxBolas) {
                ((EstacionTrabajoBola) estacion).setCantidad(maxBolas);
                executorService.submit(estacion::crearComponente);
                bolasCreadas = maxBolas;
            }
        }
    }
}