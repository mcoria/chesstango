package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.PositionReader;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENBuilder;

/**
 * @author Mauricio Coria
 */
public class GameDebugEncoder {

    public String encode(Game game) {
        StringBuilder sb = new StringBuilder();

        String initialFEN = game.getInitialFEN().toString();
        Game theGame = Game.from(FEN.of(initialFEN));
        sb.append("Game game = getGame(\"")
                .append(initialFEN)
                .append("\")\n");


        game.getHistory().iteratorReverse().forEachRemaining(careTakerRecord -> {
            Move move = careTakerRecord.playedMove();
            if (move instanceof MovePromotion movePromotion) {
                sb.append(".executeMove(Square.").append(movePromotion.getFrom().square().toString())
                        .append(", Square.").append(movePromotion.getTo().square().toString())
                        .append(", Piece.").append(movePromotion.getPromotion().toString())
                        .append(")");

                // Execute move
                theGame.executeMove(move.getFrom().square(), move.getTo().square(), movePromotion.getPromotion());

            } else {
                sb.append(".executeMove(Square.")
                        .append(move.getFrom().square().toString())
                        .append(", Square.")
                        .append(move.getTo().square().toString()).append(")");

                // Execute move
                theGame.executeMove(move.getFrom().square(), move.getTo().square());
            }

            FENBuilder fenBuilder = new FENBuilder();
            PositionReader theGamePositionReader = theGame.getPosition();
            theGamePositionReader.export(fenBuilder);

            sb.append(" // ")
                    .append(fenBuilder.getPositionRepresentation())
                    .append("\n");
        });


        return sb.toString();
    }
}
