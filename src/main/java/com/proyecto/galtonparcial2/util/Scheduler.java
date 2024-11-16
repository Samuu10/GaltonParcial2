package com.proyecto.galtonparcial2.util;

import com.proyecto.galtonparcial2.factory.EstacionTrabajo;
import com.proyecto.galtonparcial2.factory.EstacionTrabajoBola;
import com.proyecto.galtonparcial2.factory.EstacionTrabajoTablero;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Clase que representa el planificador de producción de componentes
@Component
public class Scheduler {

    //Variable que representa las estaciones de trabajo
    private final List<EstacionTrabajo> estacionesTrabajo;
    //Variable que representa el servicio de ejecución de tareas
    private final ExecutorService executorService;
    //Variable que representa si el tablero ya fue creado
    private boolean tableroCreado = false;
    //Variable que representa la cantidad de bolas creadas
    private int bolasCreadas = 0;
    //Variable que representa la cantidad máxima de bolas a crear
    private final int maxBolas = 100;

    //Constructor
    public Scheduler(List<EstacionTrabajo> estacionesTrabajo) {
        this.estacionesTrabajo = estacionesTrabajo;
        this.executorService = Executors.newFixedThreadPool(estacionesTrabajo.size());
    }

    //Metodo para iniciar la producción de componentes
    public void startProduction() {
        //Bucle que recorre las estaciones de trabajo
        for (EstacionTrabajo estacion : estacionesTrabajo) {
            //Si la estación es de tipo tablero y el tablero no ha sido creado, se crea el tablero
            if (estacion instanceof EstacionTrabajoTablero && !tableroCreado) {
                executorService.submit(estacion::crearComponente);
                tableroCreado = true;
            //Si la estación es de tipo bola y no se han creado todas las bolas, se crean las bolas
            } else if (estacion instanceof EstacionTrabajoBola && bolasCreadas < maxBolas) {
                ((EstacionTrabajoBola) estacion).setCantidad(maxBolas);
                executorService.submit(estacion::crearComponente);
                bolasCreadas = maxBolas;
            }
        }
    }
}