package net.chesstango.board.internal.moves.generators.pseudo;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.internal.moves.generators.pseudo.strategies.*;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPiece;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.*;

/**
 * Implementation of the MoveGenerator interface.
 * This class provides methods to generate pseudo-moves for different pieces
 * and handles special moves like en passant and castling.
 * It uses various move generator strategies for different piece types.
 * The `MoveGeneratorImp` class implements the Strategy design pattern by encapsulating various move
 * generation algorithms for different chess pieces and selecting the appropriate algorithm at runtime
 * based on the piece type.
 *
 * @author Mauricio Coria
 */
public class MoveGeneratorImp implements MoveGenerator {
    private final PawnWhiteMoveGenerator pawnWhiteMoveGenerator;
    private final PawnBlackMoveGenerator pawnBlackMoveGenerator;
    private final RookMoveGenerator rookWhiteMoveGenerator;
    private final RookMoveGenerator rookBlackMoveGenerator;
    private final KnightMoveGenerator knightWhiteMoveGenerator;
    private final KnightMoveGenerator knightBlackMoveGenerator;
    private final BishopMoveGenerator bishopWhiteMoveGenerator;
    private final BishopMoveGenerator bishopBlackMoveGenerator;
    private final QueenMoveGenerator queenWhiteMoveGenerator;
    private final QueenMoveGenerator queenBlackMoveGenerator;
    private final KingWhiteMoveGenerator kingWhiteMoveGenerator;
    private final KingBlackMoveGenerator kingBlackMoveGenerator;

    private MoveFactory moveFactoryWhite;
    private MoveFactory moveFactoryBlack;
    private SquareBoardReader squareBoardReader;
    private BitBoardReader bitBoardReader;
    private PositionStateReader positionStateReader;
    private KingSquareReader kingSquareReader;

    public MoveGeneratorImp() {
        this.pawnWhiteMoveGenerator = new PawnWhiteMoveGenerator();

        this.pawnBlackMoveGenerator = new PawnBlackMoveGenerator();

        this.rookWhiteMoveGenerator = new RookMoveGenerator(Color.WHITE);

        this.rookBlackMoveGenerator = new RookMoveGenerator(Color.BLACK);

        this.knightWhiteMoveGenerator = new KnightMoveGenerator(Color.WHITE);

        this.knightBlackMoveGenerator = new KnightMoveGenerator(Color.BLACK);

        this.bishopWhiteMoveGenerator = new BishopMoveGenerator(Color.WHITE);

        this.bishopBlackMoveGenerator = new BishopMoveGenerator(Color.BLACK);

        this.queenWhiteMoveGenerator = new QueenMoveGenerator(Color.WHITE);

        this.queenBlackMoveGenerator = new QueenMoveGenerator(Color.BLACK);

        this.kingWhiteMoveGenerator = new KingWhiteMoveGenerator();

        this.kingBlackMoveGenerator = new KingBlackMoveGenerator();
    }


    @Override
    public MoveGeneratorByPieceResult generateByPiecePseudoMoves(PiecePositioned from) {
        Piece piece = from.piece();
        MoveGeneratorByPiece strategy = selectMoveGeneratorStrategy(piece);
        return strategy.generateByPiecePseudoMoves(from);
    }

    @Override
    public MovePair<PseudoMove> generateEnPassantPseudoMoves() {
        if (Color.WHITE.equals(positionStateReader.getCurrentTurn())) {
            return pawnWhiteMoveGenerator.generateEnPassantPseudoMoves();
        } else {
            return pawnBlackMoveGenerator.generateEnPassantPseudoMoves();
        }
    }


    @Override
    public MovePair<PseudoMove> generateCastlingPseudoMoves() {
        if (Color.WHITE.equals(positionStateReader.getCurrentTurn())) {
            return kingWhiteMoveGenerator.generateCastlingPseudoMoves();
        } else {
            return kingBlackMoveGenerator.generateCastlingPseudoMoves();
        }
    }

    public void setSquareBoardReader(SquareBoardReader squareBoardReader) {
        this.squareBoardReader = squareBoardReader;
        setupMoveGenerators();
    }

    public void setBitBoardReader(BitBoardReader bitBoardReader) {
        this.bitBoardReader = bitBoardReader;
        setupMoveGenerators();
    }

    public void setBoardState(PositionStateReader positionState) {
        this.positionStateReader = positionState;
        setupMoveGenerators();
    }

    public void setKingSquareReader(KingSquare kingSquareReader) {
        this.kingSquareReader = kingSquareReader;
        setupMoveGenerators();
    }

