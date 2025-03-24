package net.chesstango.board.moves.generators.pseudo.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.imp.MoveCommandImp;
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
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
		Square origenSquare = origen.getSquare();
		
		MoveGeneratorResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
		
		if (generatorResult == null) {
			
			generatorResult = moveGenerator.generatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		}
		
		return generatorResult;
	}
	
	@Override
	public MovePair<MoveCommandImp> generateEnPassantPseudoMoves() {
		return moveGenerator.generateEnPassantPseudoMoves();
	}


	@Override
	public MovePair<MoveCommandImp> generateCastlingPseudoMoves() {
		return moveGenerator.generateCastlingPseudoMoves();
	}
}
