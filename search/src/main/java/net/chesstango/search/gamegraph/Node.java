package net.chesstango.search.gamegraph;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.Status;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.PositionReader;

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

    PositionReader position;

    Node parentNode;

    void executeMove(Move move, GameMock gameMock) {
        NodeLink selectedLink = null;
        for (NodeLink link :
                links) {
            if (move == link.move) {
                selectedLink = link;
            }
        }

        if (selectedLink == null) {
            throw new RuntimeException("Move doesn't exist in link + " + move);
        }
        gameMock.currentMockNode = selectedLink.mockNode;
    }

    void undoMove(GameMock gameMock) {
        gameMock.currentMockNode = parentNode;
    }

    MoveContainerReader<Move> getPossibleMoves() {
        return new MoveContainerReader<>() {

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
            public boolean contains(Object move) {
                throw new UnsupportedOperationException("Method not implemented yet");
                //return links.stream().map(GameMockNodeLink::getMove).anyMatch(theMoveLink -> GameMockNode.testMoveEquality(theMoveLink, move));
                //return false;
            }

            @Override
            public Move getMove(Square from, Square to) {
                for (Move move : this) {
                    if (from.equals(move.getFrom().square()) && to.equals(move.getTo().square())) {
                        if (move instanceof MovePromotion) {
                            return null;
                        }
                        return move;
                    }
                }
                return null;
            }

            @Override
            public Move getMove(Square from, Square to, Piece promotionPiece) {
                for (Move move : this) {
                    if (from.equals(move.getFrom().square()) && to.equals(move.getTo().square()) && (move instanceof MovePromotion movePromotion)) {
                        if (movePromotion.getPromotion().equals(promotionPiece)) {
                            return move;
                        }
                    }
                }
                return null;
            }

            @Override
            public boolean hasQuietMoves() {
                return false;
            }

        };
    }

    void accept(NodeVisitor visitor) {
        visitor.visit(this);
        links.forEach(link -> link.accept(visitor));
    }

    PositionReader getChessPosition() {
        return position;
    }

    GameStateReader gameState;

    Status getStatus() {
        return gameState.getStatus();
    }

    GameStateReader getState() {
        return gameState;
    }
}
