package com.proyecto.galtonparcial2.model;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

//Clase que representa el tablero de Galton
public class Tablero implements Componente {

    //Variable que representa la forma del tablero
    private Pane shape;

    //Constructor que inicializa el tablero de Galton
    public Tablero() {
        this.shape = new Pane();
        int rows = 10;
        int clavoSeparacion = 20;

        //Dibujar clavos
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= i; j++) {
                Circle pivot = new Circle(5, Color.BLACK);
                pivot.setLayoutX(300 + j * clavoSeparacion - i * clavoSeparacion / 2);
                pivot.setLayoutY(50 + i * 30);
                shape.getChildren().add(pivot);
            }
        }

        //Dibujar contenedores
        for (int i = 0; i <= rows - 1; i++) {
            Rectangle container = new Rectangle(20, 50, Color.BLUE);
            container.setStroke(Color.BLACK);
            container.setStrokeWidth(1);
            container.setLayoutX(300 + i * clavoSeparacion - rows * clavoSeparacion / 2);
            container.setLayoutY(50 + rows * 30);
            shape.getChildren().add(container);
        }
    }

    //Metodo para dibujar el tablero en la visualización
    @Override
    public void dibujar(Pane root) {
        if (shape != null) {
            Platform.runLater(() -> root.getChildren().add(shape));
        } else {
            System.out.println("Error: shape is null.");
        }
    }
}