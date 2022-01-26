package chess.pseudomovesgenerators.strategies;

import chess.Color;
import chess.PiecePositioned;
import chess.moves.MoveFactory;
import chess.position.ColorBoard;
import chess.position.PiecePlacement;
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;


/**
 * @author Mauricio Coria
 *
 */
//TODO: implementar el calculo de movimientos lo puede hacer en funcion de ColorBoard
public abstract class AbstractMoveGenerator implements MoveGeneratorStrategy {
	
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
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen){
		this.result = new MoveGeneratorResult();
		this.result.setFrom(origen);
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
