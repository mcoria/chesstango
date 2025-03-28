package net.chesstango.board.moves.generators.pseudo.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPiecePositioned;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.generators.pseudo.imp.strategies.*;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public class MoveGeneratorImp implements MoveGenerator {
    private final MoveFactory moveFactoryWhite;
    private final MoveFactory moveFactoryBlack;

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

    private SquareBoardReader squareBoardReader;
    private BitBoardReader bitBoardReader;
    private PositionStateReader positionStateReader;
    private KingSquareReader kingSquareReader;

    public MoveGeneratorImp() {
        pawnWhiteMoveGenerator = new PawnWhiteMoveGenerator();

        pawnBlackMoveGenerator = new PawnBlackMoveGenerator();

        rookWhiteMoveGenerator = new RookMoveGenerator(Color.WHITE);

        rookBlackMoveGenerator = new RookMoveGenerator(Color.BLACK);

        knightWhiteMoveGenerator = new KnightMoveGenerator(Color.WHITE);

        knightBlackMoveGenerator = new KnightMoveGenerator(Color.BLACK);

        bishopWhiteMoveGenerator = new BishopMoveGenerator(Color.WHITE);

        bishopBlackMoveGenerator = new BishopMoveGenerator(Color.BLACK);

        queenWhiteMoveGenerator = new QueenMoveGenerator(Color.WHITE);

        queenBlackMoveGenerator = new QueenMoveGenerator(Color.BLACK);

        kingWhiteMoveGenerator = new KingWhiteMoveGenerator();

        kingBlackMoveGenerator = new KingBlackMoveGenerator();

        moveFactoryWhite = SingletonMoveFactories.getDefaultMoveFactoryWhite();

        moveFactoryBlack = SingletonMoveFactories.getDefaultMoveFactoryBlack();
    }


    @Override
    public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
        Piece piece = origen.getPiece();
        MoveGeneratorByPiecePositioned strategy = selectMoveGeneratorStrategy(piece);
        return strategy.generatePseudoMoves(origen);
    }

    @Override
    public MovePair generateEnPassantPseudoMoves() {
        if (Color.WHITE.equals(positionStateReader.getCurrentTurn())) {
            return pawnWhiteMoveGenerator.generateEnPassantPseudoMoves();
        } else {
            return pawnBlackMoveGenerator.generateEnPassantPseudoMoves();
        }
    }


    @Override
    public MovePair generateCastlingPseudoMoves() {
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

    private void setupMoveGenerators() {
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


    private MoveGeneratorByPiecePositioned selectMoveGeneratorStrategy(Piece piece) {
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
