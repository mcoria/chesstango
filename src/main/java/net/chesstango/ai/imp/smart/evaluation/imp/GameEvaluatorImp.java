package net.chesstango.ai.imp.smart.evaluation.imp;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorImp implements GameEvaluator {

    //TOOD: Deberiamos codificar los siguientes genes para una poblacion inicial
    /*
        Para esta lista de juegos vs spike:
        (FENDecoder.INITIAL_FEN, "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20", "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7", "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"));
        Depth 4 vs Depth 1
        Evaluacion con gene1=[833] gene2=[352] gene3=[91] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[145] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[169] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[46] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[170] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[118] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[69] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[172] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[356] gene3=[91] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[129] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[355] gene3=[91] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[50] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[83] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[164] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[56] ; puntos = [9]
        Evaluacion con gene1=[838] gene2=[352] gene3=[66] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[102] ; puntos = [9]
        Evaluacion con gene1=[833] gene2=[352] gene3=[82] ; puntos = [9]
        Evaluacion con gene1=[825] gene2=[352] gene3=[91] ; puntos = [9]
        Evaluacion con gene1=[837] gene2=[352] gene3=[52] ; puntos = [9]

        Depth 3 vs Depth 1
        Evaluacion con gene1=[320] gene2=[268] gene3=[991] ; puntos = [7]
        Evaluacion con gene1=[320] gene2=[268] gene3=[969] ; puntos = [7]
        Evaluacion con gene1=[320] gene2=[268] gene3=[960] ; puntos = [7]
        Evaluacion con gene1=[320] gene2=[259] gene3=[991] ; puntos = [7]
     */


    private static final int FACTOR_MATERIAL_DEFAULT = 211;
    private static final int FACTOR_EXPANSION_DEFAULT = 188;
    private static final int FACTOR_ATAQUE_DEFAULT = 601;

    private final int material;
    private final int expansion;
    private final int ataque;

    public GameEvaluatorImp() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_EXPANSION_DEFAULT, FACTOR_ATAQUE_DEFAULT);
    }

    public GameEvaluatorImp(int material, int expansion, int ataque) {
        this.material = material;
        this.expansion = expansion;
        this.ataque = ataque;
    }

    @Override
    public int evaluate(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
                // If white is on mate then evaluation is INFINITE_NEGATIVE
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
                break;
            case DRAW:
                evaluation = 0;
                break;
            case CHECK:
                // If white is on check then evaluation starts at -1
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? -1 : +1;
            case NO_CHECK:
                evaluation += 10 * material * GameEvaluator.evaluateByMaterial(game);
                evaluation += evaluateByMoves(game);
        }
        return evaluation;
    }

    protected int evaluateByMoves(final Game game) {
        int evaluation = 0;
        Set<Square> origenes = new HashSet<>();
        Set<Square> territorioExpansion = new HashSet<>();
        Set<Square> territorioAtaque = new HashSet<>();
        int posiblesCapturasValor = 0;
        for (Move move : game.getPossibleMoves()) {
            origenes.add(move.getFrom().getKey());

            PiecePositioned to = move.getTo();

            territorioExpansion.add(to.getKey());

            if (to.getValue() != null) {
                territorioAtaque.add(to.getKey());
                posiblesCapturasValor += Math.abs(to.getValue().getPieceValue());
            }
        }
        ;

        evaluation = origenes.size() + expansion * territorioExpansion.size() + ataque * territorioAtaque.size() + posiblesCapturasValor;

        return (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) ? evaluation : -evaluation;
    }

}
