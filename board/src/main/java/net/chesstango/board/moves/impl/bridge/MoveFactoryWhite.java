package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCastling;

/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryWhite extends  MoveFactoryAbstract{

    private static final MoveCastling castlingKingMove = new MoveCastlingWhiteKing();
    private static final MoveCastling castlingQueenMove = new MoveCastlingWhiteQueen();

    public MoveFactoryWhite() {
        super(new AlgoPositionStateWhite());
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
        return Cardinal.Norte;
    }
}
