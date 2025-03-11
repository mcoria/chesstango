package net.chesstango.board.moves.generators.pseudo.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.generators.pseudo.strategies.*;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPiecePositioned;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.BitBoardReader;
import net.chesstango.board.position.KingSquare;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.SquareBoardReader;

/**
 * @author Mauricio Coria
 */
public class MoveGeneratorImp implements MoveGenerator {
    private final MoveFactory moveFactoryWhite;
    private final MoveFactory moveFactoryBlack;

    private final PawnWhiteMoveGenerator pawnWhiteMoveGenerator;
    private final PawnBlackMoveGenerator pawnBlackMoveGenerator;
    private final RookMoveGenerator rookMoveGeneratorWhite;
    private final RookMoveGenerator rookMoveGeneratorBlack;
    private final KnightMoveGenerator knightMoveGeneratorWhite;
    private final KnightMoveGenerator knightMoveGeneratorBlack;
    private final BishopMoveGenerator bishopMoveGeneratorWhite;
    private final BishopMoveGenerator bishopMoveGeneratorBlack;
    private final QueenMoveGenerator queenMoveGeneratorWhite;
    private final QueenMoveGenerator queenMoveGeneratorBlack;
    private final KingWhiteMoveGenerator kingWhiteMoveGenerator;
    private final KingBlackMoveGenerator kingBlackMoveGenerator;
    private final MoveGeneratorEnPassantImp moveGeneratorEnPassant;

    private SquareBoardReader squareBoardReader;
    private BitBoardReader bitBoardReader;
    private PositionStateReader positionStateReader;
    private KingSquare kingSquare;

    public MoveGeneratorImp() {
        pawnWhiteMoveGenerator = new PawnWhiteMoveGenerator();

        pawnBlackMoveGenerator = new PawnBlackMoveGenerator();

        rookMoveGeneratorWhite = new RookMoveGenerator(Color.WHITE);

        rookMoveGeneratorBlack = new RookMoveGenerator(Color.BLACK);

        knightMoveGeneratorWhite = new KnightMoveGenerator(Color.WHITE);

        knightMoveGeneratorBlack = new KnightMoveGenerator(Color.BLACK);

        bishopMoveGeneratorWhite = new BishopMoveGenerator(Color.WHITE);

        bishopMoveGeneratorBlack = new BishopMoveGenerator(Color.BLACK);

        queenMoveGeneratorWhite = new QueenMoveGenerator(Color.WHITE);

        queenMoveGeneratorBlack = new QueenMoveGenerator(Color.BLACK);

        kingWhiteMoveGenerator = new KingWhiteMoveGenerator();

        kingBlackMoveGenerator = new KingBlackMoveGenerator();

        moveGeneratorEnPassant = new MoveGeneratorEnPassantImp();

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
        return moveGeneratorEnPassant.generateEnPassantPseudoMoves();
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

    public void setKingSquare(KingSquare kingSquare) {
        this.kingSquare = kingSquare;
        setupMoveGenerators();
    }

    private void setupMoveGenerators() {
        setupMoveGenerator(pawnWhiteMoveGenerator, Color.WHITE);

        setupMoveGenerator(pawnBlackMoveGenerator, Color.BLACK);

        setupMoveGenerator(rookMoveGeneratorWhite, Color.WHITE);

        setupMoveGenerator(rookMoveGeneratorBlack, Color.BLACK);

        setupMoveGenerator(knightMoveGeneratorWhite, Color.WHITE);

        setupMoveGenerator(knightMoveGeneratorBlack, Color.BLACK);

        setupMoveGenerator(bishopMoveGeneratorWhite, Color.WHITE);

        setupMoveGenerator(bishopMoveGeneratorBlack, Color.BLACK);

        setupMoveGenerator(queenMoveGeneratorWhite, Color.WHITE);

        setupMoveGenerator(queenMoveGeneratorBlack, Color.BLACK);

        setupMoveGenerator(kingWhiteMoveGenerator, Color.WHITE);

        setupMoveGenerator(kingBlackMoveGenerator, Color.BLACK);

        setupEnPassantMoveGenerator();
    }

    private void setupMoveGenerator(MoveGeneratorByPiecePositioned moveGeneratorByPiecePositioned, Color color) {
        if (moveGeneratorByPiecePositioned instanceof AbstractMoveGenerator generator) {
            generator.setSquareBoard(squareBoardReader);
            generator.setBitBoard(bitBoardReader);
        }

        MoveFactory moveFactory = Color.WHITE.equals(color) ? moveFactoryWhite : moveFactoryBlack;

        if (moveGeneratorByPiecePositioned instanceof AbstractPawnMoveGenerator abstractPawnMoveGenerator) {
            abstractPawnMoveGenerator.setMoveFactory(moveFactory);
        }

        if (moveGeneratorByPiecePositioned instanceof AbstractKingMoveGenerator abstractKingMoveGenerator) {
            abstractKingMoveGenerator.setMoveFactory(moveFactory);
        }

        if (moveGeneratorByPiecePositioned instanceof RookMoveGenerator rookMoveGenerator) {
            rookMoveGenerator.setMoveFactory(moveFactory);
        }

        if (moveGeneratorByPiecePositioned instanceof BishopMoveGenerator bishopMoveGenerator) {
            bishopMoveGenerator.setMoveFactory(moveFactory);
        }

        if (moveGeneratorByPiecePositioned instanceof KnightMoveGenerator knightMoveGenerator) {
            knightMoveGenerator.setMoveFactory(moveFactory);
        }

        if (moveGeneratorByPiecePositioned instanceof QueenMoveGenerator queenMoveGenerator) {
            queenMoveGenerator.setMoveFactory(moveFactory);
        }

        if (moveGeneratorByPiecePositioned instanceof AbstractKingMoveGenerator generator) {
            generator.setBoardState(positionStateReader);
            generator.setKingCacheBoard(kingSquare);
        }
    }


    private void setupEnPassantMoveGenerator() {
        moveGeneratorEnPassant.setPositionState(positionStateReader);
        moveGeneratorEnPassant.setPiecePlacement(squareBoardReader);
    }


    private MoveGeneratorByPiecePositioned selectMoveGeneratorStrategy(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> this.pawnWhiteMoveGenerator;
            case PAWN_BLACK -> this.pawnBlackMoveGenerator;
            case ROOK_WHITE -> this.rookMoveGeneratorWhite;
            case ROOK_BLACK -> this.rookMoveGeneratorBlack;
            case KNIGHT_WHITE -> this.knightMoveGeneratorWhite;
            case KNIGHT_BLACK -> this.knightMoveGeneratorBlack;
            case BISHOP_WHITE -> this.bishopMoveGeneratorWhite;
            case BISHOP_BLACK -> this.bishopMoveGeneratorBlack;
            case QUEEN_WHITE -> this.queenMoveGeneratorWhite;
            case QUEEN_BLACK -> this.queenMoveGeneratorBlack;
            case KING_WHITE -> this.kingWhiteMoveGenerator;
            case KING_BLACK -> this.kingBlackMoveGenerator;
            default -> throw new RuntimeException("Generator not found");
        };
    }

}
