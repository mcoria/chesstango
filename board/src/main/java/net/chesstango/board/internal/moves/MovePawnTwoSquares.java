package net.chesstango.board.internal.moves;

import net.chesstango.board.Color;
import net.chesstango.board.internal.GameImp;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public class MovePawnTwoSquares extends MoveImp {
    protected final Square enPassantSquare;

    public MovePawnTwoSquares(GameImp gameImp, PiecePositioned from, PiecePositioned to, Cardinal direction, Square enPassantSquare) {
        super(gameImp, from, to, direction);
        this.enPassantSquare = enPassantSquare;
    }

    @Override
    public void doMove(SquareBoardWriter squareBoard) {
        squareBoard.move(from, to);
    }

    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        squareBoard.setPosition(from);
        squareBoard.setPosition(to);
    }

    @Override
    public void doMove(PositionState positionState) {
        positionStateSnapshot = positionState.takeSnapshot();

        positionState.setEnPassantSquare(enPassantSquare);

        positionState.resetHalfMoveClock();

        if (Color.BLACK.equals(from.getPiece().getColor())) {
            positionState.incrementFullMoveClock();
        }

        positionState.rollTurn();
    }

    @Override
    public void doMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), from.getSquare(), to.getSquare());
    }

    @Override
    public void undoMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), to.getSquare(), from.getSquare());
    }

    @Override
    public void doMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), enPassantSquare);
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), enPassantSquare);
        moveCache.pop();
    }

    @Override
    public void doMove(KingSquareWriter kingSquare) {
    }

    @Override
    public void undoMove(KingSquareWriter kingSquare) {
    }

    @Override
    public void doMove(ZobristHash hash) {
        zobristHashSnapshot = hash.takeSnapshot();

        hash.xorPosition(from);

        hash.xorPosition(PiecePositioned.of(to.getSquare(), from.getPiece()));

        hash.clearEnPassantSquare();

        PositionReader positionReader = gameImp.getPosition();

        if (enPassantSquare.equals(positionReader.getEnPassantSquare())) {
            Square leftSquare = Square.of(to.getSquare().getFile() - 1, to.getSquare().getRank());
            Square rightSquare = Square.of(to.getSquare().getFile() + 1, to.getSquare().getRank());
            if (leftSquare != null && from.getPiece().getOpposite().equals(positionReader.getPiece(leftSquare)) ||
                    rightSquare != null && from.getPiece().getOpposite().equals(positionReader.getPiece(rightSquare))) {
                hash.xorEnPassantSquare(enPassantSquare);
            }
        }

        hash.xorTurn();
    }

    @Override
    public boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMovePawn(this, this);
    }
}
