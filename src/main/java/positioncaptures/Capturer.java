package positioncaptures;

import chess.Color;
import chess.Square;

/*
 * Pregunta si color puede capturar square. Las dos implementaciones podrian ser de utilidad en distintos momentos del juego.
 */
@FunctionalInterface
public interface Capturer {
	boolean positionCaptured(Color color, Square square);
}