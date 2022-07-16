package net.chesstango.ai.imp.smart.evaluation.imp;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorImp02 implements GameEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 2110;
    private static final int FACTOR_ATAQUE_DEFAULT = 601;

    private final int material;
    private final int ataque;

    public GameEvaluatorImp02() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_ATAQUE_DEFAULT);
    }

    public GameEvaluatorImp02(int material, int ataque) {
        this.material = material;
        this.ataque = ataque;
    }

    @Override
    public int evaluate(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
            case DRAW:
                evaluation = GameEvaluator.evaluateFinalStatus(game);
                break;
            case CHECK:
                // If white is on check then evaluation starts at -1
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? -1 : +1;
            case NO_CHECK:
                evaluation += material * GameEvaluator.evaluateByMaterial(game);
                evaluation += ataque * evaluateByAttack(game);
        }
        return evaluation;
    }

    protected int evaluateByAttack(final Game game) {
        int evaluation = 0;

        ChessPositionReader positionReader = game.getChessPosition();

        MoveGenerator pseudoMovesGenerator = game.getPseudoMovesGenerator();

        Iterator<PiecePositioned> iteratorAllPieces = positionReader.iteratorAllPieces();

        while(iteratorAllPieces.hasNext()){
            PiecePositioned piecePositioned = iteratorAllPieces.next();

            MoveGeneratorResult generationResult = pseudoMovesGenerator.generatePseudoMoves(piecePositioned);

            MoveList pseudoMoves = generationResult.getPseudoMoves();

            for (Move move: pseudoMoves) {
                PiecePositioned toPosition = move.getTo();
                if (toPosition.getValue() != null) {
                    evaluation += toPosition.getValue().getPieceValue();
                }
            }
        }

        // From white point of view
        return  -  evaluation;
    }

}
