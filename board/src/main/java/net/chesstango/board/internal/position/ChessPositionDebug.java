package net.chesstango.board.internal.position;

import net.chesstango.board.Square;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.internal.moves.factories.MoveFactoryBlack;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.internal.moves.generators.pseudo.MoveGeneratorImp;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.representations.ascii.ASCIIEncoder;


/**
 * @author Mauricio Coria
 */
public class ChessPositionDebug extends ChessPositionImp {

    private MoveGeneratorImp moveGeneratorImp;


    // LLAMAR A ESTE METODO UNA VEZ QUE SE EJECUTO EL MOVIMIENTO
    public void validar() {
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
            MoveGeneratorByPieceResult cacheMoveGeneratorResult = moveCache.getPseudoMovesResult(square);
            if (cacheMoveGeneratorResult != null) {
                MoveGeneratorByPieceResult expectedMoveGeneratorResults = moveGeneratorImp.generateByPiecePseudoMoves(squareBoard.getPosition(square));
                assertMoveGeneratorResults(expectedMoveGeneratorResults, cacheMoveGeneratorResult);
            }
        }
    }

    private void assertMoveGeneratorResults(MoveGeneratorByPieceResult expectedMoveGeneratorResults,
                                            MoveGeneratorByPieceResult cacheMoveGeneratorResult) {

        MoveList<PseudoMove> expectedPseudoMoves = expectedMoveGeneratorResults.getPseudoMoves();

        MoveList<PseudoMove> cachePseudoMoves = cacheMoveGeneratorResult.getPseudoMoves();

        if (expectedPseudoMoves.size() != cachePseudoMoves.size()) {
            throw new RuntimeException("Hay inconsistencia en el cache de movimientos pseudo");
        }

        if (expectedMoveGeneratorResults.getAffectedByPositions() != cacheMoveGeneratorResult.getAffectedByPositions()) {
            throw new RuntimeException("AffectedBy no coinciden");
        }
    }

    private MoveGeneratorImp getMoveGeneratorImp() {
        if (moveGeneratorImp == null) {
            moveGeneratorImp = new MoveGeneratorImp();
            moveGeneratorImp.setSquareBoardReader(this.squareBoard);
            moveGeneratorImp.setBoardState(this.positionState);
            moveGeneratorImp.setBitBoardReader(this.bitBoard);
            moveGeneratorImp.setMoveFactoryWhite(new MoveFactoryWhite());
            moveGeneratorImp.setMoveFactoryBlack(new MoveFactoryBlack());
        }
        return moveGeneratorImp;
    }

}