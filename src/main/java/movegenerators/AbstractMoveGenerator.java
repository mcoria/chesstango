package movegenerators;

import chess.Color;
import chess.Move;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;

// Y si tenemos objetos prototipos de movimientos y lo clonamos de ser validos?
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected final Color color;
	
	protected PosicionPiezaBoard tablero;

	protected ColorBoard colorBoard;	
	
	protected MoveGeneratorResult result;
	
	public abstract void generateMovesPseudoMoves(PosicionPieza origen);
	
	public abstract boolean saveMovesInCache();
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public MoveGeneratorResult calculatePseudoMoves(PosicionPieza origen){
		this.result = new MoveGeneratorResult();
		generateMovesPseudoMoves(origen);
		this.result.setSaveMovesInCache(saveMovesInCache());
		return this.result;
	}

	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino){
		return new SimpleMove(origen, destino);
	}

	protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return new CaptureMove(origen, destino);
	}

	public void setTablero(PosicionPiezaBoard tablero) {
		this.tablero = tablero;
	}


	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}	
	
}
