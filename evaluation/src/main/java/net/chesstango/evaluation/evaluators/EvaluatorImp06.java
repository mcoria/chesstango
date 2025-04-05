package net.chesstango.evaluation.evaluators;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chesstango.board.*;
import net.chesstango.board.position.ChessPositionReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * @author Mauricio Coria
 * <p>
 * ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |                     EvaluatorImp06|      50 |      42 |      433 |      439 |       17 |       18 |       58.5 |       51.0 | 109.5 /999 |   11.0 |
 * |                          Spike 1.4|     439 |     433 |       42 |       50 |       18 |       17 |      448.0 |      441.5 | 889.5 /999 |   89.0 |
 * ---------------------------------------------------------------------------------------------------------------------------------------------------
 */
public class EvaluatorImp06 extends AbstractEvaluator {

    private static final long BISHOP_PARES = 0xAA55AA55AA55AA55L;
    private static final long BISHOP_IMPARES = 0x55AA55AA55AA55AAL;

    private final int wgMaterial;
    private final int wgMidGame;
    private final int wgEndGame;

    private final int[] mgPawnTbl;
    private final int[] mgKnightTbl;
    private final int[] mgBishopTbl;
    private final int[] mgRookTbl;
    private final int[] mgQueenTbl;
    private final int[] mgKingTbl;

    private final int[] egPawnTbl;
    private final int[] egKnightTbl;
    private final int[] egBishopTbl;
    private final int[] egRookTbl;
    private final int[] egQueenTbl;
    private final int[] egKingTbl;

    private ChessPositionReader positionReader;

    public EvaluatorImp06() {
        this(readDefaultValues());
    }

    public EvaluatorImp06(String json) {
        this(readValues(json));
    }

    public EvaluatorImp06(Tables tables) {
        this(tables.weighs,
                tables.mgPawnTbl,
                tables.mgKnightTbl,
                tables.mgBishopTbl,
                tables.mgRookTbl,
                tables.mgQueenTbl,
                tables.mgKingTbl,

                tables.egPawnTbl,
                tables.egKnightTbl,
                tables.egBishopTbl,
                tables.egRookTbl,
                tables.egQueenTbl,
                tables.egKingTbl);
    }

    public EvaluatorImp06(int[] weighs,
                          int[] mgPawnTbl, int[] mgKnightTbl, int[] mgBishopTbl, int[] mgRookTbl, int[] mgQueenTbl, int[] mgKingTbl,
                          int[] egPawnTbl, int[] egKnightTbl, int[] egBishopTbl, int[] egRookTbl, int[] egQueenTbl, int[] egKingTbl) {

        this.wgMaterial = weighs[0];
        this.wgMidGame = weighs[1];
        this.wgEndGame = weighs[2];

        this.mgPawnTbl = mgPawnTbl;
        this.mgKnightTbl = mgKnightTbl;
        this.mgBishopTbl = mgBishopTbl;
        this.mgRookTbl = mgRookTbl;
        this.mgQueenTbl = mgQueenTbl;
        this.mgKingTbl = mgKingTbl;

        this.egPawnTbl = egPawnTbl;
        this.egKnightTbl = egKnightTbl;
        this.egBishopTbl = egBishopTbl;
        this.egRookTbl = egRookTbl;
        this.egQueenTbl = egQueenTbl;
        this.egKingTbl = egKingTbl;
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        this.positionReader = game.getChessPosition();
    }


    @Override
    public int evaluateNonFinalStatus() {
        return wgMaterial * evaluateByMaterial() + evaluateByPST();
    }

    protected int evaluateByMaterial() {
        int evaluation = 0;

        ChessPositionReader positionReader = game.getChessPosition();

        long whitePositions = positionReader.getPositions(Color.WHITE);

        long blackPositions = positionReader.getPositions(Color.BLACK);


        /**
         * Whites
         */
        evaluation += Long.bitCount(whitePositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_WHITE);

        long whiteBishopPositions = whitePositions & positionReader.getBishopPositions();
        if (whiteBishopPositions != 0) {
            if ((BISHOP_PARES & whiteBishopPositions) != 0) {
                evaluation += getPieceValue(Piece.BISHOP_WHITE) + Long.bitCount(blackPositions & BISHOP_PARES);
            }

            if ((BISHOP_IMPARES & whiteBishopPositions) != 0) {
                evaluation += getPieceValue(Piece.BISHOP_WHITE) + Long.bitCount(blackPositions & BISHOP_IMPARES);
            }
        }


        /**
         * Blacks
         */
        evaluation += Long.bitCount(blackPositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_BLACK);

        long blackBishopPositions = blackPositions & positionReader.getBishopPositions();
        if (blackBishopPositions != 0) {
            if ((BISHOP_PARES & blackBishopPositions) != 0) {
                evaluation += getPieceValue(Piece.BISHOP_BLACK) - Long.bitCount(whitePositions & BISHOP_PARES);
            }

            if ((BISHOP_IMPARES & blackBishopPositions) != 0) {
                evaluation += getPieceValue(Piece.BISHOP_BLACK) - Long.bitCount(whitePositions & BISHOP_IMPARES);
            }
        }

        return evaluation;
    }

