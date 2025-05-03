package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.PositionReader;
import net.chesstango.board.representations.fen.FENParser;
import net.chesstango.board.representations.fen.FENBuilder;

/**
 * @author Mauricio Coria
 */
public class GameDebugEncoder {

    public String encode(Game game) {
        StringBuilder sb = new StringBuilder();

        String initialFEN = game.getInitialFEN().toString();
        Game theGame = FENParser.loadGame(initialFEN);
        sb.append("Game game = getGame(\"")
                .append(initialFEN)
                .append("\")\n");


        game.getHistory().iteratorReverse().forEachRemaining(careTakerRecord -> {
            Move move = careTakerRecord.playedMove();
            sb.append(".executeMove(Square.")
                    .append(move.getFrom().getSquare().toString())
                    .append(", Square.")
                    .append(move.getTo().getSquare().toString()).append(")");

            // Execute move
            move.executeMove();

            FENBuilder fenBuilder = new FENBuilder();
            PositionReader theGamePositionReader = theGame.getPosition();
            theGamePositionReader.constructChessPositionRepresentation(fenBuilder);

            sb.append(" // ")
                    .append(fenBuilder.getPositionRepresentation())
                    .append("\n");
        });


        return sb.toString();
    }
}
