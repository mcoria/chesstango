package chess.movesgenerators;

import chess.Color;
import chess.PosicionPieza;
import chess.layers.ColorBoard;
import chess.layers.PosicionPiezaBoard;
import chess.moves.MoveFactory;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected final Color color;
	
	protected PosicionPiezaBoard tablero;

	protected ColorBoard colorBoard;	
	
	protected MoveFactory moveFactory;
	
	protected MoveGeneratorResult result;
	
	public abstract void generateMovesPseudoMoves(PosicionPieza origen);
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}
	
	//TODO: revisar como estamos haciendo el settup de MoveGeneratorResult(), quizas conviene un metodo abstracto
	@Override
	public MoveGeneratorResult calculatePseudoMoves(PosicionPieza origen){
		this.result = new MoveGeneratorResult();
		generateMovesPseudoMoves(origen);
		return this.result;
	}

	public void setTablero(PosicionPiezaBoard tablero) {
		this.tablero = tablero;
	}	

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setMoveFactory(MoveFactory moveFactory) {
		this.moveFactory = moveFactory;
	}
	
}
