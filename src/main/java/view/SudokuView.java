package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.SudokuBoard;


/**
 * Vista que representa la interfaz gráfica del juego Sudoku.
 * Esta clase maneja la representación visual del tablero y los controles.
 *
 * @author andres barbosa
 * @author yoselin serna
 * @version 1.0
 */

public class SudokuView {

    private Label number3CountLabel;


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


        number3CountLabel = new Label( "Numeros 3 restantes: 6");

        // Organizar los elementos en la vista
        VBox topBox = new VBox(10, titleLabel);
        topBox.setAlignment(Pos.CENTER);

        HBox controlBox = new HBox(10, newGameButton, hintButton);
        controlBox.setAlignment(Pos.CENTER);

        VBox bottomBox = new VBox(10, controlBox, statusLabel);
        bottomBox.getChildren().add(number3CountLabel);
        bottomBox.setAlignment(Pos.CENTER);

        root.setTop(topBox);
        root.setCenter(sudokuGrid);
        root.setBottom(bottomBox);
    }

    /**
     * Aqui creo la cuadrícula del Sudoku con sus celdas.
     */
    private void createSudokuGrid() {
        sudokuGrid = new GridPane();
        sudokuGrid.setAlignment(Pos.CENTER);
        sudokuGrid.setHgap(2);
        sudokuGrid.setVgap(2);
        sudokuGrid.setStyle("-fx-background-color: #333333;");

        cells = new SudokuCell[SudokuBoard.BOARD_SIZE][SudokuBoard.BOARD_SIZE];

        // Creo las celdas y las agrego a la cuadrícula
        for (int row = 0; row < SudokuBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < SudokuBoard.BOARD_SIZE; col++) {
                SudokuCell cell = new SudokuCell(row, col);
                cells[row][col] = cell;

                // Agrego el borde más grueso para delimitar bloques
                if (row % SudokuBoard.BLOCK_ROWS == 0 && col % SudokuBoard.BLOCK_COLS == 0) {
                    cell.setStyle(cell.getStyle() + "-fx-border-width: 2 0 0 2;");
                } else if (row % SudokuBoard.BLOCK_ROWS == 0) {
                    cell.setStyle(cell.getStyle() + "-fx-border-width: 2 0 0 0;");
                } else if (col % SudokuBoard.BLOCK_COLS == 0) {
                    cell.setStyle(cell.getStyle() + "-fx-border-width: 0 0 0 2;");
                }

                // Manejor la selección de la celda
                cell.setOnMouseClicked(event -> {
                    if (selectedCell != null) {
                        selectedCell.deselect();
                    }
                    selectedCell = cell;
                    cell.select();
                });

                sudokuGrid.add(cell, col, row);
            }
        }
    }

    /**
     * Crea los botones de control del juego.
     */
    private void createControlButtons() {
        newGameButton = new Button("Nuevo Juego");
        newGameButton.setPrefWidth(120);

        hintButton = new Button("Ayuda");
        hintButton.setPrefWidth(120);
    }

    /**
     * Actualiza el tablero visual con los valores del modelo.
     *
     * @param board El modelo del tablero de Sudoku
     */
    public void updateBoard(SudokuBoard board) {
        for (int row = 0; row < SudokuBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < SudokuBoard.BOARD_SIZE; col++) {
                int value = board.getCellValue(row, col);
                boolean editable = board.isCellEditable(row, col);

                cells[row][col].setValue(value);
                cells[row][col].setEditable(editable);
            }
        }
    }

    /**
     * Actualiza el estado de una celda individual.
     *
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @param value Valor de la celda
     * @param isValid Indica si el valor es válido según las reglas
     */
    public void updateCell(int row, int col, int value, boolean isValid) {
        if (row >= 0 && row < SudokuBoard.BOARD_SIZE && col >= 0 && col < SudokuBoard.BOARD_SIZE) {
            cells[row][col].setValue(value);
            cells[row][col].setValid(isValid);
        }
    }

    /**
     * Resalta una celda como sugerencia de ayuda.
     *
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @param value Valor sugerido
     */
    public void highlightHint(int row, int col, int value) {
        if (row >= 0 && row < SudokuBoard.BOARD_SIZE && col >= 0 && col < SudokuBoard.BOARD_SIZE) {
            cells[row][col].setHint(value);
        }
    }

    /**
     * Muestra un mensaje de alerta con confirmación.
     *
     * @param title Título de la alerta
     * @param message Mensaje de la alerta
     * @return true si el usuario confirma, false en caso contrario
     */
    public boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().filter(response -> response == javafx.scene.control.ButtonType.OK).isPresent();
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param title Título del error
     * @param message Mensaje del error
     */
    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de información.
     *
     * @param title Título del mensaje
     * @param message Contenido del mensaje
     */
    public void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Actualiza el texto de la etiqueta de estado.
     *
     * @param message Mensaje de estado
     */
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Obtiene el botón de nuevo juego.
     *
     * @return El botón de nuevo juego
     */
    public Button getNewGameButton() {
        return newGameButton;
    }

    /**
     * Obtiene el botón de ayuda.
     *
     * @return El botón de ayuda
     */
    public Button getHintButton() {
        return hintButton;
    }

    /**
     * Obtiene la celda actualmente seleccionada.
     *
     * @return La celda seleccionada, o null si no hay ninguna
     */
    public SudokuCell getSelectedCell() {
        return selectedCell;
    }

    /**
     * Obtiene el panel raíz de la vista.
     *
     * @return El panel raíz
     */
    public BorderPane getRoot() {
        return root;
    }

    public void updateNumber3Count(int count) {
        number3CountLabel.setText("Numeros 3 restantes: " + count);
    }
}


