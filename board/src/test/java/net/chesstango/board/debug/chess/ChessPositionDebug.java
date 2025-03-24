package net.chesstango.board.debug.chess;

import net.chesstango.board.Square;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.factories.imp.MoveFactoryBlack;
import net.chesstango.board.moves.factories.imp.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.generators.pseudo.imp.MoveGeneratorImp;
import net.chesstango.board.position.ChessPositionVisitor;
import net.chesstango.board.position.imp.ChessPositionImp;
import net.chesstango.board.representations.ascii.ASCIIEncoder;


/**
 * @author Mauricio Coria
 */
public class ChessPositionDebug extends ChessPositionImp {

    private MoveGeneratorImp moveGeneratorImp;


    @Override
    public void doMove(ChessPositionVisitor move) {
        super.doMove(move);
        ((PositionStateDebug) positionState).validar(this.squareBoard);
        ((BitBoardDebug) bitBoard).validar(this.squareBoard);
        ((KingSquareDebug) kingSquare).validar(this.squareBoard);
        ((MoveCacheBoardDebug) moveCache).validar(this.squareBoard);
        ((ZobristHashDebug) zobristHash).validar(this);
    }

    @Override
    public void undoMove(ChessPositionVisitor move) {
        super.undoMove(move);
        ((PositionStateDebug) positionState).validar(this.squareBoard);
        ((BitBoardDebug) bitBoard).validar(this.squareBoard);
        ((KingSquareDebug) kingSquare).validar(this.squareBoard);
        ((MoveCacheBoardDebug) moveCache).validar(this.squareBoard);
        ((ZobristHashDebug) zobristHash).validar(this);
        validar(getMoveGeneratorImp());
    }

    @Override
    public void init() {
        super.init();
        ((PositionStateDebug) positionState).validar(this.squareBoard);
        ((BitBoardDebug) bitBoard).validar(this.squareBoard);
        ((KingSquareDebug) kingSquare).validar(this.squareBoard);
        ((MoveCacheBoardDebug) moveCache).validar(this.squareBoard);
    }

    @Override
    public String toString() {
        ASCIIEncoder asciiEncoder = new ASCIIEncoder();
        constructChessPositionRepresentation(asciiEncoder);

        return super.toString() + "\n" + asciiEncoder.getChessRepresentation();
    }


    private void validar(MoveGeneratorImp moveGeneratorImp) {
        for (int i = 0; i < 64; i++) {
            Square square = Square.getSquareByIdx(i);
            MoveGeneratorResult cacheMoveGeneratorResult = moveCache.getPseudoMovesResult(square);
            if (cacheMoveGeneratorResult != null) {
                MoveGeneratorResult expectedMoveGeneratorResults = moveGeneratorImp.generatePseudoMoves(squareBoard.getPosition(square));
                assertMoveGeneratorResults(expectedMoveGeneratorResults, cacheMoveGeneratorResult);
            }
        }
    }

    private void assertMoveGeneratorResults(MoveGeneratorResult expectedMoveGeneratorResults,
                                            MoveGeneratorResult cacheMoveGeneratorResult) {

        MoveList expectedPseudoMoves = expectedMoveGeneratorResults.getPseudoMoves();

        MoveList cachePseudoMoves = cacheMoveGeneratorResult.getPseudoMoves();

        if (expectedPseudoMoves.size() != cachePseudoMoves.size()) {
            throw new RuntimeException("Hay inconsistencia en el cache de movimientos pseudo");
        }

        if (expectedMoveGeneratorResults.getAffectedByPositions() != cacheMoveGeneratorResult.getAffectedByPositions()) {
            throw new RuntimeException("AffectedBy no coinciden");
        }
    }

    private MoveGeneratorImp getMoveGeneratorImp() {
        if (moveGeneratorImp == null) {
            moveGeneratorImp = new MoveGeneratorImp(new MoveFactoryWhite(), new MoveFactoryBlack());
            moveGeneratorImp.setSquareBoardReader(this.squareBoard);
            moveGeneratorImp.setBoardState(this.positionState);
            moveGeneratorImp.setBitBoardReader(this.bitBoard);
        }
        return moveGeneratorImp;
    }

}