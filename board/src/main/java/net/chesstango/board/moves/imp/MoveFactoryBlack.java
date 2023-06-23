package net.chesstango.board.moves.imp;

import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCastling;

/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryBlack extends MoveFactoryAbstract{

    private static final MoveCastling castlingKingMove = new MoveCastlingBlackKing();
    private static final MoveCastling castlingQueenMove = new MoveCastlingBlackQueen();

    public MoveFactoryBlack() {
        super(new AlgoPositionStateBlack());
    }

    @Override
    public MoveCastling createCastlingQueenMove() {
        return castlingQueenMove;
    }

    @Override
    public MoveCastling createCastlingKingMove() {
        return castlingKingMove;
    }

    @Override
    protected Cardinal getPawnDirection() {
        return Cardinal.Sur;
    }
}
