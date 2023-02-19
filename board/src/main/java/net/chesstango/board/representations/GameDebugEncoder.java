package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.GameState;
import net.chesstango.board.GameStateVisitor;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;

/**
 * @author Mauricio Coria
 */
public class GameDebugEncoder {

    public String encode(String initialFen, Game game) {
        StringBuilder sb = new StringBuilder();

        Game theGame = FENDecoder.loadGame(initialFen);

        sb.append("Game game = getDefaultGame();\n");
        sb.append("game\n");

        game.accept(new GameStateVisitor() {
            @Override
            public void visit(GameState gameState) {

            }

            @Override
            public void visit(GameState.GameStateData gameStateData) {
                Move move = gameStateData.selectedMove;

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
