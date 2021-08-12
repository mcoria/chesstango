package debug.chess;

import java.util.Collection;

import chess.BoardState;
import chess.PosicionPieza;
import chess.Square;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import layers.imp.ArrayPosicionPiezaBoard;
import movecalculators.MoveFilter;
import movecalculators.NoCheckLegalMoveCalculator;
import moveexecutors.Move;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;

public class NoCheckLegalMoveCalculatorDebug extends NoCheckLegalMoveCalculator{

	public NoCheckLegalMoveCalculatorDebug(PosicionPiezaBoard dummyBoard, KingCacheBoard kingCacheBoard,
			ColorBoard colorBoard, MoveCacheBoard moveCache, BoardState boardState, MoveGeneratorStrategy strategy, MoveFilter filter) {
		super(dummyBoard, kingCacheBoard, colorBoard, moveCache, boardState, strategy, filter);
	}
	

	@Override
	public Collection<Move> getLegalMoves() {
		try {
			boolean reportError = false;
			
			ArrayPosicionPiezaBoard boardInicial = ((ArrayPosicionPiezaBoard) super.dummyBoard).clone();
			
			KingCacheBoard kingCacheBoardInicial = super.kingCacheBoard.clone();
			
			BoardState boardStateInicial = super.boardState.clone();
	
			Collection<Move> result = super.getLegalMoves();
			
			if (!super.boardState.equals(boardStateInicial)) {
				System.out.println("El estado fué modificado");
				System.out.println("Inicial [" + boardStateInicial.toString() + "]\n" + "Final   [" + super.boardState.toString() + "]\n");
				reportError = true;				
			}			
			
			if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de rey fué modificado");
				System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + super.kingCacheBoard.toString() + "]\n");
				reportError = true;
			}
	
			if (!super.dummyBoard.equals(boardInicial)) {
				System.out.println("El board fué modificado");
				System.out.println("Inicial:\n" + boardInicial.toString() + "\n" + "Final:\n" + super.dummyBoard.toString());
				reportError = true;				
			}
	
			if (reportError) {
				throw new RuntimeException("Hubo modificaciones ! ! !");
			}
			
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected MoveGeneratorResult getPseudoMovesResult(Square origenSquare) {
		MoveGeneratorResult generatorResultCache = moveCache.getPseudoMovesResult(origenSquare);
		
		if (generatorResultCache != null) {
	
			PosicionPieza origen = dummyBoard.getPosicion(origenSquare);
	
			MoveGenerator moveGenerator =  strategy.getMoveGenerator(origen.getValue());
											//origen.getValue().getMoveGenerator(strategy); Mala performance
	
			MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
	
			// comenzar comparaciones
			if(generatorResultCache.getPseudoMoves().size() != generatorResult.getPseudoMoves().size()) {
				throw new RuntimeException("La cantidad de movimientos pseudo no concide");
			}
			
			if(generatorResultCache.getAffectedBy() != generatorResult.getAffectedBy()) {
				throw new RuntimeException("AffectedBy es distinto");
			}			
			
			return generatorResultCache;
		}
		
		return super.getPseudoMovesResult(origenSquare);
	}

}
