package net.chesstango.search.gamegraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FENDecoder;

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
        @Override
        public void visit(Node node) {
            ChessPosition position = FENDecoder.loadChessPosition(node.fen);
            node.position = position;
        }

        Pattern movePattern = Pattern.compile("(?<from>[a-h][1-8])(?<to>[a-h][1-8])");

        @Override
        public void visit(NodeLink nodeLink) {
            Matcher moveMatcher = movePattern.matcher(nodeLink.moveStr);
            if (moveMatcher.matches()) {
                String fromStr = moveMatcher.group("from");
                String toStr = moveMatcher.group("to");

                Square from = Square.valueOf(fromStr);
                Square to = Square.valueOf(toStr);

                ChessPosition position = nodeLink.parent.position;
                PiecePositioned fromPosition = position.getPosicion(from);
                PiecePositioned toPosition = position.getPosicion(to);

                MockMove mockMove = new MockMove();
                mockMove.from = fromPosition;
                mockMove.to = toPosition;

                nodeLink.move = mockMove;
            } else {
                throw new RuntimeException("Invalid move " + nodeLink.moveStr);
            }
        }
    }

}
