package com.proyecto.galtonparcial2.config;

import com.proyecto.galtonparcial2.model.Componente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class ConfigApp {

    @Bean
    public BlockingQueue<Componente> componenteBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }
}