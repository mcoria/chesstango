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

    private final PawnWhiteMoveGenerator pbmg;
    private final PawnBlackMoveGenerator pnmg;
    private final RookMoveGenerator tbmg;
    private final RookMoveGenerator tnmg;
    private final KnightMoveGenerator cbmg;
    private final KnightMoveGenerator cnmg;
    private final BishopMoveGenerator abmg;
    private final BishopMoveGenerator anmg;
    private final QueenMoveGenerator rebmg;
    private final QueenMoveGenerator renmg;
    private final KingWhiteMoveGenerator rbmg;
    private final KingBlackMoveGenerator rnmg;
    private final MoveGeneratorEnPassantImp ppmg;

    private SquareBoardReader piecePlacement;
    private BitBoardReader colorBoard;
    private PositionStateReader positionState;
    private KingSquare kingSquare;

    public MoveGeneratorImp() {
        pbmg = new PawnWhiteMoveGenerator();

        pnmg = new PawnBlackMoveGenerator();

        tbmg = new RookMoveGenerator(Color.WHITE);

        tnmg = new RookMoveGenerator(Color.BLACK);

        cbmg = new KnightMoveGenerator(Color.WHITE);

        cnmg = new KnightMoveGenerator(Color.BLACK);

        abmg = new BishopMoveGenerator(Color.WHITE);

        anmg = new BishopMoveGenerator(Color.BLACK);

        rebmg = new QueenMoveGenerator(Color.WHITE);

        renmg = new QueenMoveGenerator(Color.BLACK);

        rbmg = new KingWhiteMoveGenerator();

        rnmg = new KingBlackMoveGenerator();

        ppmg = new MoveGeneratorEnPassantImp();

        moveFactoryWhite = SingletonMoveFactories.getDefaultMoveFactoryWhite();
        moveFactoryBlack = SingletonMoveFactories.getDefaultMoveFactoryBlack();
    }


    @Override
    public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
        Piece piece = origen.getPiece();
        //MoveGeneratorStrategy strategy = piece.selectMoveGeneratorStrategy(this);
        MoveGeneratorByPiecePositioned strategy = selectMoveGeneratorStrategy(piece);
        return strategy.generatePseudoMoves(origen);
    }

    @Override
    public MovePair generateEnPassantPseudoMoves() {
        return ppmg.generateEnPassantPseudoMoves();
    }


    @Override
    public MovePair generateCastlingPseudoMoves() {
        if (Color.WHITE.equals(positionState.getCurrentTurn())) {
            return rbmg.generateCastlingPseudoMoves();
        } else {
            return rnmg.generateCastlingPseudoMoves();
        }
    }

    public void setPiecePlacement(SquareBoardReader dummyBoard) {
        this.piecePlacement = dummyBoard;
        setupMoveGenerators();
    }

    public void setColorBoard(BitBoardReader colorBoard) {
        this.colorBoard = colorBoard;
        setupMoveGenerators();
    }

    public void setBoardState(PositionStateReader positionState) {
        this.positionState = positionState;
        setupMoveGenerators();
    }

    public void setKingSquare(KingSquare kingSquare) {
        this.kingSquare = kingSquare;
        setupMoveGenerators();
    }

    private void setupMoveGenerators() {
        setupMoveGenerator(pbmg, Color.WHITE);

        setupMoveGenerator(pnmg, Color.BLACK);

        setupMoveGenerator(tbmg, Color.WHITE);

        setupMoveGenerator(tnmg, Color.BLACK);

        setupMoveGenerator(cbmg, Color.WHITE);

        setupMoveGenerator(cnmg, Color.BLACK);

        setupMoveGenerator(abmg, Color.WHITE);

        setupMoveGenerator(anmg, Color.BLACK);

        setupMoveGenerator(rebmg, Color.WHITE);

        setupMoveGenerator(renmg, Color.BLACK);

        setupMoveGenerator(rbmg, Color.WHITE);

        setupMoveGenerator(rnmg, Color.BLACK);

        setupEnPassantMoveGenerator();
    }

    private void setupMoveGenerator(MoveGeneratorByPiecePositioned moveGeneratorByPiecePositioned, Color color) {
        if (moveGeneratorByPiecePositioned instanceof AbstractMoveGenerator generator) {
            generator.setSquareBoard(piecePlacement);
            generator.setBitBoard(colorBoard);
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
            generator.setBoardState(positionState);
            generator.setKingCacheBoard(kingSquare);
        }
    }


    private void setupEnPassantMoveGenerator() {
        ppmg.setPositionState(positionState);
        ppmg.setPiecePlacement(piecePlacement);
    }


    public PawnWhiteMoveGenerator getPawnWhiteMoveGenerator() {
        return pbmg;
    }

    public PawnBlackMoveGenerator getPawnBlackMoveGenerator() {
        return pnmg;
    }

    public RookMoveGenerator getRookWhiteMoveGenerator() {
        return tbmg;
    }

    public RookMoveGenerator getRookBlackMoveGenerator() {
        return tnmg;
    }

    public KnightMoveGenerator getKnightWhiteMoveGenerator() {
        return cbmg;
    }

    public KnightMoveGenerator getKnightBlackMoveGenerator() {
        return cnmg;
    }

    public BishopMoveGenerator getBishopWhiteMoveGenerator() {
        return abmg;
    }

    public BishopMoveGenerator getBishopBlackMoveGenerator() {
        return anmg;
    }

    public QueenMoveGenerator getQueenWhiteMoveGenerator() {
        return rebmg;
    }

    public QueenMoveGenerator getQueenBlackMoveGenerator() {
        return renmg;
    }

    public KingWhiteMoveGenerator getKingWhiteMoveGenerator() {
        return rbmg;
    }

    public KingBlackMoveGenerator getKingBlackMoveGenerator() {
        return rnmg;
    }


    protected MoveGeneratorByPiecePositioned selectMoveGeneratorStrategy(Piece piece) {
        MoveGeneratorByPiecePositioned value = null;
        switch (piece) {
            case PAWN_WHITE:
                value = this.pbmg;
                break;
            case PAWN_BLACK:
                value = this.pnmg;
                break;
            case ROOK_WHITE:
                value = this.tbmg;
                break;
            case ROOK_BLACK:
                value = this.tnmg;
                break;
            case KNIGHT_WHITE:
                value = this.cbmg;
                break;
            case KNIGHT_BLACK:
                value = this.cnmg;
                break;
            case BISHOP_WHITE:
                value = this.abmg;
                break;
            case BISHOP_BLACK:
                value = this.anmg;
                break;
            case QUEEN_WHITE:
                value = this.rebmg;
                break;
            case QUEEN_BLACK:
                value = this.renmg;
                break;
            case KING_WHITE:
                value = this.rbmg;
                break;
            case KING_BLACK:
                value = this.rnmg;
                break;
            default:
                throw new RuntimeException("Generator not found");
        }
        return value;
    }

}
