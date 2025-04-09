package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * Vista que representa la interfaz gráfica del juego Sudoku.
 * Esta clase maneja la representación visual del tablero y los controles.
 *
 * @author andres barbosa
 * @version 1.0
 */

public class SudokuView {

    /** Panel principal que contiene todos los elementos de la vista */
    private BorderPane root;

    /** Cuadrícula que contiene las celdas del Sudoku */
    private GridPane sudokuGrid;

    /** Matriz de celdas del Sudoku */
    private SudokuCell[][] cells;

    /** Botón para iniciar un nuevo juego */
    private Button newGameButton;

    /** Botón para obtener una ayuda */
    private Button hintButton;

    /** Etiqueta que muestra el estado del juego */
    private Label statusLabel;

    /** Celda actualmente seleccionada */
    private SudokuCell selectedCell;

    /**
     * Constructor que inicializa la interfaz gráfica.
     */
    public SudokuView() {
        initializeUI();
    }

    /**
     * Inicializa todos los componentes de la interfaz de usuario.
     */
    private void initializeUI() {
        root = new BorderPane();
        root.setPadding(new Insets(20));

        // Título del juego
        Label titleLabel = new Label("Sudoku 6x6");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Creo la cuadrícula del Sudoku
        createSudokuGrid();

        // Creo los botones de control
        createControlButtons();

        // Etiqueta de estado
        statusLabel = new Label("¡Bienvenido! Presiona 'Nuevo Juego' para comenzar.");

        // Organizar los elementos en la vista
        VBox topBox = new VBox(10, titleLabel);
        topBox.setAlignment(Pos.CENTER);

        HBox controlBox = new HBox(10, newGameButton, hintButton);
        controlBox.setAlignment(Pos.CENTER);

        VBox bottomBox = new VBox(10, controlBox, statusLabel);
        bottomBox.setAlignment(Pos.CENTER);

        root.setTop(topBox);
        root.setCenter(sudokuGrid);
        root.setBottom(bottomBox);
    }
}
