package controller;


import model.SudokuBoard;
import view.SudokuCell;
import view.SudokuView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * Controlador que maneja la lógica de interacción entre el modelo y la vista.
 * Esta clase implementa MVC conectando el modelo (SudokuBoard) con la vista (SudokuView).
 *
 * @author andres barbosa
 * @author yoselin serna
 * @version 1.0
 */
public class SudokuController {


    /** Modelo del tablero de Sudoku */
    private SudokuBoard model;


    /** Vista del juego Sudoku */
    private SudokuView view;


    /** Contador de ayudas utilizadas */
    private int hintsUsed;


    /** Número máximo de ayudas permitidas */
    private static final int MAX_HINTS = 3;


    /**
     * Constructor que inicializa el controlador con el modelo y la vista.
     *
     * @param model Modelo del tablero de Sudoku
     * @param view Vista del juego Sudoku
     */
    public SudokuController(SudokuBoard model, SudokuView view) {
        this.model = model;
        this.view = view;
        this.hintsUsed = 0;


        // Aqui Configuro los eventos
        setupEventHandlers();
    }

    private int countRemainingNumber3() {
        int placed = 0;
        // Contador de cuántos números 3 ya están colocados
        for (int row = 0; row < SudokuBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < SudokuBoard.BOARD_SIZE; col++) {
                if (model.getCellValue(row, col) == 3) {
                    placed++;
                }
            }
        }

        return SudokuBoard.BOARD_SIZE - placed;
    }

    private void updateNumber3CountInView() {
        int remaining = countRemainingNumber3();
        view.updateNumber3Count(remaining);
    }


    /**
     * Configuro los manejadores de eventos para la interfaz.
     */
    private void setupEventHandlers() {
        // Evento para el botón Nuevo Juego
        view.getNewGameButton().setOnAction(event -> startNewGame());


        // Evento para el botón Ayuda
        view.getHintButton().setOnAction(event -> provideHint());


        // Evento de teclado para toda la aplicación
        view.getRoot().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }


    /**
     * Inicio un nuevo juego.
     */
    private void startNewGame() {
        boolean confirmed = view.showConfirmation("Nuevo Juego",
                "¿Estás seguro de que deseas iniciar un nuevo juego?");


        if (confirmed) {
            model.initializeGame();
            view.updateBoard(model);
            updateNumber3CountInView();
            hintsUsed = 0;
            view.updateStatus("Nuevo juego iniciado. ¡Buena suerte!");
        }
    }


    /**
     * Proporciona una sugerencia para una celda vacía.
     */
    private void provideHint() {
        if (hintsUsed >= MAX_HINTS) {
            view.showInfo("Límite de Ayudas",
                    "Has alcanzado el límite de ayudas (" + MAX_HINTS + ") para este juego.");
            return;
        }


        SudokuCell selectedCell = view.getSelectedCell();
        if (selectedCell == null) {
            view.showInfo("Selecciona una Celda",
                    "Por favor, selecciona una celda vacía para recibir una sugerencia.");
            return;
        }


        int row = selectedCell.getRow();
        int col = selectedCell.getCol();


        if (!model.isCellEditable(row, col)) {
            view.showInfo("Celda No Editable",
                    "Esta celda es parte del tablero inicial y no se puede modificar.");
            return;
        }


        if (selectedCell.getValue() != 0) {
            view.showInfo("Celda Ocupada",
                    "Por favor, selecciona una celda vacía para recibir una sugerencia.");
            return;
        }


        int hint = model.getHint(row, col);
        if (hint > 0) {
            view.highlightHint(row, col, hint);
            if (hint == 3) {
               updateNumber3CountInView();
            }

            hintsUsed++;
            view.updateStatus("Ayuda utilizada (" + hintsUsed + "/" + MAX_HINTS + ")");
        } else {
            view.showInfo("No hay Sugerencia",
                    "No se puede encontrar una sugerencia válida para esta celda.");
        }
    }


    /**
     * Maneja los eventos de teclado para ingresar números en las celdas.
     *
     * @param event Evento de teclado
     */
    private void handleKeyPress(KeyEvent event) {
        SudokuCell selectedCell = view.getSelectedCell();
        if (selectedCell == null || !selectedCell.isEditable()) {
            return;
        }


        int row = selectedCell.getRow();
        int col = selectedCell.getCol();


        // Teclas numéricas 1-6
        if (event.getCode().isDigitKey()) {
            String digit = event.getText();
            try {
                int num = Integer.parseInt(digit);
                if (num >= 1 && num <= SudokuBoard.BOARD_SIZE) {
                    boolean success = model.placeNumber(row, col, num);
                    view.updateCell(row, col, num, success);

                    if (success && num == 3) {
                        updateNumber3CountInView();
                    } else if (success && selectedCell.getValue() == 3) {
                        // Si el usuario reemplazó un 3 con otro número
                        updateNumber3CountInView();
                    }

                    if (!success) {
                        view.showError("Número Inválido",
                                "El número " + num + " no puede colocarse aquí según las reglas del Sudoku.");
                    } else {
                        checkGameCompletion();
                    }
                }
            } catch (NumberFormatException e) {
                // No es un número válido
            }
            event.consume();
        }
        // Tecla de borrado
        else if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
            model.placeNumber(row, col, 0);
            view.updateCell(row, col, 0, true);
            event.consume();
        }
    }


    /**
     * Verifica si el juego ha sido completado correctamente.
     */
    private void checkGameCompletion() {
        if (model.isGameComplete()) {
            view.showInfo("¡Felicidades!", "Has completado el Sudoku correctamente.");
            view.updateStatus("Juego completado. ¡Felicidades!");
        }
    }
}
