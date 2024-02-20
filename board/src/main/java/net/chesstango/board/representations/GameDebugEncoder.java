package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.GameStateReader;
import net.chesstango.board.GameVisitor;
import net.chesstango.board.moves.Move;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GameDebugEncoder {

    public String encode(Game game) {
        StringBuilder sb = new StringBuilder();

        String initialFEN = game.getInitialFen();
        Game theGame = FENDecoder.loadGame(initialFEN);
        sb.append("Game game = getGame(\"" + initialFEN + "\")\n");

        game.accept(new GameVisitor() {

            @Override
            public void visit(ChessPositionReader chessPositionReader) {

            }

            @Override
            public void visit(GameStateReader gameState) {
                List<Move> moves = new LinkedList<>();
                GameStateReader currentGameState = gameState.getPreviousState();
                while (currentGameState != null) {
                    moves.add(currentGameState.getSelectedMove());
                    currentGameState = currentGameState.getPreviousState();
                }
                Collections.reverse(moves);

                moves.forEach(move -> {
                    sb.append(".executeMove(Square." + move.getFrom().getSquare().toString() + ", Square." + move.getTo().getSquare().toString() + ")");

                    theGame.executeMove(move);
                    FENEncoder fenEncoder = new FENEncoder();
                    ChessPositionReader theGamePositionReader = theGame.getChessPosition();
                    theGamePositionReader.constructChessPositionRepresentation(fenEncoder);

                    sb.append(" // " + fenEncoder.getChessRepresentation() + "\n");
                });
            }

            @Override
            public void visit(MoveGenerator moveGenerator) {

            }
        });


        return sb.toString();
    }
}
