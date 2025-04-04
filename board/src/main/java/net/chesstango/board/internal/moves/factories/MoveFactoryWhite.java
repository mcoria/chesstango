package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.internal.GameImp;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.MoveCastlingImp;
import net.chesstango.board.internal.moves.MoveCastlingWhiteKing;
import net.chesstango.board.internal.moves.MoveCastlingWhiteQueen;

/**
 * @author Mauricio Coria
 */
public class MoveFactoryWhite extends MoveFactoryAbstract {

    private final MoveCastlingImp castlingKingMove;
    private final MoveCastlingImp castlingQueenMove;

    public MoveFactoryWhite(){
        this(null);
    }

    public MoveFactoryWhite(GameImp gameImp) {
        super(gameImp, new AlgoPositionStateWhite());
        this.castlingKingMove = new MoveCastlingWhiteKing(gameImp);
        this.castlingQueenMove = new MoveCastlingWhiteQueen(gameImp);
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
