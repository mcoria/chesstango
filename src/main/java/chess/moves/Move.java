package chess.moves;

import chess.ChessPosition;
import chess.BoardState;
import chess.PiecePositioned;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PiecePlacement;
import chess.pseudomovesfilters.MoveFilter;

//TOOD: Y si en vez de PosicionPieza utilizamos Square para To?
//      La mayoria de los movimientos posibles es a square vacios
//      Tiene sentido puesto que las capturas solo son contra piezas contrarias, sin importar que pieza es.

//TODO: y se implementamos un cache de movimientos? Implementar flyweight  pattern
//TODO: implementar Factory para crear objetos, la creacion est� repartida por todas partes y habria que desacoplarla de move generators 
// 		ademas con Decorartor se complic� por ejemplo: 
// 				algunos movimientos de King quitan derecho a enroque; 
//				todos los movimientos de torre que la mueven de su posicion inicial hacen perder enroque
//				toda captura a una torre que que se encuentra en si posicion inicial hacen perder enroque
//		cada vez que se cambia la jerarquia o hay algun tipo de modificacion en estos objetos las clases de pruebas necesitan ser actualizadas


/**
 * @author Mauricio Coria
 *
 */
public interface Move extends Comparable<Move> {
	PiecePositioned getFrom();
	PiecePositioned getTo();

	void executeMove(PiecePlacement board);
	void undoMove(PiecePlacement board);
	
	void executeMove(BoardState boardState);
	void undoMove(BoardState boardState);

	void executeMove(ColorBoard coloBoard);
	void undoMove(ColorBoard colorBoard);
	
	void executeMove(MoveCacheBoard moveCache);
	void undoMove(MoveCacheBoard moveCache);
	
	void executeMove(KingCacheBoard kingCacheBoard);
	void undoMove(KingCacheBoard kingCacheBoard);
	
	void executeMove(ChessPosition chessPosition);
	void undoMove(ChessPosition chessPosition);
	
	boolean filter(MoveFilter filter);
}