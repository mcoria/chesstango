package net.chesstango.search.gamegraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.gardel.fen.FENBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mauricio Coria
 */
public class GameMockLoader {

    public static GameMock loadFromFile(String fileName) {
        InputStream inputStream = GameMockLoader.class.getClassLoader().getResourceAsStream(fileName);

        // reading the files with buffered reader
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        return new GameMockLoader().readGameMove(inputStreamReader);
    }

    public GameMock readGameMove(Reader reader) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Node node = objectMapper.readValue(reader, Node.class);

            node.accept(new VerifyUniqueMovesStrings());
            node.accept(new NodeFixParentLink());
            node.accept(new CreatePositions());

            GameMock gameMock = new GameMock();
            gameMock.currentMockNode = node;

            return gameMock;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class VerifyUniqueMovesStrings implements NodeVisitor {

        @Override
        public void visit(Node node) {
            Set<String> uniqueMoves = new HashSet<>();
            node.links.stream().map(link -> link.moveStr).forEach(moveStr -> {
                if (!uniqueMoves.add(moveStr)) {
                    throw new RuntimeException(String.format("Move %s duplicated", moveStr));
                }
            });
        }

        @Override
        public void visit(NodeLink nodeLink) {
        }
    }

    private static class NodeFixParentLink implements NodeVisitor {
        private Node parentNode;

        @Override
        public void visit(Node node) {
            node.parentNode = parentNode;
        }

        @Override
        public void visit(NodeLink nodeLink) {
            this.parentNode = nodeLink.parent;
        }
    }

    private static class CreatePositions implements NodeVisitor {
        private final Pattern movePattern = Pattern.compile("[a-h][1-8][a-h][1-8][rnbq]?");

        @Override
        public void visit(Node node) {
            if (node.position == null) {
                Game game = Game.fromFEN(node.fen);
                node.position = game.getPosition();
                node.gameState = game.getState();
            }

            FENBuilder fenBuilder = new FENBuilder();
            node.position.constructChessPositionRepresentation(fenBuilder);
            String fenFromPosition = fenBuilder.getPositionRepresentation().toString();

            if (node.fen == null) {
                node.fen = fenFromPosition;
            } else if (!node.fen.equals(fenFromPosition)) {
                throw new RuntimeException(String.format("invalid fen at this position: %s", node.fen));
            }
        }

        @Override
        public void visit(NodeLink nodeLink) {
            Matcher moveMatcher = movePattern.matcher(nodeLink.moveStr);
            if (moveMatcher.matches()) {

                Game game = Game.fromFEN(nodeLink.parent.fen);

                Move selectedMove = selectMove(game, nodeLink.moveStr);

                nodeLink.move = selectedMove;

                selectedMove.executeMove();

                nodeLink.mockNode.position = game.getPosition();
                nodeLink.mockNode.gameState = game.getState();

            } else {
                throw new RuntimeException("Invalid move " + nodeLink.moveStr);
            }
        }

        private Move selectMove(Game game, String moveStr) {
            for (Move move : game.getPossibleMoves()) {
                String encodedMoveStr = encode(move);
                if (encodedMoveStr.equals(moveStr)) {
                    return move;
                }
            }
            throw new RuntimeException(String.format("Move %s not found", moveStr));
        }

    }


    public static String encode(Move move) {
        String promotionStr = "";
        if (move instanceof MovePromotion movePromotion) {
            promotionStr = switch (movePromotion.getPromotion()) {
                case ROOK_WHITE, ROOK_BLACK -> "r";
                case KNIGHT_WHITE, KNIGHT_BLACK -> "n";
                case BISHOP_WHITE, BISHOP_BLACK -> "b";
                case QUEEN_WHITE, QUEEN_BLACK -> "q";
                default -> throw new RuntimeException("Invalid promotion " + move);
            };
        }
        return move.getFrom().getSquare().toString() + move.getTo().getSquare().toString() + promotionStr;
    }
}
