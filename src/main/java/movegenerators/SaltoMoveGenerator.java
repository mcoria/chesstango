package movegenerators;

import java.util.Collection;
import java.util.Iterator;

import chess.Color;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;
import iterators.SaltoSquareIterator;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;

public abstract class SaltoMoveGenerator extends AbstractMoveGenerator {
	
	protected Color color;
	
	private int[][] saltos;
	
	protected abstract SimpleMove createSimpleMove(PosicionPieza origen, PosicionPieza destino);
	
	protected abstract CaptureMove createCaptureMove(PosicionPieza origen, PosicionPieza destino);
	
	protected abstract void addMoveIfValid(PosicionPieza origen, PosicionPieza destino, Collection<Move> moveContainer);
	
	public SaltoMoveGenerator(Color color, int[][] saltos) {
		this.color = color;
		this.saltos = saltos;
	}

	@Override
	public void generateMoves(PosicionPieza origen, Collection<Move> moveContainer) {
		Square casillero = origen.getKey();
		Iterator<PosicionPieza> iterator = tablero.iterator(new SaltoSquareIterator(casillero, saltos));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    addMoveIfValid(origen, destino, moveContainer);
		}
	}

	@Override
	public boolean puedeCapturarRey(PosicionPieza origen, Square kingSquare) {
		Square squareOrigen = origen.getKey();
		int fileOrigen = squareOrigen.getFile();
		int rankOrigen = squareOrigen.getRank();
		
		int fileKing = kingSquare.getFile();
		int rankKing = kingSquare.getRank();

		for (int i = 0; i < saltos.length; i++) {
			if(fileOrigen + saltos[i][0] == fileKing && rankOrigen + saltos[i][1] == rankKing) {
				return true;
			}
				
		}
		return false;		
	}

}
