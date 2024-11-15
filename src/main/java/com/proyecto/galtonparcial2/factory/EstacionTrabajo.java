package com.proyecto.galtonparcial2.factory;

import com.proyecto.galtonparcial2.model.Componente;
import java.util.concurrent.BlockingQueue;

//Clase abstracta que representa una estación de trabajo genérica
public abstract class EstacionTrabajo extends Thread {

    //Variable que representa el buffer en el que se almacenan los componentes
    protected BlockingQueue<Componente> buffer;

    //Constructor
    public EstacionTrabajo(BlockingQueue<Componente> buffer) {
        this.buffer = buffer;
    }

    //Metodo abstracto para crear un componente
    public abstract Componente crearComponente();
}