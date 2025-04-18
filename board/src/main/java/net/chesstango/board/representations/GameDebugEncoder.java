package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.PositionReader;
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


        game.getHistory().iteratorReverse().forEachRemaining(careTakerRecord -> {
            Move move = careTakerRecord.playedMove();
            sb.append(".executeMove(Square.")
                    .append(move.getFrom().getSquare().toString())
                    .append(", Square.")
                    .append(move.getTo().getSquare().toString()).append(")");

            // Execute move
            move.executeMove();

            FENEncoder fenEncoder = new FENEncoder();
            PositionReader theGamePositionReader = theGame.getPosition();
            theGamePositionReader.constructChessPositionRepresentation(fenEncoder);

            sb.append(" // ")
                    .append(fenEncoder.getChessRepresentation())
                    .append("\n");
        });


        return sb.toString();
    }
}
