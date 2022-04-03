package chess.board.debug.chess;

import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.legalmovesgenerators.strategies.DefaultLegalMoveGenerator;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.MoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class DefaultLegalMoveGeneratorDebug extends DefaultLegalMoveGenerator {

	public DefaultLegalMoveGeneratorDebug(ChessPositionReader positionReader, MoveGenerator strategy, MoveFilter filter) {
		super(positionReader, strategy, filter);
	}
	
	//TODO: reimplement method
	/*
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
	}*/
	

}
