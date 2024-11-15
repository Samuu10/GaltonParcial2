package com.proyecto.galtonparcial2.util;

import com.proyecto.galtonparcial2.factory.EstacionTrabajo;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Clase que representa el planificador de producción
@Component
public class Scheduler {

    //Variable que representa las estaciones de trabajo
    private final List<EstacionTrabajo> estacionesTrabajo;
    //Variable que representa el servicio de ejecución
    private final ExecutorService executorService;
    //Variable que representa el índice de la estación de trabajo actual
    private int indice;

    //Constructor
    public Scheduler(List<EstacionTrabajo> estacionesTrabajo) {
        this.estacionesTrabajo = estacionesTrabajo;
        this.executorService = Executors.newFixedThreadPool(estacionesTrabajo.size());
        this.indice = 0;
    }

    //Metodo para iniciar la producción de componentes siguiendo un algoritmo round-robin
    public void startProduction() {
        while (true) {
            EstacionTrabajo estacion = estacionesTrabajo.get(indice);
            executorService.submit(estacion::crearComponente);
            indice = (indice + 1) % estacionesTrabajo.size();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}