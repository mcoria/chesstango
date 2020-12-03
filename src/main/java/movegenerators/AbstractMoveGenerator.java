package movegenerators;

import chess.Color;
import chess.PosicionPieza;
import layers.DummyBoard;

// Y si tenemos objetos prototipos de movimientos y lo clonamos de ser validos?
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected final Color color;
	
	protected DummyBoard tablero;
	
	protected MoveGeneratorResult result;
	
	public abstract void generateMoves(PosicionPieza origen);
	
	public abstract boolean saveMovesInCache();
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public MoveGeneratorResult calculatePseudoMoves(PosicionPieza origen){
		result = new MoveGeneratorResult();
		generateMoves(origen);
		result.setSaveMovesInCache(saveMovesInCache());
		return result;
	}

	@Override
	public void setTablero(DummyBoard tablero) {
		this.tablero = tablero;
	}
	
}
