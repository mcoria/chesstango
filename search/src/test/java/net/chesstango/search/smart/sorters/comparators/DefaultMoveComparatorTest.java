package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class DefaultMoveComparatorTest {

    private DefaultMoveComparator defaultMoveComparator;

    @BeforeEach
    public void setUp() {
        defaultMoveComparator = new DefaultMoveComparator();
    }

    @Test
    public void testSimpleMove_CompareByPiece() {
        Move moveQueen = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveKnight = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveBishop = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveRook = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move movePawn = createSimpleOneSquarePawnMove(PiecePositioned.of(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveKing = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3));

        assertTrue(defaultMoveComparator.compare(moveQueen, moveKnight) > 0);
        assertTrue(defaultMoveComparator.compare(moveQueen, moveBishop) > 0);
        assertTrue(defaultMoveComparator.compare(moveQueen, moveRook) > 0);
        assertTrue(defaultMoveComparator.compare(moveQueen, movePawn) > 0);
        assertTrue(defaultMoveComparator.compare(moveQueen, moveKing) > 0);

        assertTrue(defaultMoveComparator.compare(moveKnight, moveQueen) < 0);
        assertTrue(defaultMoveComparator.compare(moveKnight, moveBishop) > 0);
        assertTrue(defaultMoveComparator.compare(moveKnight, moveRook) > 0);
        assertTrue(defaultMoveComparator.compare(moveKnight, movePawn) > 0);
        assertTrue(defaultMoveComparator.compare(moveKnight, moveKing) > 0);

        assertTrue(defaultMoveComparator.compare(moveBishop, moveQueen) < 0);
        assertTrue(defaultMoveComparator.compare(moveBishop, moveKnight) < 0);
        assertTrue(defaultMoveComparator.compare(moveBishop, moveRook) > 0);
        assertTrue(defaultMoveComparator.compare(moveBishop, movePawn) > 0);
        assertTrue(defaultMoveComparator.compare(moveBishop, moveKing) > 0);

        assertTrue(defaultMoveComparator.compare(moveRook, moveQueen) < 0);
        assertTrue(defaultMoveComparator.compare(moveRook, moveKnight) < 0);
        assertTrue(defaultMoveComparator.compare(moveRook, moveBishop) < 0);
        assertTrue(defaultMoveComparator.compare(moveRook, movePawn) > 0);
        assertTrue(defaultMoveComparator.compare(moveRook, moveKing) > 0);

        assertTrue(defaultMoveComparator.compare(movePawn, moveQueen) < 0);
        assertTrue(defaultMoveComparator.compare(movePawn, moveKnight) < 0);
        assertTrue(defaultMoveComparator.compare(movePawn, moveBishop) < 0);
        assertTrue(defaultMoveComparator.compare(movePawn, moveRook) < 0);
        assertTrue(defaultMoveComparator.compare(movePawn, moveKing) > 0);

        assertTrue(defaultMoveComparator.compare(moveKing, moveQueen) < 0);
        assertTrue(defaultMoveComparator.compare(moveKing, moveKnight) < 0);
        assertTrue(defaultMoveComparator.compare(moveKing, moveBishop) < 0);
        assertTrue(defaultMoveComparator.compare(moveKing, moveRook) < 0);
        assertTrue(defaultMoveComparator.compare(moveKing, movePawn) < 0);
    }

    @Test
    public void testPawnMoveWhite() {
        Move move1 = createSimpleOneSquarePawnMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a3));
        Move move2 = createSimpleTwoSquaresPawnMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a4));

        assertTrue(defaultMoveComparator.compare(move1, move2) < 0);
        assertTrue(defaultMoveComparator.compare(move2, move1) > 0);
    }

    @Test
    public void testPawnMoveBlack() {
        Move move1 = createSimpleOneSquarePawnMove(PiecePositioned.of(Square.a7, Piece.PAWN_BLACK), PiecePositioned.getPosition(Square.a6));
        Move move2 = createSimpleTwoSquaresPawnMove(PiecePositioned.of(Square.a7, Piece.PAWN_BLACK), PiecePositioned.getPosition(Square.a5));

        assertTrue(defaultMoveComparator.compare(move1, move2) < 0);
        assertTrue(defaultMoveComparator.compare(move2, move1) > 0);
    }


    @Test
    public void testPawnAndKnightMove() {
        Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.b1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.a3));

        Move move2 = createSimpleTwoSquaresPawnMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a4));

        assertTrue(defaultMoveComparator.compare(move1, move2) > 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) < 0);
    }

    @Test
    public void testKnightMove() {
        Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.g1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.f3));

        Move move2 = createCaptureKnightMove(PiecePositioned.of(Square.g1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.h3));


        assertTrue(defaultMoveComparator.compare(move1, move2) < 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) > 0);
    }

    @Test
    public void testCapture01_White() {
        Move move1 = createCapturePawnMove(PiecePositioned.of(Square.e4, Piece.PAWN_WHITE), PiecePositioned.of(Square.f5, Piece.QUEEN_BLACK));

        Move move2 = createCaptureKnightMove(PiecePositioned.of(Square.h4, Piece.KNIGHT_WHITE), PiecePositioned.of(Square.f5, Piece.QUEEN_BLACK));

        assertTrue(defaultMoveComparator.compare(move1, move2) > 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) < 0);
    }


    @Test
    public void testCapture01_Black() {
        Move move1 = createCapturePawnMove(PiecePositioned.of(Square.e5, Piece.PAWN_BLACK), PiecePositioned.of(Square.f4, Piece.QUEEN_WHITE));

        Move move2 = createCaptureKnightMove(PiecePositioned.of(Square.h5, Piece.KNIGHT_BLACK), PiecePositioned.of(Square.f4, Piece.QUEEN_WHITE));

        assertTrue(defaultMoveComparator.compare(move1, move2) > 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) < 0);
    }

    @Test
    @Disabled
    public void sortMoveToEmptySquareWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createSimpleOneSquarePawnMove(PiecePositioned.of(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.sort(defaultMoveComparator);
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_WHITE, move.getFrom().piece());

        assertFalse(movesSortedIt.hasNext());
    }


    @Test
    @Disabled
    public void sortMoveCaptureWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createCapturePawnMove(PiecePositioned.of(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.of(Square.f3, Piece.QUEEN_BLACK)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createCaptureKnightMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.of(Square.e3, Piece.PAWN_BLACK)));

        moveList.sort(defaultMoveComparator);
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_WHITE, move.getFrom().piece());

        assertFalse(movesSortedIt.hasNext());
    }

    @Test
    @Disabled
    public void sortMoveToEmptySquareBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createSimpleOneSquarePawnMove(PiecePositioned.of(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPosition(Square.e6)));


        moveList.sort(defaultMoveComparator);
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_BLACK, move.getFrom().piece());

        assertFalse(movesSortedIt.hasNext());
    }

    @Test
    @Disabled
    public void sortMoveCaptureBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createCapturePawnMove(PiecePositioned.of(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.of(Square.f6, Piece.QUEEN_WHITE)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e5)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createCaptureKnightMove(PiecePositioned.of(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.of(Square.e6, Piece.PAWN_WHITE)));


        moveList.sort(defaultMoveComparator);
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_BLACK, move.getFrom().piece());

        assertFalse(movesSortedIt.hasNext());
    }

    @Test
    public void sort_Fried_Liver_Attack_Mirror() {
        Game blackGame = Game.from(FEN.of("r1bqkb1r/ppp2Npp/2n5/3np3/B1Q1P3/8/PPPP1PPP/RNB1K2R b KQkq - 0 1"));
        Game whiteGame = Game.from(FEN.of("r1bqkb1r/ppp2Npp/2n5/3np3/B1Q1P3/8/PPPP1PPP/RNB1K2R b KQkq - 0 1")).mirror();

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

        blackPossibleMovesList.sort(defaultMoveComparator.reversed());
        whitePossibleMovesList.sort(defaultMoveComparator.reversed());

        assertEquals(blackPossibleMovesList.size(), whitePossibleMovesList.size());

        for (int i = 0; i < blackPossibleMovesList.size(); i++) {
            Move move1 = blackPossibleMovesList.get(i);
            Move move2 = whitePossibleMovesList.get(i);
            assertEquals(move1.getFrom().piece(), move2.getFrom().piece().getOpposite());
            assertEquals(move1.getFrom().square(), move2.getFrom().square().mirror());
            assertEquals(move1.getTo().square(), move2.getTo().square().mirror());
        }

    }

    private Move createSimpleKnightMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }

    private Move createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }

    private Move createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }

    private Move createCaptureKnightMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }

    private Move createCapturePawnMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
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
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public void undoMove() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public Cardinal getMoveDirection() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public boolean isQuiet() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public long getZobristHash() {
                throw new RuntimeException("Not meant for execution");
            }
        };
    }

}
