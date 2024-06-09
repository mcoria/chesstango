package net.chesstango.board.moves.imp;

import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCastling;

/**
 * @author Mauricio Coria
 */
public class MoveFactoryWhite extends MoveFactoryAbstract {

    private static final MoveCastlingImp castlingKingMove = new MoveCastlingWhiteKing();
    private static final MoveCastlingImp castlingQueenMove = new MoveCastlingWhiteQueen();

    public MoveFactoryWhite() {
        super(new AlgoPositionStateWhite());
    }

    @Override
    public MoveCastlingImp createCastlingQueenMove() {
        return castlingQueenMove;
    }

    @Override
    public MoveCastlingImp createCastlingKingMove() {
        return castlingKingMove;
    }

    @Override
    protected Cardinal getPawnDirection() {
        return Cardinal.Norte;
    }
}
