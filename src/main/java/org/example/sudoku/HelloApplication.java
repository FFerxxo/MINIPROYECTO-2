package org.example.sudoku;


import controller.SudokuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.SudokuBoard;
import view.SudokuView;


/**
 * Clase principal que inicia la aplicación del juego Sudoku 6x6.
 * Esta clase es el punto de entrada de la aplicación JavaFX.
 *
 * @author andres barbosa
 * @author yoselin serna
 * @version 1.0
 */
public class HelloApplication extends Application {


    /**
     * Método que configura y muestra la ventana principal de la aplicación.
     * Implemente el patrón MVC creando el modelo, la vista y el controlador.
     *
     * @param stage El escenario principal proporcionado por JavaFX
     */
    @Override
    public void start(Stage stage) {
        try {
            // Creo el modelo
            SudokuBoard model = new SudokuBoard();


            // Creo la vista
            SudokuView view = new SudokuView();


            // Creao el controlador y conecto el modelo y vista
            SudokuController controller = new SudokuController(model, view);


            // Configuro la escena
            Scene scene = new Scene(view.getRoot(), 500, 600);


            // Configuro el escenario
            stage.setTitle("Sudoku 6x6");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Método principal que inicia la aplicación.
     *
     */
    public static void main(String[] args) {
        launch();
    }
}
