package chess.debug.chess;

import java.util.Collection;

import chess.legalmovesgenerators.MoveFilter;
import chess.legalmovesgenerators.NoCheckLegalMoveGenerator;
import chess.moves.Move;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.position.imp.ArrayPiecePlacement;
import chess.pseudomovesgenerators.MoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveGeneratorDebug extends NoCheckLegalMoveGenerator{

	public NoCheckLegalMoveGeneratorDebug(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard,
			ColorBoard colorBoard, PositionState positionState, MoveGenerator strategy, MoveFilter filter) {
		super(dummyBoard, kingCacheBoard, colorBoard, positionState, strategy, filter);
	}
	

	@Override
	public Collection<Move> getLegalMoves() {
		try {
			boolean reportError = false;
			
			ArrayPiecePlacement boardInicial = ((ArrayPiecePlacement) super.dummyBoard).clone();
			
			KingCacheBoard kingCacheBoardInicial = super.kingCacheBoard.clone();
			
			PositionState boardStateInicial = super.positionState.clone();
	
			Collection<Move> result = super.getLegalMoves();
			
			if (!super.positionState.equals(boardStateInicial)) {
				System.out.println("El estado fué modificado");
				System.out.println("Inicial [" + boardStateInicial.toString() + "]\n" + "Final   [" + super.positionState.toString() + "]\n");
				reportError = true;				
			}			
			
			if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de king fué modificado");
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
	
	
	//TODO: mover a clase debug
	/*
	@Override
	protected MoveGeneratorResult getPseudoMovesResult(Square origenSquare) {
		MoveGeneratorResult generatorResultCache = moveCache.getPseudoMovesResult(origenSquare);
		
		if (generatorResultCache != null) {
	
			PiecePositioned origen = dummyBoard.getPosicion(origenSquare);
	
			MoveGeneratorResult generatorResult = pseudoMovesGenerator.generatePseudoMoves(origen);
	
			// comenzar comparaciones
			if(generatorResultCache.getPseudoMoves().size() != generatorResult.getPseudoMoves().size()) {
				throw new RuntimeException("El cache quedó en estado inconsistente");
			}
			
			if(generatorResultCache.getAffectedBy() != generatorResult.getAffectedBy()) {
				throw new RuntimeException("AffectedBy es distinto");
			}			
			
			return generatorResultCache;
		}
		
		return super.getPseudoMovesResult(origenSquare);
	}*/

}
