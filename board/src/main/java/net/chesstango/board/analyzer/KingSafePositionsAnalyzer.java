package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.iterators.bysquare.PositionsSquareIterator;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class KingSafePositionsAnalyzer implements Analyzer {

    private final ChessPositionReader positionReader;
    private final KingSafePositionsAnalyzerByColor analyzerWhite;
    private final KingSafePositionsAnalyzerByColor analyzerBlack;

    public KingSafePositionsAnalyzer(ChessPositionReader positionReader) {
        this.positionReader = positionReader;
        this.analyzerWhite = new KingSafePositionsAnalyzerByColor(Color.WHITE);
        this.analyzerBlack = new KingSafePositionsAnalyzerByColor(Color.BLACK);
    }

    @Override
    public void analyze(AnalyzerResult result) {
        Color currentTurn = positionReader.getCurrentTurn();

        if (Color.WHITE.equals(currentTurn)) {
            analyzerWhite.analyze(result);
        } else {
            analyzerBlack.analyze(result);
        }

        Square kingSquare = positionReader.getKingSquare(currentTurn);
        long safeKingPositions = result.getSafeKingPositions();
        boolean kingsInCheck = (safeKingPositions & kingSquare.getBitPosition()) == 0;

        result.setKingInCheck(kingsInCheck);
    }

    private class KingSafePositionsAnalyzerByColor implements Analyzer {
        private final Color color;

        private KingSafePositionsAnalyzerByColor(Color color) {
            this.color = color;
        }

        @Override
        public void analyze(AnalyzerResult result) {
            long searchKnightsAt = 0;
            long searchPawnsAt = 0;

            long searchNorte = 0;
            long searchSur = 0;
            long searchEste = 0;
            long searchOeste = 0;
            long searchNorteEste = 0;
            long searchNorteOeste = 0;
            long searchSurEste = 0;
            long searchSurOeste = 0;

            Square squareKing = positionReader.getKingSquare(color);

            long kingJumps = squareKing.getKingJumps();

            // Todas las posiciones donde podria saltar el rey incluida la posicion actual;
            long possibleKingPositions = (kingJumps & ~positionReader.getPositions(color)) | squareKing.getBitPosition();

            PositionsSquareIterator possibleKingPositionsIt = new PositionsSquareIterator(possibleKingPositions);
            while (possibleKingPositionsIt.hasNext()) {
                Square possibleKingSquare = possibleKingPositionsIt.next();
                searchKnightsAt |= possibleKingSquare.getKnightJumps();
                searchPawnsAt |= possibleKingSquare.getKingJumps(); // TODO: Esto se puede mejorar para que solo busquemos posiciones Pawn

                searchNorte |= Cardinal.Norte.getSquaresInDirection(possibleKingSquare);
                searchSur |= Cardinal.Sur.getSquaresInDirection(possibleKingSquare);
                searchEste |= Cardinal.Este.getSquaresInDirection(possibleKingSquare);
                searchOeste |= Cardinal.Oeste.getSquaresInDirection(possibleKingSquare);

                searchNorteEste |= Cardinal.NorteEste.getSquaresInDirection(possibleKingSquare);
                searchNorteOeste |= Cardinal.NorteOeste.getSquaresInDirection(possibleKingSquare);
                searchSurEste |= Cardinal.SurEste.getSquaresInDirection(possibleKingSquare);
                searchSurOeste |= Cardinal.SurOeste.getSquaresInDirection(possibleKingSquare);
            }

            long opponentPositions = positionReader.getPositions(color.oppositeColor());

            long safePositions = possibleKingPositions;

            safePositions &= ~knightCapturedSquares(opponentPositions & positionReader.getKnightPositions() & searchKnightsAt);

            safePositions &= ~pawnCapturedSquares(opponentPositions & positionReader.getPawnPositions() & searchPawnsAt);

            safePositions &= ~positionReader.getKingSquare(color.oppositeColor()).getKingJumps();

            safePositions &= ~cardinalCapturedSquares(Cardinal.Sur, opponentPositions & (positionReader.getRookPositions() | positionReader.getQueenPositions())  & searchNorte);
            safePositions &= ~cardinalCapturedSquares(Cardinal.Norte, opponentPositions & (positionReader.getRookPositions() | positionReader.getQueenPositions())  & searchSur);
            safePositions &= ~cardinalCapturedSquares(Cardinal.Oeste, opponentPositions & (positionReader.getRookPositions() | positionReader.getQueenPositions())  & searchEste);
            safePositions &= ~cardinalCapturedSquares(Cardinal.Este, opponentPositions & (positionReader.getRookPositions() | positionReader.getQueenPositions())  & searchOeste);

            safePositions &= ~cardinalCapturedSquares(Cardinal.SurOeste, opponentPositions & (positionReader.getBishopPositions() | positionReader.getQueenPositions())  & searchNorteEste);
            safePositions &= ~cardinalCapturedSquares(Cardinal.SurEste, opponentPositions & (positionReader.getBishopPositions() | positionReader.getQueenPositions())  & searchNorteOeste);
            safePositions &= ~cardinalCapturedSquares(Cardinal.NorteOeste, opponentPositions & (positionReader.getBishopPositions() | positionReader.getQueenPositions())  & searchSurEste);
            safePositions &= ~cardinalCapturedSquares(Cardinal.NorteEste, opponentPositions & (positionReader.getBishopPositions() | positionReader.getQueenPositions())  & searchSurOeste);

            result.setSafeKingPositions(safePositions);
        }

        private long cardinalCapturedSquares(Cardinal attackerDirection, long searchFrom) {
            long result = 0;
            if (searchFrom != 0) {
                PositionsSquareIterator cardinalAttackerIt = new PositionsSquareIterator(searchFrom);
                while (cardinalAttackerIt.hasNext()) {
                    Square cardinalAttackerFrom = cardinalAttackerIt.next();
                    Iterator<Square> iterator = new CardinalSquareIterator(cardinalAttackerFrom, attackerDirection);
                    while (iterator.hasNext()) {
                        Square to = iterator.next();
                        result |= to.getBitPosition();
                        if (positionReader.getColor(to) == null || positionReader.getKingSquare(color).equals(to)) {
                            continue;
                        }
                        break;
                    }
                }
            }
            return result;
        }

        private long knightCapturedSquares(long searchKnightsAt) {
            long result = 0;
            if (searchKnightsAt != 0) {
                PositionsSquareIterator possibleKnightPositionsIt = new PositionsSquareIterator(searchKnightsAt);
                while (possibleKnightPositionsIt.hasNext()) {
                    Square possibleKnightSquare = possibleKnightPositionsIt.next();
                    result |= possibleKnightSquare.getKnightJumps();
                }
            }
            return result;
        }

        private long pawnCapturedSquares(long searchPawnsAt) {
            long result = 0;
            if (searchPawnsAt != 0) {
                PositionsSquareIterator possiblePawnPositionsIt = new PositionsSquareIterator(searchPawnsAt);
                while (possiblePawnPositionsIt.hasNext()) {
                    Square possiblePawnSquare = possiblePawnPositionsIt.next();
                    result |= Color.WHITE.equals(color.oppositeColor()) ? possiblePawnSquare.getPawnBlackCaptureJumps() : possiblePawnSquare.getPawnWhiteCaptureJumps();
                }
            }
            return result;
        }

    }
}
