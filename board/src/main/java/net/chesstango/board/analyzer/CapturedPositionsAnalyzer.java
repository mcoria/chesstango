package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.SquareIterator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.PositionReader;

/**
 * @author Mauricio Coria
 * <p>
 * Esta clase no se utiliza por el momento
 */
public class CapturedPositionsAnalyzer implements Analyzer {
    private final PositionReader positionReader;

    protected final MoveGenerator pseudoMovesGenerator;

    public CapturedPositionsAnalyzer(PositionReader positionReader, MoveGenerator pseudoMovesGenerator) {
        this.positionReader = positionReader;
        this.pseudoMovesGenerator = pseudoMovesGenerator;
    }

    @Override
    public void analyze(AnalyzerResult result) {
        result.setCapturedPositions(getCapturedPositionsOponente());
    }


    //TODO: este metodo no tien buena performance
    protected long getCapturedPositionsOponente() {
        final Color turnoActual = this.positionReader.getCurrentTurn();

        long posicionesCapturadas = 0;

        for (SquareIterator iterator = positionReader.iteratorSquare(turnoActual.oppositeColor()); iterator.hasNext(); ) {

            Square origenSquare = iterator.next();

            MoveGeneratorByPieceResult generatorResult = pseudoMovesGenerator.generateByPiecePseudoMoves(positionReader.getPosition(origenSquare));

            posicionesCapturadas |= generatorResult.getCapturedPositions();

        }

        return posicionesCapturadas;
    }
}
