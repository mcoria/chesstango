package net.chesstango.evaluation.evaluators;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.GameStateReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 * Positions: Balsa_v500.pgn
 * Depth: 1
 * Time elapsed: 89616 ms
 *  _________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS |WIN % |
 * |                 GameEvaluatorImp02|     799 |     772 |      418 |      410 |      783 |      818 |     1190.5 |     1181.0 |2371.5 /4000 | 59.3 |
 * |            GameEvaluatorByMaterial|       0 |       3 |      313 |      318 |      187 |      179 |       93.5 |       92.5 | 186.0 /1000 | 18.6 |
 * |    GameEvaluatorByMaterialAndMoves|       5 |       1 |      304 |      323 |      191 |      176 |      100.5 |       89.0 | 189.5 /1000 | 19.0 |
 * |                 GameEvaluatorImp01|      45 |      33 |      151 |      155 |      304 |      312 |      197.0 |      189.0 | 386.0 /1000 | 38.6 |
 * |                          Spike 1.4|     360 |     381 |        4 |        3 |      136 |      116 |      428.0 |      439.0 | 867.0 /1000 | 86.7 |
 *  -------------------------------------------------------------------------------------------------------------------------------------------------
 *
 * Positions: Balsa_v500.pgn  (Match)
 * Depth: 1
 * Time taken: 140897 ms
 *  _________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS |WIN % |
 * |                              Tango|       3 |       4 |      360 |        0 |      116 |      136 |       61.0 |       72.0 | 133.0 /1000 | 13.3 |
 * |                          Spike 1.4|     360 |     381 |        3 |        0 |      136 |      116 |      428.0 |      439.0 | 867.0 /1000 | 86.7 |
 *  -------------------------------------------------------------------------------------------------------------------------------------------------
 *
 *  Positions: Balsa_v2724.pgn  (Match)
 *  Depth: 1
 *  Time elapsed: 939716 ms
 *  ________________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|   TOTAL POINTS  |   WIN %|
 * |                 GameEvaluatorImp02|    6561 |    6406 |     1948 |     2039 |     5111 |     5175 |     9116.5 |     8993.5 | 18110.0 / 27240 |   66.5 |
 * |            GameEvaluatorByMaterial|      10 |       6 |     1814 |     1914 |      900 |      804 |      460.0 |      408.0 |   868.0 / 5448  |   15.9 |
 * |    GameEvaluatorByMaterialAndMoves|       7 |       3 |     1896 |     1784 |      821 |      937 |      417.5 |      471.5 |   889.0 / 5448  |   16.3 |
 * |                 GameEvaluatorImp01|     139 |     156 |      922 |      976 |     1663 |     1592 |      970.5 |      952.0 |  1922.5 / 5448  |   35.3 |
 * |                 GameEvaluatorImp03|      30 |      23 |     1753 |     1849 |      941 |      852 |      500.5 |      449.0 |   949.5 / 5448  |   17.4 |
 * |                          Spike 1.4|    1853 |    1760 |       21 |       38 |      850 |      926 |     2278.0 |     2223.0 |  4501.0 / 5448  |   82.6 |
 *  --------------------------------------------------------------------------------------------------------------------------------------------------------
 */
public class EvaluatorImp02 extends AbstractEvaluator {
    private static final int FACTOR_MATERIAL_DEFAULT = 422;
    private static final int FACTOR_EXPANSION_DEFAULT = 3;
    private static final int FACTOR_ATAQUE_DEFAULT = 575;

    private final int material;
    private final int expansion;
    private final int ataque;

    private ChessPositionReader positionReader;
    private MoveGenerator pseudoMovesGenerator;

    public EvaluatorImp02() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_EXPANSION_DEFAULT, FACTOR_ATAQUE_DEFAULT);
    }

    public EvaluatorImp02(int material, int expansion, int ataque) {
        this.material = material;
        this.expansion = expansion;
        this.ataque = ataque;
    }

    @Override
    public int evaluateNonFinalStatus() {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
            case STALEMATE:
                evaluation = evaluateFinalStatus();
                break;
            case CHECK:
                // If white is on check then evaluation starts at -1
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? -1 : +1;
            case NO_CHECK:
                evaluation += material * 10 * evaluateByMaterial();
                evaluation += evaluateByMoveAndByAttack();
        }
        return evaluation;
    }

    protected int evaluateByMoveAndByAttack() {
        int evaluationByAttack = 0;

        int evaluationByMoveToEmptySquare = 0;

        Iterator<PiecePositioned> iteratorAllPieces = positionReader.iteratorAllPieces();

        while (iteratorAllPieces.hasNext()) {
            PiecePositioned piecePositioned = iteratorAllPieces.next();

            MoveGeneratorResult generationResult = pseudoMovesGenerator.generatePseudoMoves(piecePositioned);

            MoveList<PseudoMove> pseudoMoves = generationResult.getPseudoMoves();

            for (Move move : pseudoMoves) {
                PiecePositioned fromPosition = move.getFrom();
                PiecePositioned toPosition = move.getTo();
                if (toPosition.getPiece() == null) {
                    evaluationByMoveToEmptySquare += getPieceValue(fromPosition.getPiece());
                } else {
                    evaluationByAttack -= getPieceValue(toPosition.getPiece());
                }
            }
        }

        // From white point of view
        return expansion * evaluationByMoveToEmptySquare + ataque * evaluationByAttack;
    }


    public int getPieceValue(Piece piece) {
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

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        game.accept(new GameVisitor() {
            @Override
            public void visit(ChessPositionReader chessPositionReader) {
                positionReader = chessPositionReader;
            }

            @Override
            public void visit(GameStateReader gameState) {
            }

            @Override
            public void visit(MoveGenerator moveGenerator) {
                pseudoMovesGenerator = moveGenerator;
            }

        });
    }


    protected int evaluateByMaterial() {
        int evaluation = 0;

        ChessPositionReader positionReader = game.getChessPosition();

        long whitePositions = positionReader.getPositions(Color.WHITE);

        long blackPositions = positionReader.getPositions(Color.BLACK);

        evaluation += Long.bitCount(whitePositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getBishopPositions()) * getPieceValue(Piece.BISHOP_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_WHITE);

        evaluation += Long.bitCount(blackPositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getBishopPositions()) * getPieceValue(Piece.BISHOP_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_BLACK);

        return evaluation;
    }

}
