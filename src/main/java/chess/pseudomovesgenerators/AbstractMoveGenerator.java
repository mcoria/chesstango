package chess.pseudomovesgenerators;

import chess.Color;
import chess.PiecePositioned;
import chess.moves.MoveFactory;
import chess.position.ColorBoard;
import chess.position.PiecePlacement;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected final Color color;
	
	protected PiecePlacement tablero;

	protected ColorBoard colorBoard;	
	
	protected MoveFactory moveFactory;
	
	protected MoveGeneratorResult result;
	
	public abstract void generateMovesPseudoMoves(PiecePositioned origen);
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}
	
	//TODO: revisar como estamos haciendo el settup de MoveGeneratorResult(), quizas conviene un metodo abstracto
	@Override
	public MoveGeneratorResult calculatePseudoMoves(PiecePositioned origen){
		this.result = new MoveGeneratorResult();
		generateMovesPseudoMoves(origen);
		return this.result;
	}

	public void setTablero(PiecePlacement tablero) {
		this.tablero = tablero;
	}	

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setMoveFactory(MoveFactory moveFactoryImp) {
		this.moveFactory = moveFactoryImp;
	}
	
}
