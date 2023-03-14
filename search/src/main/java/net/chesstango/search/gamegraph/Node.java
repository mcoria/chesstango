package net.chesstango.search.gamegraph;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.chesstango.board.GameStatus;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
class Node {
    @JsonProperty("fen")
    String fen;

    @JsonProperty("evaluation")
    int evaluation;

    @JsonManagedReference
    List<NodeLink> links;

    ChessPositionReader position;

    Node parentNode;

    void executeMove(Move move, GameMock gameMock) {
        NodeLink selectedLink = null;
        for (NodeLink link :
                links) {
            if (move == link.move) {
                selectedLink = link;
            }
        }

        if(selectedLink == null){
            throw new RuntimeException("Move doesn't exist in link + " + move);
        }
        gameMock.currentMockNode = selectedLink.mockNode;
    }

    void undoMove(GameMock gameMock) {
        gameMock.currentMockNode = parentNode;
    }

    MoveContainerReader getPossibleMoves() {
        return new MoveContainerReader() {

            @Override
            public Iterator<Move> iterator() {
                return links.stream().map(NodeLink::getMove).iterator();
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
                throw new UnsupportedOperationException("Method not implemented yet");
                //return links.stream().map(GameMockNodeLink::getMove).anyMatch(theMoveLink -> GameMockNode.testMoveEquality(theMoveLink, move));
                //return false;
            }
        };
    }

    void accept(NodeVisitor visitor) {
        visitor.visit(this);
        links.forEach(link -> link.accept(visitor));
    }

    ChessPositionReader getChessPosition() {
        return position;
    }

    GameStatus gameStatus;

    GameStatus getStatus() {
        return gameStatus;
    }
}
