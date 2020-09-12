package positioncaptures;

import chess.Color;
import chess.PosicionPieza;
import chess.Square;

public interface Capturer {

	//TODO: Esto hay que reimplementarlo, buscar los peones, caballos; alfiles; torres y reinas que podrian capturar la posicion
	// EN VEZ DE RECORRER TODO EL TABLERO EN BUSCA DE LAS PIEZAS QUE PUEDEN CAPTURAR LA POSICION
	/*
	 * Observar que este método itera las posiciones en base a boardCache.
	 * Luego obtiene la posicion de dummyBoard.
	 * Esto implica que boardCache necesita estar actualizado en todo momento. 
	 */
	PosicionPieza positionCaptured(Color color, Square square);

}