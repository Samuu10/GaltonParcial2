# Simulación de producción concurrente con Spring Boot WebFlux y RabbitMQ en la Fábrica de Campanas de Gauss

## Descripción del Proyecto

En este proyecto, se simula el proceso de producción de una fábrica llamada "Campanas de Gauss", fundada por Sir Francis Galton en un universo paralelo en 1850. La fábrica produce máquinas que demuestran la distribución normal utilizando un tablero de Galton. La simulación se realiza utilizando Spring Boot WebFlux y RabbitMQ para modelar la concurrencia en la fabricación.

## Estructura del Proyecto

### Clases Principales

#### `GaltonParcial2Application`
Clase principal de la aplicación que inicia el contexto de Spring Boot y configura los componentes necesarios para la simulación.

#### `Sender`
Clase que representa un emisor de mensajes de RabbitMQ. Utiliza `RabbitTemplate` para enviar mensajes a la cola `componentQueue`.

#### `Receiver`
Clase que representa un receptor de mensajes de RabbitMQ. Recibe mensajes de la cola `componentQueue` y los procesa.

#### `Scheduler`
Clase que representa el planificador de producción de componentes. Coordina la producción de bolas y tableros en las estaciones de trabajo.

#### `Ensamblador`
Clase que representa un ensamblador de componentes. Recibe mensajes de RabbitMQ y ensambla los componentes en el buffer.

#### `EstacionTrabajo`
Clase abstracta que representa una estación de trabajo genérica. Define el método abstracto `crearComponente` para la creación de componentes.

#### `EstacionTrabajoBola`
Clase que extiende `EstacionTrabajo` y representa una estación de trabajo que produce bolas. Utiliza `ScheduledExecutorService` para programar la producción de bolas.

#### `EstacionTrabajoTablero`
Clase que extiende `EstacionTrabajo` y representa una estación de trabajo que produce tableros. Utiliza `ExecutorService` para gestionar la producción de tableros.

#### `SchedulerController`
Controlador que expone un endpoint para iniciar el planificador de producción.

#### `EstacionTrabajoController`
Controlador que expone endpoints para iniciar la producción de bolas y tableros.

#### `Visualizacion`
Clase que representa la visualización en tiempo real de la distribución de bolas en el tablero de Galton. Utiliza JavaFX para mostrar la simulación.

### Clases de Modelo

#### `Componente`
Interfaz que representa un componente genérico. Define el método `dibujar` para dibujar el componente en la visualización.

#### `Bola`
Clase que implementa `Componente` y representa una bola. Define el método `run` para mover la bola por el tablero.

#### `Tablero`
Clase que implementa `Componente` y representa el tablero de Galton. Define el método `dibujar` para dibujar el tablero en la visualización.

### Configuración

#### `ConfigRabbitMQ`
Clase de configuración de RabbitMQ. Define los beans necesarios para la conexión y gestión de colas en RabbitMQ.

#### `ConfigApp`
Clase de configuración de la aplicación. Define los beans necesarios para la gestión de la cola de componentes y el latch de sincronización.

### Archivos de Configuración

#### `application.properties`
Archivo de configuración de la aplicación. Define las propiedades de configuración de Spring Boot, RabbitMQ y WebFlux.

## Funcionamiento del Programa

1. **Inicio de la Aplicación**: La clase `GaltonParcial2Application` inicia el contexto de Spring Boot y configura los componentes necesarios.
2. **Producción de Componentes**: Las estaciones de trabajo (`EstacionTrabajoBola` y `EstacionTrabajoTablero`) producen bolas y tableros, respectivamente. Utilizan `Sender` para enviar mensajes a RabbitMQ.
3. **Sincronización**: RabbitMQ se utiliza para sincronizar la producción y el ensamblaje de componentes. `Receiver` recibe los mensajes y `Ensamblador` ensambla los componentes en el buffer.
4. **Planificación**: `Scheduler` coordina la producción de componentes utilizando un algoritmo de scheduling personalizado.
5. **Visualización**: `Visualizacion` muestra en tiempo real la distribución de bolas en el tablero de Galton utilizando JavaFX.

## Requisitos

- **Spring Boot WebFlux**: Para modelar la concurrencia y la programación reactiva.
- **RabbitMQ**: Para la sincronización y comunicación entre componentes.
- **JavaFX**: Para la visualización en tiempo real de la distribución de bolas.

## Ejecución

Para ejecutar la aplicación, asegúrate de tener configurado RabbitMQ y JavaFX. Luego, inicia la aplicación desde la clase `GaltonParcial2Application`.

## Documentación y Estilo de Código

El código está organizado y documentado adecuadamente para facilitar su comprensión y mantenimiento. Los comentarios en el código explican las funcionalidades y el flujo de la aplicación.
