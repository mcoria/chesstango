package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 * Esta clase no se utiliza por el momento
 */
public class CapturedPositionsAnalyzer implements Analyzer {
    private final ChessPositionReader positionReader;

    protected final MoveGenerator pseudoMovesGenerator;

    public CapturedPositionsAnalyzer(ChessPositionReader positionReader, MoveGenerator pseudoMovesGenerator) {
        this.positionReader = positionReader;
        this.pseudoMovesGenerator = pseudoMovesGenerator;
    }

    @Override
    public void analyze(AnalyzerResult result) {
        result.setCapturedPositions(getCapturedPositionsOponente());
    }

    //TODO: Esta complicado este metodo, se pierde demasiada performance
	/*
	protected void getCastlingMoves(Collection<Move> moves) {
		Collection<MoveCastling> pseudoMoves = pseudoMovesGenerator.generateCastlingPseudoMoves();
		long capturedPositionsOponente = this.getCapturedPositionsOponente();
		for (MoveCastling move : pseudoMoves) {
			long posicionesRey = (move.getRookMove().getTo().getKey().getPosicion())
					| (move.getTo().getKey().getPosicion());
			if ((capturedPositionsOponente & posicionesRey) == 0) {
				moves.add(move);
			}
		}
	}
	 */

    //TODO: este metodo no tien buena performance
    protected long getCapturedPositionsOponente(){
        final Color turnoActual = this.positionReader.getCurrentTurn();

        long posicionesCapturadas = 0;

        for (SquareIterator iterator = positionReader.iteratorSquare( turnoActual.oppositeColor() ); iterator.hasNext();) {

            Square origenSquare = iterator.next();

            MoveGeneratorResult generatorResult = pseudoMovesGenerator.generatePseudoMoves(positionReader.getPosition(origenSquare));

            posicionesCapturadas |= generatorResult.getCapturedPositions();

        }

        return posicionesCapturadas;
    }
}
