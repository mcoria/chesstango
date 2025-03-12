package net.chesstango.board.moves.factories.imp;

import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.imp.AlgoPositionStateBlack;
import net.chesstango.board.moves.imp.MoveCastlingBlackKing;
import net.chesstango.board.moves.imp.MoveCastlingBlackQueen;
import net.chesstango.board.moves.imp.MoveCastlingImp;

/**
 * @author Mauricio Coria
 */
public class MoveFactoryBlack extends MoveFactoryAbstract {

    private static final MoveCastlingImp castlingKingMove = new MoveCastlingBlackKing();
    private static final MoveCastlingImp castlingQueenMove = new MoveCastlingBlackQueen();

    public MoveFactoryBlack() {
        super(new AlgoPositionStateBlack());
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
        return Cardinal.Sur;
    }
}
