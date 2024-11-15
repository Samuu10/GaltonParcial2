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

@SpringBootApplication
public class GaltonParcial2Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(GaltonParcial2Application.class, args);

		BlockingQueue<Componente> buffer = context.getBean(BlockingQueue.class);
		CountDownLatch latch = new CountDownLatch(1);

		EstacionTrabajoBola estacionTrabajoBola = new EstacionTrabajoBola(buffer, context.getBean(Sender.class), latch);
		EstacionTrabajoTablero estacionTrabajoTablero = new EstacionTrabajoTablero(buffer, context.getBean(Sender.class), latch);

		List<EstacionTrabajo> estacionesTrabajo = Arrays.asList(estacionTrabajoBola, estacionTrabajoTablero);

		CountDownLatch visualizacionLatch = new CountDownLatch(1);
		new Thread(() -> Visualizacion.iniciarVisualizacion(new String[]{}, visualizacionLatch)).start();

		try {
			visualizacionLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Ensamblador ensamblador = context.getBean(Ensamblador.class);
		Scheduler scheduler = new Scheduler(estacionesTrabajo);

		// Crear un tablero y 100 bolas
		estacionTrabajoTablero.crearComponente();
		estacionTrabajoBola.setCantidad(100);
		estacionTrabajoBola.crearComponente();

		new Thread(scheduler::startProduction).start();
	}
}