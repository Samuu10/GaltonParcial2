package com.proyecto.galtonparcial2.controller;

import com.proyecto.galtonparcial2.util.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

//Clase que representa un controlador del planificador y se encarga de recibir las peticiones del cliente
@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    //Variable que representa el planificador
    private final Scheduler scheduler;

    //Constructor
    @Autowired
    public SchedulerController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    //Metodo que inicia el planificador
    @PostMapping("/start")
    public Mono<String> startScheduler() {
        //Se devuelve un mono que se ejecuta de manera asíncrona y se encarga de iniciar el planificador
        return Mono.fromCallable(() -> {
            new Thread(scheduler::startProduction).start();
            return "Scheduler iniciado";
        });
    }
}