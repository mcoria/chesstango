package net.chesstango.board.representations.epd;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.move.MoveDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class EPD {
    private String text;

    private String piecePlacement;
    private String activeColor;
    private String castingsAllowed;
    private String enPassantSquare;

    private String id;
    private String c0;
    private String c1;
    private String c2;
    private String c3;
    private String c4;
    private String c5;
    private String c6;
    private String c7;

    private String bestMovesStr;

    private String avoidMovesStr;

    private String suppliedMoveStr;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        EPD epd = (EPD) object;
        return Objects.equals(getText(), epd.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText());
    }


    @Override
    public String toString() {
        return text != null ? text : new EPDEncoder().encode(this);
    }

    public String getFenWithoutClocks() {
        return piecePlacement +
                " " +
                activeColor +
                " " +
                castingsAllowed +
                " " +
                enPassantSquare;
    }

    public boolean isMoveSuccess(Move move) {
        if (bestMovesStr != null && !bestMovesStr.isEmpty()) {
            return isMoveSuccess(move, bestMovesStr);
        } else if (avoidMovesStr != null && !avoidMovesStr.isEmpty()) {
            return isMoveSuccess(move, avoidMovesStr);
        } else if (suppliedMoveStr != null && !suppliedMoveStr.isEmpty()) {
            return isMoveSuccess(move, suppliedMoveStr);
        } else {
            throw new RuntimeException("Undefined expected EPD result");
        }
    }

    private boolean isMoveSuccess(Move move, String movesStr) {
        List<Move> movesFromString = movesStringToMoves(movesStr);
        for (Move moveFromString : movesFromString) {
            if (move.equals(moveFromString)) {
                return true;
            }
        }
        return false;
    }

    List<Move> movesStringToMoves(String movesString) {
        Game game = Game.from(this);
        String[] bestMoves = movesString.split(" ");
        List<Move> moveList = new ArrayList<>(bestMoves.length);
        MoveDecoder moveDecoder = new MoveDecoder();
        for (String bestMove : bestMoves) {
            Move move = moveDecoder.decode(bestMove, game.getPossibleMoves());
            if (move != null) {
                moveList.add(move);
            } else {
                throw new RuntimeException(String.format("Unable to find move %s", bestMove));
            }
        }
        return moveList;
    }
}