    public void setMoveFactoryWhite(MoveFactory moveFactoryWhite) {
        this.moveFactoryWhite = moveFactoryWhite;
        setupMoveGenerators();
    }

    public void setMoveFactoryBlack(MoveFactory moveFactoryBlack) {
        this.moveFactoryBlack = moveFactoryBlack;
        setupMoveGenerators();
    }


    private void setupMoveGenerators() {
        if (moveFactoryWhite != null && moveFactoryBlack != null) {
            setupMoveGenerator(pawnWhiteMoveGenerator, moveFactoryWhite);

            setupMoveGenerator(pawnBlackMoveGenerator, moveFactoryBlack);

            setupMoveGenerator(rookWhiteMoveGenerator, moveFactoryWhite);

            setupMoveGenerator(rookBlackMoveGenerator, moveFactoryBlack);

            setupMoveGenerator(knightWhiteMoveGenerator, moveFactoryWhite);

            setupMoveGenerator(knightBlackMoveGenerator, moveFactoryBlack);

            setupMoveGenerator(bishopWhiteMoveGenerator, moveFactoryWhite);

            setupMoveGenerator(bishopBlackMoveGenerator, moveFactoryBlack);

            setupMoveGenerator(queenWhiteMoveGenerator, moveFactoryWhite);

            setupMoveGenerator(queenBlackMoveGenerator, moveFactoryBlack);

            setupMoveGenerator(kingWhiteMoveGenerator, moveFactoryWhite);

            setupMoveGenerator(kingBlackMoveGenerator, moveFactoryBlack);
        }
    }

    private void setupMoveGenerator(AbstractMoveGenerator abstractMoveGenerator, MoveFactory moveFactory) {
        abstractMoveGenerator.setSquareBoard(squareBoardReader);
        abstractMoveGenerator.setBitBoard(bitBoardReader);

        if (abstractMoveGenerator instanceof AbstractPawnMoveGenerator abstractPawnMoveGenerator) {
            abstractPawnMoveGenerator.setMoveFactory(moveFactory);
            abstractPawnMoveGenerator.setPositionState(positionStateReader);
        }

        if (abstractMoveGenerator instanceof AbstractKingMoveGenerator abstractKingMoveGenerator) {
            abstractKingMoveGenerator.setMoveFactory(moveFactory);
            abstractKingMoveGenerator.setPositionState(positionStateReader);
            abstractKingMoveGenerator.setKingSquare(kingSquareReader);
        }

        if (abstractMoveGenerator instanceof RookMoveGenerator rookMoveGenerator) {
            rookMoveGenerator.setMoveFactory(moveFactory);
        }

        if (abstractMoveGenerator instanceof BishopMoveGenerator bishopMoveGenerator) {
            bishopMoveGenerator.setMoveFactory(moveFactory);
        }

        if (abstractMoveGenerator instanceof KnightMoveGenerator knightMoveGenerator) {
            knightMoveGenerator.setMoveFactory(moveFactory);
        }

        if (abstractMoveGenerator instanceof QueenMoveGenerator queenMoveGenerator) {
            queenMoveGenerator.setMoveFactory(moveFactory);
        }
    }


    /**
     * Selects the appropriate move generator strategy based on the given piece.
     * This method uses a switch expression to return the corresponding move generator
     * for the specified piece type. If the piece type is not recognized, it throws a RuntimeException.
     *
     * @param piece the piece for which to select the move generator strategy
     * @return the move generator strategy for the specified piece
     * @throws RuntimeException if the piece type is not recognized
     */
    private MoveGeneratorByPiece selectMoveGeneratorStrategy(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> this.pawnWhiteMoveGenerator;
            case PAWN_BLACK -> this.pawnBlackMoveGenerator;
            case ROOK_WHITE -> this.rookWhiteMoveGenerator;
            case ROOK_BLACK -> this.rookBlackMoveGenerator;
            case KNIGHT_WHITE -> this.knightWhiteMoveGenerator;
            case KNIGHT_BLACK -> this.knightBlackMoveGenerator;
            case BISHOP_WHITE -> this.bishopWhiteMoveGenerator;
            case BISHOP_BLACK -> this.bishopBlackMoveGenerator;
            case QUEEN_WHITE -> this.queenWhiteMoveGenerator;
            case QUEEN_BLACK -> this.queenBlackMoveGenerator;
            case KING_WHITE -> this.kingWhiteMoveGenerator;
            case KING_BLACK -> this.kingBlackMoveGenerator;
            default -> throw new RuntimeException("Generator not found");
        };
    }

}
