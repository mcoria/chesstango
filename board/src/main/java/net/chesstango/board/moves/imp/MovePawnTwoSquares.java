package net.chesstango.board.moves.imp;

import net.chesstango.board.Color;
import net.chesstango.board.GameImp;
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
    public void doMove(PositionStateWriter positionState) {
        positionState.pushState();

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
    public void doMove(ZobristHashWriter hash) {
        hash.pushState();

        hash.xorPosition(from);

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        hash.clearEnPassantSquare();

        ChessPositionReader chessPositionReader = gameImp.getChessPosition();

        if (enPassantSquare.equals(chessPositionReader.getEnPassantSquare())) {
            Square leftSquare = Square.getSquare(to.getSquare().getFile() - 1, to.getSquare().getRank());
            Square rightSquare = Square.getSquare(to.getSquare().getFile() + 1, to.getSquare().getRank());
            if (leftSquare != null && from.getPiece().getOpposite().equals(chessPositionReader.getPiece(leftSquare)) ||
                    rightSquare != null && from.getPiece().getOpposite().equals(chessPositionReader.getPiece(rightSquare))) {
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
