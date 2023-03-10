package net.chesstango.search.gamegraph;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;

import java.util.Iterator;
import java.util.List;

public class Node {
    @JsonProperty("fen")
    String fen;

    @JsonManagedReference
    List<NodeLink> links;

    Node parentNode;

    public void executeMove(Move move, GameMock gameMock) {
        for (NodeLink link :
                links) {
            /*
            if (GameMockNode.testMoveEquality(link.move, move)) {
                gameMock.currentMockNode = link.mockNode;
            }*/
        }
    }

    public void undoMove(GameMock gameMock) {
        gameMock.currentMockNode = parentNode;
    }

    public MoveContainerReader getPossibleMoves() {
        return new MoveContainerReader() {

            @Override
            public Iterator<Move> iterator() {
                //return links.stream().map(GameMockNodeLink::getMove).iterator();
                return null;
            }

            @Override
            public int size() {
                return links.size();
            }

            @Override
            public boolean isEmpty() {
                return links.isEmpty();
            }

            @Override
            public boolean contains(Move move) {
                //return links.stream().map(GameMockNodeLink::getMove).anyMatch(theMoveLink -> GameMockNode.testMoveEquality(theMoveLink, move));
                return false;
            }
        };
    }

    private static boolean testMoveEquality(Move move1, Move move2){
        return false;
    }

    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
        links.forEach(link -> link.accept(visitor));
    }
}