    protected int evaluateByPST() {
        int evaluation = 0;

        final int numberOfPieces = Long.bitCount(positionReader.getAllPositions());

        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getPiece();
            Square square = piecePlacement.getSquare();

            int[] mgPositionValues = getMgPositionValues(piece);
            int mgValue = Color.WHITE.equals(piece.getColor()) ? mgPositionValues[square.toIdx()] : -mgPositionValues[square.getMirrorSquare().toIdx()];

            int[] egPositionValues = getEgPositionValues(piece);
            int egValue = Color.WHITE.equals(piece.getColor()) ? egPositionValues[square.toIdx()] : -egPositionValues[square.getMirrorSquare().toIdx()];

            evaluation += wgMidGame * (numberOfPieces - 2) * mgValue + wgEndGame * (32 - numberOfPieces) * egValue;
        }
        return evaluation;
    }

    protected int[] getMgPositionValues(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE, PAWN_BLACK -> mgPawnTbl;
            case KNIGHT_WHITE, KNIGHT_BLACK -> mgKnightTbl;
            case BISHOP_WHITE, BISHOP_BLACK -> mgBishopTbl;
            case ROOK_WHITE, ROOK_BLACK -> mgRookTbl;
            case QUEEN_WHITE, QUEEN_BLACK -> mgQueenTbl;
            case KING_WHITE, KING_BLACK -> mgKingTbl;
        };
    }

    protected int[] getEgPositionValues(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE, PAWN_BLACK -> egPawnTbl;
            case KNIGHT_WHITE, KNIGHT_BLACK -> egKnightTbl;
            case BISHOP_WHITE, BISHOP_BLACK -> egBishopTbl;
            case ROOK_WHITE, ROOK_BLACK -> egRookTbl;
            case QUEEN_WHITE, QUEEN_BLACK -> egQueenTbl;
            case KING_WHITE, KING_BLACK -> egKingTbl;
        };
    }

    protected int getPieceValue(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> PIECES_DEFAULT[0];
            case PAWN_BLACK -> -PIECES_DEFAULT[0];
            case KNIGHT_WHITE -> PIECES_DEFAULT[1];
            case KNIGHT_BLACK -> -PIECES_DEFAULT[1];
            case BISHOP_WHITE -> PIECES_DEFAULT[2];
            case BISHOP_BLACK -> -PIECES_DEFAULT[2];
            case ROOK_WHITE -> PIECES_DEFAULT[3];
            case ROOK_BLACK -> -PIECES_DEFAULT[3];
            case QUEEN_WHITE -> PIECES_DEFAULT[4];
            case QUEEN_BLACK -> -PIECES_DEFAULT[4];
            case KING_WHITE, KING_BLACK -> 0;
        };
    }


    /**
     * Values
     */


    private static final int[] PIECES_DEFAULT = new int[]{
            100, // PAWN
            320, // KNIGHT
            330, // BISHOP
            500, // ROOK
            900, // QUEEN
    };

    public record Tables(String id,
                         int[] weighs,
                         int[] mgPawnTbl,
                         int[] mgKnightTbl,
                         int[] mgBishopTbl,
                         int[] mgRookTbl,
                         int[] mgQueenTbl,
                         int[] mgKingTbl,

                         int[] egPawnTbl,
                         int[] egKnightTbl,
                         int[] egBishopTbl,
                         int[] egRookTbl,
                         int[] egQueenTbl,
                         int[] egKingTbl) {
    }

    public static Tables readDefaultValues() {
        String fileName = "EvaluatorImp06.json";
        try (InputStream inputStream = EvaluatorByMaterial.EvaluatorByMaterialTable.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException(String.format("File doesn't exist: %s", fileName));
            }
            return readValues(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Tables readValues(InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStreamReader, Tables.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Tables readValues(String dump) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(dump, Tables.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
