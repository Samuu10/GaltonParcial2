package com.proyecto.galtonparcial2.view;

import com.proyecto.galtonparcial2.model.Bola;
import com.proyecto.galtonparcial2.model.Tablero;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Visualizacion extends Application {

    //Variable estática que representa el cerrojo
    private static CountDownLatch latch;
    //Variable estática que representa el contenedor de la visualización
    private static Pane root;
    //Variable que representa la lista de bolas
    private List<Bola> bolas;
    //Variable que representa el tablero
    private Tablero tablero;
    //Arreglo que representa las barras del histograma
    private Rectangle[] histogramBars;
    //Arreglo que representa los textos de los conteos de bolas
    private Text[] ballCounts;
    //Arreglo que representa los valores de los conteos de bolas
    private int[] ballCountValues;
    //Variable estática que representa la instancia de la visualización
    private static Visualizacion instance = new Visualizacion();

    //Constructor
    public Visualizacion() {
        instance = this;
        this.bolas = new ArrayList<>();
        this.histogramBars = new Rectangle[10];
        this.ballCounts = new Text[10];
        this.ballCountValues = new int[10];
    }

    //Metodo para obtener la instancia de la visualización
    public static Visualizacion getInstance() {
        return instance;
    }

    //Metodo para iniciar la visualización
    public static void iniciarVisualizacion(String[] args, CountDownLatch latch) {
        Visualizacion.latch = latch;
        launch(args);
    }

    //Metodo para iniciar la visualización en JavaFX
    @Override
    public void start(Stage primaryStage) {
        //Se crea la ventana de la visualización
        root = new Pane();
        Scene scene = new Scene(root, 600, 800);

        //Se establecen las propiedades de la ventana
        primaryStage.setTitle("Simulación Tablero de Galton");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Bucle para crear las barras del histograma y los textos de los conteos de bolas
        for (int i = 0; i < histogramBars.length; i++) {
            histogramBars[i] = new Rectangle(40, 0, Color.GREEN);
            histogramBars[i].setLayoutX(100 + i * 40);
            histogramBars[i].setLayoutY(750);
            root.getChildren().add(histogramBars[i]);

            ballCounts[i] = new Text("0");
            ballCounts[i].setLayoutX(115 + i * 40);
            ballCounts[i].setLayoutY(750);
            root.getChildren().add(ballCounts[i]);
        }

        latch.countDown();
    }

    //Metodo para agregar el tablero a la visualización
    public void agregarTablero(Tablero tablero) {
        this.tablero = tablero;
        //Función que se ejecuta en el hilo de JavaFX y dibuja el tablero
        Platform.runLater(() -> {
            if (root != null) {
                tablero.dibujar(root);
            } else {
                System.out.println("Error: root is not initialized.");
            }
        });
    }

    //Metodo para agregar una bola a la visualización
    public void agregarBola(Bola bola) {
        bolas.add(bola);
        //Función que se ejecuta en el hilo de JavaFX y dibuja la bola
        Platform.runLater(() -> {
            if (root != null) {
                bola.dibujar(root);
                new Thread(bola).start();
            } else {
                System.out.println("Error: root is not initialized.");
            }
        });
    }

    //Metodo para actualizar el histograma según el contenedor en el que cae la bola
    public void actualizarHistograma(int containerIndex) {
        Platform.runLater(() -> {
            //Si el contenedor está dentro de los límites, se actualiza el histograma
            if (containerIndex >= 0 && containerIndex < histogramBars.length) {
                //Se aumenta la altura de la barra del histograma y se disminuye la posición en Y
                if (histogramBars[containerIndex] != null) {
                    histogramBars[containerIndex].setHeight(histogramBars[containerIndex].getHeight() + 10);
                    histogramBars[containerIndex].setLayoutY(histogramBars[containerIndex].getLayoutY() - 10);
                    ballCountValues[containerIndex]++;
                    ballCounts[containerIndex].setText(String.valueOf(ballCountValues[containerIndex]));
                } else {
                    System.out.println("Error: histogramBars[" + containerIndex + "] es null.");
                }
            } else {
                System.out.println("Error: containerIndex fuera de límites.");
            }
        });
    }
}