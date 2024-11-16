package com.proyecto.galtonparcial2;

import com.proyecto.galtonparcial2.factory.EstacionTrabajo;
import com.proyecto.galtonparcial2.factory.EstacionTrabajoBola;
import com.proyecto.galtonparcial2.factory.EstacionTrabajoTablero;
import com.proyecto.galtonparcial2.model.Componente;
import com.proyecto.galtonparcial2.util.Ensamblador;
import com.proyecto.galtonparcial2.util.Scheduler;
import com.proyecto.galtonparcial2.util.Sender;
import com.proyecto.galtonparcial2.view.Visualizacion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

//Clase principal de la aplicación
@SpringBootApplication
public class GaltonParcial2Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(GaltonParcial2Application.class, args);

		//Creamos un buffer de componentes
		BlockingQueue<Componente> buffer = context.getBean(BlockingQueue.class);
		//Creamos un latch (cerrojo) para sincronizar la producción de bolas
		CountDownLatch latch = new CountDownLatch(1);

		//Creamos una estación de trabajo de tipo bola
		EstacionTrabajoBola estacionTrabajoBola = new EstacionTrabajoBola(buffer, context.getBean(Sender.class), latch);
		//Creamos una estación de trabajo de tipo tablero
		EstacionTrabajoTablero estacionTrabajoTablero = new EstacionTrabajoTablero(buffer, context.getBean(Sender.class), latch);

		//Creamos una lista de estaciones de trabajo y le agregamos las estaciones de trabajo de tipo bola y tablero
		List<EstacionTrabajo> estacionesTrabajo = Arrays.asList(estacionTrabajoBola, estacionTrabajoTablero);

		//Creamos un latch (cerrojo) para la visualización
		CountDownLatch visualizacionLatch = new CountDownLatch(1);
		//Iniciamos la visualización en un hilo aparte
		new Thread(() -> Visualizacion.iniciarVisualizacion(new String[]{}, visualizacionLatch)).start();

		//Esperamos a que la visualización se inicie
		try {
			visualizacionLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//Creamos un ensamblador
		Ensamblador ensamblador = context.getBean(Ensamblador.class);
		//Creamos un planificador de producción de componentes
		Scheduler scheduler = new Scheduler(estacionesTrabajo);

		//Creamos un tablero
		estacionTrabajoTablero.crearComponente();

		//Creamos 100 bolas
		estacionTrabajoBola.setCantidad(100);
		estacionTrabajoBola.crearComponente();

		//Iniciamos la producción de componentes
		new Thread(scheduler::startProduction).start();
	}
}