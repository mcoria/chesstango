package net.chesstango.evaluation.imp;

import net.chesstango.board.Piece;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 * Positions: Balsa_v500.pgn
 * Depth: 1
 * Time elapsed: 105736 ms
 *  ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |                 GameEvaluatorImp03|     275 |     305 |     1058 |     1056 |     1167 |     1139 |      858.5 |      874.5 |1733.0 /5000 |   34.7 |
 * |            GameEvaluatorByMaterial|      45 |      72 |      100 |       77 |      355 |      351 |      222.5 |      247.5 | 470.0 /1000 |   47.0 |
 * |    GameEvaluatorByMaterialAndMoves|       9 |      10 |      175 |      177 |      316 |      313 |      167.0 |      166.5 | 333.5 /1000 |   33.4 |
 * |                 GameEvaluatorImp01|     186 |     174 |       22 |       20 |      292 |      306 |      332.0 |      327.0 | 659.0 /1000 |   65.9 |
 * |                 GameEvaluatorImp02|     318 |     308 |        8 |        1 |      174 |      191 |      405.0 |      403.5 | 808.5 /1000 |   80.9 |
 * |                          Spike 1.4|     498 |     494 |        0 |        0 |        2 |        6 |      499.0 |      497.0 | 996.0 /1000 |   99.6 |
 *  ---------------------------------------------------------------------------------------------------------------------------------------------------
 *
 *  Positions: Balsa_v2724.pgn
 *  Depth: 1
 *  Time elapsed: 1066937 ms
 *  ______________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS  |   WIN %|
 * |                 GameEvaluatorImp03|    1816 |    1788 |     5856 |     5920 |     5938 |     5902 |     4785.0 |     4739.0 |9524.0 /27220 |   35.0 |
 * |            GameEvaluatorByMaterial|     391 |     416 |      564 |      561 |     1769 |     1747 |     1275.5 |     1289.5 |2565.0 /5448  |   47.1 |
 * |    GameEvaluatorByMaterialAndMoves|      29 |      36 |     1050 |     1106 |     1645 |     1582 |      851.5 |      827.0 |1678.5 /5448  |   30.8 |
 * |                 GameEvaluatorImp01|     985 |     980 |      150 |      118 |     1589 |     1626 |     1779.5 |     1793.0 |3572.5 /5448  |   65.6 |
 * |                 GameEvaluatorImp02|    1849 |    1753 |       23 |       30 |      852 |      941 |     2275.0 |     2223.5 |4498.5 /5448  |   82.6 |
 * |                          Spike 1.4|    2666 |    2671 |        1 |        1 |       47 |       42 |     2689.5 |     2692.0 |5381.5 /5428  |   99.1 |
 *  ------------------------------------------------------------------------------------------------------------------------------------------------------
 */
public class GameEvaluatorImp03 implements GameEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 104;
    private static final int FACTOR_MATERIAL_COLOR_DEFAULT = 896;

    private final int material;
    private final int material_color;

    public GameEvaluatorImp03() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_MATERIAL_COLOR_DEFAULT);
    }

    public GameEvaluatorImp03(Integer material, Integer material_color) {
        this.material = material;
        this.material_color = material_color;
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
                evaluation += material * evaluateByMaterial(game);
                evaluation += material_color * evaluateByColor(game);
        }
        return evaluation;
    }

    protected int evaluateByColor(final Game game) {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            evaluation += Color.WHITE.equals(piecePlacement.getPiece().getColor()) ? +1 : -1;
        }
        return evaluation;
    }

    @Override
    public int getPieceValue(Game game, Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> 1;
            case PAWN_BLACK -> -1;
            case KNIGHT_WHITE -> 3;
            case KNIGHT_BLACK -> -3;
            case BISHOP_WHITE -> 3;
            case BISHOP_BLACK -> -3;
            case ROOK_WHITE -> 5;
            case ROOK_BLACK -> -5;
            case QUEEN_WHITE -> 9;
            case QUEEN_BLACK -> -9;
            case KING_WHITE -> 10;
            case KING_BLACK -> -10;
        };
    }

}
