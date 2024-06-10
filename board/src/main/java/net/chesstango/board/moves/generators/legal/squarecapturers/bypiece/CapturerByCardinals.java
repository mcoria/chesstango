package net.chesstango.board.moves.generators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.BitBoardReader;
import net.chesstango.board.position.SquareBoardReader;

/**
 * @author Mauricio Coria
 */
public abstract class CapturerByCardinals implements CapturerByPiece {

    protected final SquareBoardReader squareBoardReader;
    protected final BitBoardReader bitBoardReader;
    protected final Cardinal[] cardinals;

    protected final Color color;

    protected abstract long getCardinalThreats();

    public CapturerByCardinals(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader, Color color, Cardinal[] cardinals) {
        this.squareBoardReader = squareBoardReader;
        this.bitBoardReader = bitBoardReader;
        this.cardinals = cardinals;
        this.color = color;
    }

    @Override
    public boolean positionCaptured(Square square, long possibleThreats) {
        for (Cardinal cardinal : cardinals) {
            if (positionCapturedByCardinal(square, cardinal, possibleThreats)) {
                return true;
            }
        }
        return false;
    }


    private boolean positionCapturedByCardinal(Square square, Cardinal cardinal, long possibleThreats) {
        long cardinalThreats = getCardinalThreats();

        long squaresToThreat = cardinal.getSquaresInDirection(square);

        long possibleThreatsInCardinalDirection = possibleThreats & cardinalThreats & squaresToThreat;

        if(possibleThreatsInCardinalDirection != 0) {
            while (possibleThreatsInCardinalDirection != 0) {
                long posicionLng = Long.lowestOneBit(possibleThreatsInCardinalDirection);

                Square threat = Square.getSquareByIdx(Long.numberOfTrailingZeros(posicionLng));

                long squaresFromThreat = cardinal.getOpposite().getSquaresInDirection(threat);

                long betweenSquares = squaresToThreat & squaresFromThreat;

                if (betweenSquares == 0) {
                    return true;
                } else {
                    long intersection = betweenSquares & bitBoardReader.getEmptyPositions();
                    if (intersection == betweenSquares) {
                        return true;
                    }
                }

                possibleThreatsInCardinalDirection &= ~posicionLng;
            }
        }

        return false;
    }
}
