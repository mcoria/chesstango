package chess;

/*
 * Pregunta si un casillero del tablero puede puede ser capturado.
 */
@FunctionalInterface
public interface PositionCaptured {
	boolean check(Color color, Square square);
}
