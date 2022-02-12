package chess.pseudomovesgenerators.strategies;

import chess.Color;
import chess.PiecePositioned;
import chess.moves.MoveFactory;
import chess.position.ColorBoard;
import chess.position.PiecePlacementReader;
import chess.pseudomovesgenerators.MoveGeneratorByPiecePositioned;
import chess.pseudomovesgenerators.MoveGeneratorResult;


/**
 * @author Mauricio Coria
 *
 */
//TODO: implementar el calculo de movimientos lo puede hacer en funcion de ColorBoard
public abstract class AbstractMoveGenerator implements MoveGeneratorByPiecePositioned {
	
	protected final Color color;
	
	protected PiecePlacementReader piecePlacement;

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

	public void setPiecePlacement(PiecePlacementReader piecePlacement) {
		this.piecePlacement = piecePlacement;
	}	

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setMoveFactory(MoveFactory moveFactory) {
		this.moveFactory = moveFactory;
	}
	
}
