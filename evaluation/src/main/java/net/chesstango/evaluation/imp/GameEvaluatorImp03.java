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
 *
 *  Positions: Balsa_v2724.pgn
 *  Depth: 1
 *  Time elapsed: 1066937 ms
 *  Time elapsed: 355648 ms
 *  _______________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|  TOTAL POINTS  |   WIN %|
 * |                 GameEvaluatorImp03|    1910 |    1846 |     5827 |     5982 |     5873 |     5782 |     4846.5 |     4737.0 | 9583.5 / 27220 |   35.2 |
 * |            GameEvaluatorByMaterial|     422 |     409 |      560 |      596 |     1742 |     1719 |     1293.0 |     1268.5 | 2561.5 / 5448  |   47.0 |
 * |    GameEvaluatorByMaterialAndMoves|      32 |      45 |     1106 |     1165 |     1586 |     1514 |      825.0 |      802.0 | 1627.0 / 5448  |   29.9 |
 * |                 GameEvaluatorImp01|     973 |     968 |      161 |      120 |     1590 |     1636 |     1768.0 |     1786.0 | 3554.0 / 5448  |   65.2 |
 * |                 GameEvaluatorImp02|    1896 |    1747 |       18 |       29 |      810 |      948 |     2301.0 |     2221.0 | 4522.0 / 5448  |   83.0 |
 * |                          Spike 1.4|    2659 |    2658 |        1 |        0 |       54 |       56 |     2686.0 |     2686.0 | 5372.0 / 5428  |   99.0 |
 *  -------------------------------------------------------------------------------------------------------------------------------------------------------
 */
public class GameEvaluatorImp03 implements GameEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 500;
    private static final int FACTOR_MATERIAL_COLOR_DEFAULT = 500;

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
