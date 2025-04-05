package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.iterators.state.FirstToLast;
import net.chesstango.board.iterators.state.StateIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;

/**
 * @author Mauricio Coria
 */
public class GameDebugEncoder {

    public String encode(Game game) {
        StringBuilder sb = new StringBuilder();

        String initialFEN = game.getInitialFEN().toString();
        Game theGame = FENDecoder.loadGame(initialFEN);
        sb.append("Game game = getGame(\"")
                .append(initialFEN)
                .append("\")\n");

        StateIterator stateIterator = new FirstToLast(game.getState());
        while (stateIterator.hasNext()) {
            GameStateReader gameState = stateIterator.next();

            Move move = gameState.getSelectedMove();


            sb.append(".executeMove(Square.")
                    .append(move.getFrom().getSquare().toString())
                    .append(", Square.")
                    .append(move.getTo().getSquare().toString()).append(")");

            // Execute move
            move.executeMove();

            FENEncoder fenEncoder = new FENEncoder();
            ChessPositionReader theGamePositionReader = theGame.getChessPosition();
            theGamePositionReader.constructChessPositionRepresentation(fenEncoder);

            sb.append(" // ")
                    .append(fenEncoder.getChessRepresentation())
                    .append("\n");
        }
        ;


        return sb.toString();
    }
}
