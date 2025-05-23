package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.PositionReader;
import net.chesstango.gardel.fen.FENBuilder;

/**
 * @author Mauricio Coria
 */
public class GameDebugEncoder {

    public String encode(Game game) {
        StringBuilder sb = new StringBuilder();

        String initialFEN = game.getInitialFEN().toString();
        Game theGame = Game.fromFEN(initialFEN);
        sb.append("Game game = getGame(\"")
                .append(initialFEN)
                .append("\")\n");


        game.getHistory().iteratorReverse().forEachRemaining(careTakerRecord -> {
            Move move = careTakerRecord.playedMove();
            if (move instanceof MovePromotion movePromotion) {
                sb.append(".executeMove(Square.").append(movePromotion.getFrom().getSquare().toString())
                        .append(", Square.").append(movePromotion.getTo().getSquare().toString())
                        .append(", Piece.").append(movePromotion.getPromotion().toString())
                        .append(")");

                // Execute move
                theGame.executeMove(move.getFrom().getSquare(), move.getTo().getSquare(), movePromotion.getPromotion());

            } else {
                sb.append(".executeMove(Square.")
                        .append(move.getFrom().getSquare().toString())
                        .append(", Square.")
                        .append(move.getTo().getSquare().toString()).append(")");

                // Execute move
                theGame.executeMove(move.getFrom().getSquare(), move.getTo().getSquare());
            }

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
