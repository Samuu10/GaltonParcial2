package com.proyecto.galtonparcial2.config;

import com.proyecto.galtonparcial2.model.Componente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

//Clase que configura los beans de la aplicación para que sean inyectados en las clases que los necesiten
@Configuration
public class ConfigApp {

    //Metodo que gestiona el bean de la cola de componentes
    @Bean
    public BlockingQueue<Componente> componenteBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    //Metodo que gestiona el bean del latch
    @Bean
    public CountDownLatch countDownLatch() {
        return new CountDownLatch(1);
    }
}