package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EPDEntry {
    public String id;
    public String text;

    public String fen;
    public String bestMovesString;
    public List<Move> bestMoves;

    public Game game;
}
