package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.GameState;
import net.chesstango.board.GameVisitor;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;

/**
 * @author Mauricio Coria
 */
public class GameDebugEncoder {

    public String encode(Game game) {
        StringBuilder sb = new StringBuilder();
        game.accept(new GameVisitor() {
            private Game theGame = null;

            @Override
            public void visit(GameState gameState) {
                String initialFEN = gameState.getInitialFen();
                theGame = FENDecoder.loadGame(initialFEN);
                sb.append("Game game = getDefaultGame(\"" + initialFEN + "\"\n");
                sb.append("game\n");
            }

            @Override
            public void visit(GameState.GameStateData gameStateData) {
                Move move = gameStateData.getSelectedMove();

                if (move != null) {
                    sb.append(".executeMove(Square." + move.getFrom().getSquare().toString() + ", Square." + move.getTo().getSquare().toString() + ")");

                    theGame.executeMove(move);
                    FENEncoder fenEncoder = new FENEncoder();
                    ChessPositionReader theGamePositionReader = theGame.getChessPosition();
                    theGamePositionReader.constructBoardRepresentation(fenEncoder);

                    sb.append(" // " + fenEncoder.getChessRepresentation() + "\n");
                }
            }
        });


        return sb.toString();
    }
}
