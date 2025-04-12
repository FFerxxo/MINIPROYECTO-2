package model;

import java.util.Random;

/**
 * Modelo que representa el tablero de Sudoku y su lógica.
 * Esta clase maneja la representación interna del tablero 6x6
 * y proporciona métodos para manipular y validar el estado del juego.
 *
 * @author andres barbosa
 * @author yoselin serna
 * @version 1.0
 */
public class SudokuBoard {

    /** Tamaño del tablero Sudoku (6x6) */
    public static final int BOARD_SIZE = 6;

    /** Tamaño de los bloques (2x3) */
    public static final int BLOCK_ROWS = 2; //fila
    public static final int BLOCK_COLS = 3; //columna

    /** Matriz que representa el tablero de Sudoku */
    private int[][] board;

    /** Matriz que indica si una celda es editable (las celdas iniciales no lo son) */
    private boolean[][] editable;

    /**
     * Constructor que inicializa el tablero de Sudoku.
     */
    public SudokuBoard() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        editable = new boolean[BOARD_SIZE][BOARD_SIZE];

        // Todas las celdas son editables inicialmente
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = 1; // 0 representa una celda vacía
                editable[row][col] = true;
            }
        }
    }

    /**
     * este metodo inicializa un nuevo juego con algunos números iniciales en el tablero.
     * Coloca 2 números por cada bloque de 2x3 asegurándose que sean válidos.
     */
    public void initializeGame() {
        // Limpiar el tablero
        clearBoard();

        Random random = new Random();

        // Colocar 2 números en cada bloque de 2x3
        for (int blockRow = 0; blockRow < BOARD_SIZE / BLOCK_ROWS; blockRow++) {
            for (int blockCol = 0; blockCol < BOARD_SIZE / BLOCK_COLS; blockCol++) {
                placeNumbersInBlock(blockRow, blockCol, random);
            }
        }
    }
    /**
     * Coloca exactamente 6 números 1 en el tablero, uno por fila/columna/bloque
     * .
     */
    public void placeSixValidOnes() {
        int unosColocados = 0;

        for (int row = 0; row < BOARD_SIZE && unosColocados < 6; row++) {
            for (int col = 0; col < BOARD_SIZE && unosColocados < 6; col++) {
                if (board[row][col] == 0 && isValidPlacement(row, col, 1)) {
                    board[row][col] = 1;
                    editable[row][col] = false;
                    unosColocados++;
                }
            }
        }
    }


    /**
     * Coloca 2 números aleatorios en un bloque específico.
     *
     * @param blockRow Fila del bloque
     * @param blockCol Columna del bloque
     * @param random Generador de números aleatorios
     */
    private void placeNumbersInBlock(int blockRow, int blockCol, Random random) {
        int count = 0;
        while (count < 2) { // 2 números por bloque
            int row = blockRow * BLOCK_ROWS + random.nextInt(BLOCK_ROWS);
            int col = blockCol * BLOCK_COLS + random.nextInt(BLOCK_COLS);

            // Si la celda está vacía
            if (board[row][col] == 0) {
                int num = random.nextInt(BOARD_SIZE) + 1; // Número del 1 al 6

                // Verificar si es válido colocar este número aquí
                if (isValidPlacement(row, col, num)) {
                    board[row][col] = num;
                    editable[row][col] = false; // No se puede editar esta celda
                    count++;
                }
            }
        }
    }

    /**
     * Limpia el tablero y lo prepara para un nuevo juego.
     */
    private void clearBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = 0;
                editable[row][col] = true;
            }
        }
    }

    /**
     * Verifica si es válido colocar un número en una posición específica.
     *
     * @param row Fila donde se coloca el número
     * @param col Columna donde se coloca el número
     * @param num Número a colocar
     * @return true si el número puede ser colocado, false en caso contrario
     */
    public boolean isValidPlacement(int row, int col, int num) {
        // Verificar fila si encuentra el mismo numero retonar false, no se puede poner
        for (int c = 0; c < BOARD_SIZE; c++) {
            if (board[row][c] == num) {
                return false;
            }
        }

        // Verificar columna, lo mismo aqui
        for (int r = 0; r < BOARD_SIZE; r++) {
            if (board[r][col] == num) {
                return false;
            }
        }

        // Verificar bloque 2x3
        int blockRowStart = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int blockColStart = (col / BLOCK_COLS) * BLOCK_COLS;


        // Recorre todas las celdas dentro del bloque si encunetra el mismo # retorna false
        for (int r = blockRowStart; r < blockRowStart + BLOCK_ROWS; r++) {
            for (int c = blockColStart; c < blockColStart + BLOCK_COLS; c++) {
                if (board[r][c] == num) {
                    return false;
                }
            }
        }
        // si no encuentra repetido
        return true;
    }

    /**
     * Coloca un número en una posición específica del tablero si es válido.
     *
     * @param row Fila donde se coloca el número
     * @param col Columna donde se coloca el número
     * @param num Número a colocar
     * @return true si el número fue colocado, false si no es válido o la celda no es editable
     */
    public boolean placeNumber(int row, int col, int num) {
        if (!isValidPosition(row, col) || !editable[row][col]) {
            return false;
        }

        // Si el número es 0, significa borrar la celda
        if (num == 0) {
            board[row][col] = 0;
            return true;
        }

        // Asegura que el número esté dentro del rango del tablero 1-6
        if (num < 1 || num > BOARD_SIZE) {
            return false;
        }

        // Verificar si es válido colocar este número
        // Usa el método isValidPlacement() para verificar las reglas del Sudoku
        // Si es válido, coloca el número
        //Retorna true si se pudo colocar
        if (isValidPlacement(row, col, num)) {
            board[row][col] = num;
            return true;
        }

        return false;
    }

    /**
     * Verifica si una posición está dentro del tablero.
     * Este método comprueba si las coordenadas de fila y columna
     * corresponden a una posición válida dentro del tablero.
     *
     * @param row Fila a verificar
     * @param col Columna a verificar
     * @return true si la posición es válida, false en caso contrario
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    /**
     * Proporciona un número válido para una celda vacía como ayuda.(la pista)
     *
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @return Un número válido para esa celda, o 0 si no hay celdas vacías o no es posible
     */
    public int getHint(int row, int col) { // verifica estas tres condiciones
        if (!isValidPosition(row, col) || !editable[row][col] || board[row][col] != 0) {
            return 0;
        }

        // Probar cada número del 1 al 6 a ver cual se puede colocar
        for (int num = 1; num <= BOARD_SIZE; num++) {
            if (isValidPlacement(row, col, num)) {
                return num;
            }
        }

        return 0; // No se encontró un número válido
    }

    /**
     * Verifica si el tablero está completo y correcto.
     *
     * @return true si el juego está completo, false en caso contrario
     */
    public boolean isGameComplete() {
        // Verificar que no hay celdas vacías
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == 0) {
                    return false;
                }
            }
        }

        // Verificar que todas las filas, columnas y bloques son válidos
        return isBoardValid();
    }

    /**
     * Verifica si el tablero actual es válido según las reglas de Sudoku.
     *
     * @return true si el tablero es válido, false en caso contrario
     */
    private boolean isBoardValid() {
        // Verificar cada fila
        for (int row = 0; row < BOARD_SIZE; row++) {
            boolean[] seen = new boolean[BOARD_SIZE + 1];
            for (int col = 0; col < BOARD_SIZE; col++) {
                int num = board[row][col];
                if (num > 0 && seen[num]) {
                    return false;
                }
                seen[num] = true;
            }
        }

        // Verificar cada columna
        for (int col = 0; col < BOARD_SIZE; col++) {
            boolean[] seen = new boolean[BOARD_SIZE + 1];
            for (int row = 0; row < BOARD_SIZE; row++) {
                int num = board[row][col];
                if (num > 0 && seen[num]) {
                    return false;
                }
                seen[num] = true;
            }
        }

        // Verificar cada bloque
        for (int blockRow = 0; blockRow < BOARD_SIZE / BLOCK_ROWS; blockRow++) {
            for (int blockCol = 0; blockCol < BOARD_SIZE / BLOCK_COLS; blockCol++) {
                if (!isBlockValid(blockRow, blockCol)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica si un bloque específico es válido.
     *
     * Sirve para verificar que en un bloque no hayan números repetidos.
     *
     * @param blockRow Fila del bloque
     * @param blockCol Columna del bloque
     * @return true si el bloque es válido, false en caso contrario
     */
    private boolean isBlockValid(int blockRow, int blockCol) {
        boolean[] seen = new boolean[BOARD_SIZE + 1];

        int rowStart = blockRow * BLOCK_ROWS;
        int colStart = blockCol * BLOCK_COLS;

        for (int row = rowStart; row < rowStart + BLOCK_ROWS; row++) {
            for (int col = colStart; col < colStart + BLOCK_COLS; col++) {
                int num = board[row][col];
                if (num > 0 && seen[num]) {
                    return false;
                }
                seen[num] = true;
            }
        }

        return true;
    }

    /**
     * Obtiene el valor de una celda.
     *
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @return El valor de la celda, o 0 si está vacía o la posición es inválida
     */
    public int getCellValue(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return 0;
    }

    /**
     * Verifica si una celda es editable.
     *
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @return true si la celda es editable, false en caso contrario
     */
    public boolean isCellEditable(int row, int col) {
        if (isValidPosition(row, col)) {
            return editable[row][col];
        }
        return false;
    }
}