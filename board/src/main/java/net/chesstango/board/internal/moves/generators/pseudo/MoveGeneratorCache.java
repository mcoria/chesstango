package net.chesstango.board.internal.moves.generators.pseudo;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.MoveCacheBoard;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorCache implements MoveGenerator {

	protected final MoveGenerator moveGenerator;
	
	protected final MoveCacheBoard moveCache;
	

	public MoveGeneratorCache(MoveGenerator implementation, MoveCacheBoard moveCache) {
		this.moveGenerator = implementation;
		this.moveCache = moveCache;
	}	


	@Override
	public MoveGeneratorByPieceResult generateByPiecePseudoMoves(PiecePositioned from) {
		Square origenSquare = from.square();
		
		MoveGeneratorByPieceResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
		
		if (generatorResult == null) {
			
			generatorResult = moveGenerator.generateByPiecePseudoMoves(from);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		}
		
		return generatorResult;
	}
	
	@Override
	public MovePair<PseudoMove> generateEnPassantPseudoMoves() {
		return moveGenerator.generateEnPassantPseudoMoves();
	}


	@Override
	public MovePair<PseudoMove> generateCastlingPseudoMoves() {
		return moveGenerator.generateCastlingPseudoMoves();
	}
}
