package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.internal.GameImp;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.MoveCastlingBlackKing;
import net.chesstango.board.internal.moves.MoveCastlingBlackQueen;
import net.chesstango.board.internal.moves.MoveCastlingImp;

/**
 * @author Mauricio Coria
 */
public class MoveFactoryBlack extends MoveFactoryAbstract {

    private final MoveCastlingImp castlingKingMove;
    private final MoveCastlingImp castlingQueenMove;

    public MoveFactoryBlack(){
        this(null);
    }

    public MoveFactoryBlack(GameImp gameImp) {
        super(gameImp, new AlgoPositionStateBlack());
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
