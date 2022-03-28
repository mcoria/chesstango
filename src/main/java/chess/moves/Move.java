package chess.moves;

import chess.PiecePositioned;
import chess.legalmovesgenerators.MoveFilter;
import chess.position.ChessPositionWriter;
import chess.position.PiecePlacementWritter;
import chess.position.imp.ColorBoard;
import chess.position.imp.MoveCacheBoard;
import chess.position.imp.PositionState;

//TOOD: Y si en vez de PosicionPieza utilizamos Square para To?
//      La mayoria de los movimientos posibles es a square vacios
//      Tiene sentido puesto que las capturas solo son contra piezas contrarias, sin importar que pieza es.

//TODO: y se implementamos un cache de movimientos? Implementar flyweight  pattern
//TODO: implementar Factory para crear objetos, la creacion está repartida por todas partes y habria que desacoplarla de move generators 
// 		ademas con Decorartor se complicó por ejemplo: 
// 				algunos movimientos de King quitan derecho a castling; 
//				todos los movimientos de torre que la mueven de su posicion inicial hacen perder castling
//				toda captura a una torre que que se encuentra en si posicion inicial hacen perder castling
//		cada vez que se cambia la jerarquia o hay algun tipo de modificacion en estos objetos las clases de pruebas necesitan ser actualizadas

//TODO: implement bridge pattern.

/**
 * @author Mauricio Coria
 *
 */
public interface Move extends Comparable<Move> {
	PiecePositioned getFrom();
	PiecePositioned getTo();
	
	void executeMove(ChessPositionWriter chessPosition);
	void undoMove(ChessPositionWriter chessPosition);
	
	boolean filter(MoveFilter filter);	

	void executeMove(PiecePlacementWritter board);
	void undoMove(PiecePlacementWritter board);
	
	void executeMove(PositionState positionState);
	void undoMove(PositionState positionState);

	void executeMove(ColorBoard coloBoard);
	void undoMove(ColorBoard colorBoard);
	
	void executeMove(MoveCacheBoard moveCache);
	void undoMove(MoveCacheBoard moveCache);
}