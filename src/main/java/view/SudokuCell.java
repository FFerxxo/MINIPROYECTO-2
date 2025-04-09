package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Clase que representa una celda individual del tablero de Sudoku.
 * Esta clase controla todo lo que puede hacer ese casilla
 * Extiende de StackPane para poder contener la etiqueta con el número.
 *
 *  @author andres barbosa
 *  @author yoselin serna
 * @version 1.0
 */
public class SudokuCell extends StackPane {

    /** Etiqueta que muestra el número en la celda */
    private Label numberLabel;

    /** Fila de la celda en el tablero */
    private int row;

    /** Columna de la celda en el tablero */
    private int col;

    /** Indica si la celda es editable */
    private boolean editable;

    /** Indica si el valor actual es válido */
    private boolean valid;

    /** Estilos CSS para diferentes estados de la celda */
    // Blanco cuando está vacío
    private static final String STYLE_NORMAL = "-fx-background-color: white; -fx-border-color: #CCCCCC;";
    // Gris cuando no se puede modificar
    private static final String STYLE_FIXED = "-fx-background-color: #F0F0F0; -fx-border-color: #CCCCCC;";
    // Azul claro cuando está seleccionada
    private static final String STYLE_SELECTED = "-fx-background-color: #E6F3FF; -fx-border-color: #0078D7;";
    // Rojo cuando tiene un número inválido
    private static final String STYLE_INVALID = "-fx-background-color: #FFE6E6; -fx-border-color: #FF0000;";
    // Verde claro cuando es una pista
    private static final String STYLE_HINT = "-fx-background-color: #E6FFE6; -fx-border-color: #00CC00;";

    /**
     * Constructor que inicializa una celda del Sudoku.
     *
     * @param row Fila de la celda
     * @param col Columna de la celda
     * getChildren() es un método de StackPane que devuelve una lista de todos los componentes dentro de un contenedor
     */
    public SudokuCell(int row, int col) {
        this.row = row; // guarda la posicion de la fila
        this.col = col; // guarda la posicion de la columna
        this.editable = true;
        this.valid = true;

        // Configurar la apariencia de la celda
        setPrefSize(60, 60);
        setStyle(STYLE_NORMAL); // pone el estado normal (blanco)

        // Crear la etiqueta para mostrar el número
        numberLabel = new Label(); // crea la etiqueta para mostrar el numero
        numberLabel.setStyle("-fx-font-size: 20px;");// tamaño
        numberLabel.setAlignment(Pos.CENTER); // centra

        getChildren().add(numberLabel);
    }

    /**
     * Mostrar el valor numérico de la celda.
     *
     * @param value Valor a mostrar (0 para celda vacía)
     */
    public void setValue(int value) {
        if (value > 0) { // Comprueba si el valor es mayor que cero
            numberLabel.setText(String.valueOf(value)); // Si el número es mayor que 0, lo convierte a texto
        } else {
            numberLabel.setText(""); // Si el valor es 0, borra el texto de la etiqueta
        }
        updateStyle(); // Actualiza el estilo visual de la celda
    }

    /**
     * Establece si la celda es editable o no.
     *
     * @param editable true si la celda es editable, false en caso contrario
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        updateStyle();
    }

    /**
     * Establece si el valor de la celda es válido según las reglas.
     *
     * @param valid true si el valor es válido, false en caso contrario
     */
    public void setValid(boolean valid) {
        this.valid = valid;
        updateStyle();
    }

    /**
     * Marca la celda como seleccionada.
     */
    public void select() {
        setStyle(STYLE_SELECTED);
    }

    /**
     * Desmarca la celda como seleccionada.
     */
    public void deselect() {
        updateStyle();
    }

    /**
     * Resalta la celda como sugerencia de ayuda.
     *
     * @param value Valor sugerido para la celda
     */
    public void setHint(int value) {
        setValue(value);
        setStyle(STYLE_HINT);
    }

    /**
     * Actualiza el estilo de la celda según su estado actual.
     */
    private void updateStyle() {
        if (!valid) {
            setStyle(STYLE_INVALID);
        } else if (!editable) {
            setStyle(STYLE_FIXED);
        } else {
            setStyle(STYLE_NORMAL);
        }
    }

    /**
     * Obtiene la fila de la celda.
     *
     * @return Fila de la celda
     */
    public int getRow() {
        return row;
    }

    /**
     * Obtiene la columna de la celda.
     *
     * @return Columna de la celda
     */
    public int getCol() {
        return col;
    }

    /**
     * Verifica si la celda es editable.
     *
     * @return true si la celda es editable, false en caso contrario
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Obtiene el valor actual de la celda.
     *
     * @return Valor de la celda, o 0 si está vacía
     */
    public int getValue() {
        String text = numberLabel.getText();
        if (text == null || text.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}