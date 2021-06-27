package movegenerators;

import chess.Color;
import chess.PosicionPieza;
import layers.PosicionPiezaBoard;

// Y si tenemos objetos prototipos de movimientos y lo clonamos de ser validos?
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected final Color color;
	
	protected PosicionPiezaBoard tablero;
	
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

	@Override
	public void setTablero(PosicionPiezaBoard tablero) {
		this.tablero = tablero;
	}
	
}
