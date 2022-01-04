package movegenerators;

import chess.Color;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import moveexecutors.Move;
import moveexecutors.MoveFactory;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected final Color color;
	
	protected PosicionPiezaBoard tablero;

	protected ColorBoard colorBoard;	
	
	protected MoveGeneratorResult result;
	
	protected MoveFactory moveFactory;
	
	protected abstract Move createSimpleMove(PosicionPieza origen, PosicionPieza destino);
	
	protected abstract Move createCaptureMove(PosicionPieza origen, PosicionPieza destino);
	
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
