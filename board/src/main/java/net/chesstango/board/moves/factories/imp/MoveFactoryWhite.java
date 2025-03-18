package net.chesstango.board.moves.factories.imp;

import net.chesstango.board.GameImp;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.imp.MoveCastlingImp;
import net.chesstango.board.moves.imp.MoveCastlingWhiteKing;
import net.chesstango.board.moves.imp.MoveCastlingWhiteQueen;

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
