package net.chesstango.search.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class NodeMoveSorterStageCatchAllTest {

    private NodeMoveSorterStage sorterStage;

    @BeforeEach
    public void setUp() {
        sorterStage = NodeMoveSorterStage.createCatchAllStage();
    }


    @Test
    public void testSortFullList() {
        Move moveQueen = createMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveKnight = createMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveBishop = createMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveRook = createMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE), PiecePositioned.getPosition(Square.e3));
        Move movePawn = createMove(PiecePositioned.of(Square.e2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveKing = createMove(PiecePositioned.of(Square.e2, Piece.KING_WHITE), PiecePositioned.getPosition(Square.e3));

        List<Move> moves = new ArrayList<>(List.of(moveKnight, moveKing, moveBishop, moveQueen, moveRook, movePawn));

        int result = sorterStage.sort(moves, 0);

        assertEquals(moves.size(), result);
        assertEquals(List.of(moveQueen, moveKnight, moveBishop, moveRook, movePawn, moveKing), moves);
    }

    @Test
    public void testSortSubRange() {
        Move head = createMove(PiecePositioned.of(Square.a1, Piece.ROOK_WHITE), PiecePositioned.getPosition(Square.a2));
        Move moveQueen = createMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveKnight = createMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveBishop = createMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveRook = createMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE), PiecePositioned.getPosition(Square.e3));
        Move moveRook1 = createMove(PiecePositioned.of(Square.h1, Piece.ROOK_WHITE), PiecePositioned.getPosition(Square.h2));

        List<Move> moves = new ArrayList<>(List.of(head, moveKnight, moveBishop, moveQueen, moveRook, moveRook1));

        int result = sorterStage.sort(moves, 1);

        assertEquals(6, result);
        assertEquals(head, moves.get(0));
        assertEquals(moveQueen, moves.get(1));
        assertEquals(moveKnight, moves.get(2));
        assertEquals(moveBishop, moves.get(3));
        assertEquals(moveRook, moves.get(4));
        assertEquals(moveRook1, moves.get(5));
    }

    @Test
    public void testSortGameMoves() {
        Game game = Game.from(FEN.from("rnb1kbnr/pppp1ppp/4p3/5q2/4P2N/8/PPPP1PPP/RNBQKB1R w KQkq - 4 4"));

        MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
        List<Move> moves1 = new ArrayList<>(possibleMoves.size());
        List<Move> moves2 = new ArrayList<>(possibleMoves.size());

        for (Move move : possibleMoves) {
            moves1.add(move);
            moves2.add(move);
        }

        int result = sorterStage.sort(moves1, 0);

        assertEquals(moves1.size(), result);

        moves2.sort(new DefaultMoveComparator().reversed());

        assertEquals(moves2, moves1);
    }

    @Test
    public void sortFriedLiverAttack() {
        FEN fen = FEN.from("r1bqkb1r/ppp2Npp/2n5/3np3/B1Q1P3/8/PPPP1PPP/RNB1K2R b KQkq - 0 1");
        String[] expectedOrderedMoves = new String[]{
                "e8f7", "d8h4", "d8g5", "d8f6", "d8d6", "d8e7", "d8d7", "d5e3", "d5c3",
                "d5f4", "d5b4", "d5f6", "d5b6", "d5e7", "f8a3", "f8b4", "f8c5", "f8d6",
                "f8e7", "c8h3", "c8g4", "c8f5", "c8e6", "c8d7", "h8g8", "a8b8", "h7h5",
                "h7h6", "g7g5", "g7g6", "b7b5", "b7b6", "a7a5", "a7a6", "e8e7", "e8d7"
        };
        assertMoveContainer(fen, expectedOrderedMoves, false);
        assertSortingSymmetry(fen);
    }

    @Test
    public void sortGameMoves01() {
        FEN fen = FEN.from("1R5r/1R2bpp1/2k1p2r/q3P3/b1P2P1p/PN1P2n1/5QPP/6K1 w - - 0 1");
        String[] expectedOrderedMoves = new String[]{
                "b3a5", "b8h8", "b7e7", "h2g3", "f2g3", "f2a7", "f2b6", "f2c5", "f2d4",
                "f2f3", "f2e3", "f2e2", "f2d2", "f2c2", "f2b2", "f2a2", "f2f1", "f2e1",
                "b3c5", "b3d4", "b3d2", "b3c1", "b3a1", "b8g8", "b8f8", "b8e8", "b8d8",
                "b8c8", "b8a8", "b7d7", "b7c7", "b7a7", "b7b6", "b7b5", "b7b4", "f4f5",
                "c4c5", "d3d4", "h2h3"
        };
        assertMoveContainer(fen, expectedOrderedMoves, false);
        assertSortingSymmetry(fen);
    }

    @Test
    public void sortKiwipeteTest() {
        FEN fen = FEN.from("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        String[] expectedOrderedMoves = new String[]{
                "e2a6", "f3f6", "d5e6", "g2h3", "e5f7", "e5d7", "e5g6", "f3h3", "f3h5",
                "f3f5", "f3g4", "f3f4", "f3g3", "f3e3", "f3d3", "e5c6", "e5g4", "e5c4",
                "e5d3", "c3b5", "c3a4", "c3d1", "c3b1", "e2b5", "e2c4", "e2d3", "e2f1",
                "e2d1", "d2h6", "d2g5", "d2f4", "d2e3", "d2c1", "h1g1", "h1f1", "a1d1",
                "a1c1", "a1b1", "d5d6", "g2g4", "g2g3", "b2b3", "a2a4", "a2a3", "e1g1",
                "e1f1", "e1d1", "e1c1"
        };
        assertMoveContainer(fen, expectedOrderedMoves, false);
        assertSortingSymmetry(fen);
    }

    @Test
    public void sortWithEnPassant() {
        FEN fen = FEN.from("r3k2r/p1pp1pb1/bn1qpnpB/3PN3/1p2P3/2N2Q1p/PPP1BPPP/R2K3R b kq - 3 2");
        String[] expectedOrderedMoves = new String[]{
                "h8h6","a6e2","g7h6","b4c3","d6e5","h3g2","e6d5","f6e4","f6d5",
                "b6d5","d6d5","d6c5","d6c6","d6e7","d6f8","f6g4","f6h5","f6h7",
                "f6g8","b6c4","b6a4","b6c8","a6d3","a6c4","a6b5","a6b7","a6c8",
                "g7f8","h8h7","h8g8","h8f8","a8d8","a8c8","a8b8","b4b3","g6g5",
                "c7c5","c7c6","e8e7","e8g8","e8f8","e8d8","e8c8"
        };
        assertMoveContainer(fen, expectedOrderedMoves, false);
        assertSortingSymmetry(fen);
    }

    @Test
    public void sortGameMoves02() {
        FEN fen = FEN.from("rnbqk2r/p4pb1/2p1p2p/1p1nP1p1/PPpP4/2N2NB1/5PPP/R2QKB1R b KQkq b3 0 11");
        String[] expectedOrderedMoves = new String[]{
                "d5c3","c4b3","b5a4","g7e5","d5b4","d8a5","d8f6","d8d6","d8b6",
                "d8e7","d8d7","d8c7","d5e3","d5f4","d5f6","d5b6","d5e7","d5c7",
                "b8a6","b8d7","g7f6","g7f8","c8a6","c8d7","c8b7","h8h7","h8g8",
                "h8f8","g5g4","h6h5","c6c5","f7f5","f7f6","a7a5","a7a6","e8e7",
                "e8d7","e8g8","e8f8"
        };
        assertMoveContainer(fen, expectedOrderedMoves, false);
        assertSortingSymmetry(fen);
    }

    @Test
    public void sortGameMoves03() {
        FEN fen = FEN.from("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q2/PPPBBPpP/R3K2R b KQkq - 0 1");
        String[] expectedOrderedMoves = new String[]{
                "g2h1q","g2g1q","g2h1r","g2g1r","g2h1b","g2g1b","g2h1n","g2g1n","a6e2",
                "b4c3","e6d5","h8h2","f6e4","f6d5","b6d5","e7c5","e7d6","e7f8",
                "e7d8","f6g4","f6h5","f6h7","f6g8","b6c4","b6a4","b6c8","a6d3",
                "a6c4","a6b5","a6b7","a6c8","g7h6","g7f8","h8h3","h8h4","h8h5",
                "h8h6","h8h7","h8g8","h8f8","a8d8","a8c8","a8b8","b4b3","g6g5",
                "d7d6","c7c5","c7c6","e8g8","e8f8","e8d8","e8c8"
        };
        assertMoveContainer(fen, expectedOrderedMoves, false);
        assertSortingSymmetry(fen);
    }

    void assertMoveContainer(FEN fen, String[] expectedOrderedMoves, boolean debug) {
        Game game = Game.from(fen);

        MoveContainerReader<Move> moves = game.getPossibleMoves();
        List<Move> possibleMovesList = new ArrayList<>(moves.size());
        for (Move move : moves) {
            possibleMovesList.add(move);
        }

        int result = sorterStage.sort(possibleMovesList, 0);

        assertEquals(possibleMovesList.size(), result);

        List<String> movesStr = possibleMovesList
                .stream()
                .map(Move::coordinateEncoding)
                .toList();

        if (debug) {
            AtomicInteger moveIndex = new AtomicInteger(0);
            movesStr.stream()
                    .map(m -> String.format("\"%s\",", m))
                    .forEach(m -> {
                        if (moveIndex.incrementAndGet() % 9 == 0) {
                            System.out.println(m);
                        } else {
                            System.out.print(m);
                        }
                    });
        }

        assertArrayEquals(expectedOrderedMoves, movesStr.toArray());
    }

    void assertSortingSymmetry(FEN fen) {
        Game blackGame = Game.from(fen);
        Game whiteGame = Game.from(fen).mirror();

        MoveContainerReader<Move> blackPossibleMoves = blackGame.getPossibleMoves();
        List<Move> blackPossibleMovesList = new ArrayList<>(blackPossibleMoves.size());
        for (Move move : blackPossibleMoves) {
            blackPossibleMovesList.add(move);
        }

        MoveContainerReader<Move> whitePossibleMoves = whiteGame.getPossibleMoves();
        List<Move> whitePossibleMovesList = new ArrayList<>(whitePossibleMoves.size());
        for (Move move : whitePossibleMoves) {
            whitePossibleMovesList.add(move);
        }

        int resultBlack = sorterStage.sort(blackPossibleMovesList, 0);
        assertEquals(blackPossibleMovesList.size(), resultBlack);

        int resultWhite = sorterStage.sort(whitePossibleMovesList, 0);
        assertEquals(whitePossibleMovesList.size(), resultWhite);


        assertEquals(blackPossibleMovesList.size(), whitePossibleMovesList.size());

        for (int i = 0; i < blackPossibleMovesList.size(); i++) {
            Move move1 = blackPossibleMovesList.get(i);
            Move move2 = whitePossibleMovesList.get(i);
            assertEquals(move1.getFrom().piece(), move2.getFrom().piece().getOpposite());
            assertEquals(move1.getFrom().square(), move2.getFrom().square().mirror());
            assertEquals(move1.getTo().square(), move2.getTo().square().mirror());
        }
    }

    private Move createMove(PiecePositioned from, PiecePositioned to) {
        return new Move() {
            @Override
            public PiecePositioned getFrom() {
                return from;
            }

            @Override
            public PiecePositioned getTo() {
                return to;
            }

            @Override
            public void executeMove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void undoMove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Cardinal getMoveDirection() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isQuiet() {
                throw new UnsupportedOperationException();
            }

            @Override
            public long getZobristHash() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
