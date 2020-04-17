package chess;

/*
 * Pregunta si una pieza (p1) de Color puede capturar una pieza contraria en Square.
 * No importa si el casillero actualmente esta siendo ocupado por alguna otra pieza (p2) de Color.
 * Esta funcion podria ser ser de utilizad para probar movimientos de Rey.
 */
@FunctionalInterface
public interface PositionCaptured {
	boolean check(Color color, Square square);
}
