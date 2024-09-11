package net.chesstango.evaluation.evaluators;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.position.ChessPositionReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Mauricio Coria
 */
public class EvaluatorByMaterial extends AbstractEvaluator {

    /**
     * Values
     */
    private final int PAWN_VALUE;
    private final int KNIGHT_VALUE;
    private final int BISHOP_VALUE;
    private final int ROOK_VALUE;
    private final int QUEEN_VALUE;

    public EvaluatorByMaterial() {
        this(readDefaultValues());
    }

    public EvaluatorByMaterial(Tables tables) {
        this(tables.pawn, tables.knight, tables.bishop, tables.rook, tables.queen);
    }

    public EvaluatorByMaterial(int pawn,
                               int knight,
                               int bishop,
                               int rook,
                               int queen) {

        this.PAWN_VALUE = pawn;
        this.KNIGHT_VALUE = knight;
        this.BISHOP_VALUE = bishop;
        this.ROOK_VALUE = rook;
        this.QUEEN_VALUE = queen;
    }

    @Override
    public int evaluateNonFinalStatus() {
        return evaluateByMaterial();
    }


    protected int evaluateByMaterial() {
        int evaluation = 0;

        ChessPositionReader positionReader = game.getChessPosition();

        long whitePositions = positionReader.getPositions(Color.WHITE);

        long blackPositions = positionReader.getPositions(Color.BLACK);

        evaluation += Long.bitCount(whitePositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getBishopPositions()) * getPieceValue(Piece.BISHOP_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_WHITE);

        evaluation += Long.bitCount(blackPositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getBishopPositions()) * getPieceValue(Piece.BISHOP_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_BLACK);

        return evaluation;
    }

    protected int getPieceValue(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> PAWN_VALUE;
            case PAWN_BLACK -> -PAWN_VALUE;
            case KNIGHT_WHITE -> KNIGHT_VALUE;
            case KNIGHT_BLACK -> -KNIGHT_VALUE;
            case BISHOP_WHITE -> BISHOP_VALUE;
            case BISHOP_BLACK -> -BISHOP_VALUE;
            case ROOK_WHITE -> ROOK_VALUE;
            case ROOK_BLACK -> -ROOK_VALUE;
            case QUEEN_WHITE -> QUEEN_VALUE;
            case QUEEN_BLACK -> -QUEEN_VALUE;
            case KING_WHITE, KING_BLACK -> 0;
        };
    }

    public record Tables(String id,
                         int pawn,
                         int knight,
                         int bishop,
                         int rook,
                         int queen) {
    }

    private static Tables readDefaultValues() {
        try (InputStream inputStream = Tables.class.getClassLoader()
                .getResourceAsStream("evaluatorByMaterial.json");) {
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
