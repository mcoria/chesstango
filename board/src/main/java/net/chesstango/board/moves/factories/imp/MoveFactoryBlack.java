package net.chesstango.board.moves.factories.imp;

import net.chesstango.board.GameImp;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.imp.MoveCastlingBlackKing;
import net.chesstango.board.moves.imp.MoveCastlingBlackQueen;
import net.chesstango.board.moves.imp.MoveCastlingImp;

/**
 * @author Mauricio Coria
 */
public class MoveFactoryBlack extends MoveFactoryAbstract {

    private final MoveCastlingImp castlingKingMove;
    private final MoveCastlingImp castlingQueenMove;

    public MoveFactoryBlack() {
        super(new AlgoPositionStateBlack());
        this.castlingKingMove = new MoveCastlingBlackKing(gameImp);
        this.castlingQueenMove = new MoveCastlingBlackQueen(gameImp);
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
