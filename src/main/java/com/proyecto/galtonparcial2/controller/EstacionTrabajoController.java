package com.proyecto.galtonparcial2.controller;

import com.proyecto.galtonparcial2.factory.EstacionTrabajoBola;
import com.proyecto.galtonparcial2.factory.EstacionTrabajoTablero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

//Clase que representa un controlador de la estación de trabajo y se encarga de recibir las peticiones del cliente
@RestController
@RequestMapping("/estacion-trabajo")
public class EstacionTrabajoController {

    //Varable que representa la estación de trabajo de bolas
    private final EstacionTrabajoBola estacionTrabajoBola;
    //Varable que representa la estación de trabajo de tableros
    private final EstacionTrabajoTablero estacionTrabajoTablero;

    //Constructor
    @Autowired
    public EstacionTrabajoController(EstacionTrabajoBola estacionTrabajoBola, EstacionTrabajoTablero estacionTrabajoTablero) {
        this.estacionTrabajoBola = estacionTrabajoBola;
        this.estacionTrabajoTablero = estacionTrabajoTablero;
    }

    //Metodo que inicia la producción de bolas
    @PostMapping("/producir-bolas")
    public Mono<String> producirBolas() {
        return Mono.fromCallable(() -> {
            estacionTrabajoBola.setCantidad(100);
            estacionTrabajoBola.crearComponente();
            return "Producción de bolas iniciada";
        });
    }

    //Metodo que inicia la producción de tableros
    @PostMapping("/producir-tablero")
    public Mono<String> producirTablero() {
        return Mono.fromCallable(() -> {
            estacionTrabajoTablero.crearComponente();
            return "Producción de tablero iniciada";
        });
    }
}