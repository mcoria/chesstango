package net.chesstango.board.movesgenerators.pseudo.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorByPiecePositioned;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.movesgenerators.pseudo.strategies.*;
import net.chesstango.board.position.BoardReader;
import net.chesstango.board.position.ColorBoardReader;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.KingSquare;

/**
 * @author Mauricio Coria
 *
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
	
	private BoardReader piecePlacement;
	private ColorBoardReader colorBoard;
	private PositionStateReader positionState;
	private KingSquare kingSquare;
	
	public MoveGeneratorImp() {
		pbmg =  new PawnWhiteMoveGenerator();
		
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

	public void setPiecePlacement(BoardReader dummyBoard) {
		this.piecePlacement = dummyBoard;
		setupMoveGenerators();
	}

	public void setColorBoard(ColorBoardReader colorBoard) {
		this.colorBoard = colorBoard;
		setupMoveGenerators();
	}

	public void setBoardState(PositionStateReader positionState) {
		this.positionState = positionState;
		setupMoveGenerators();
	}
	
	public void setKingCacheBoard(KingSquare kingSquare) {
		this.kingSquare = kingSquare;
		setupMoveGenerators();
	}	
	
	private void setupMoveGenerators(){
		setupMoveGenerator(pbmg);

		setupMoveGenerator(pnmg);

		setupMoveGenerator(tbmg);
		
		setupMoveGenerator(tnmg);
		
		setupMoveGenerator(cbmg);
		
		setupMoveGenerator(cnmg);
		
		setupMoveGenerator(abmg);
		
		setupMoveGenerator(anmg);

		setupMoveGenerator(rebmg);

		setupMoveGenerator(renmg);

		setupMoveGenerator(rbmg);

		setupMoveGenerator(rnmg);
		
		setupEnPassantMoveGenerator();
	}	
	
	private void setupMoveGenerator(MoveGeneratorByPiecePositioned moveGeneratorByPiecePositioned) {
		if (moveGeneratorByPiecePositioned instanceof AbstractMoveGenerator) {
			AbstractMoveGenerator generator = (AbstractMoveGenerator) moveGeneratorByPiecePositioned;
			generator.setBoard(piecePlacement);
			generator.setColorBoard(colorBoard);
			
			if(moveGeneratorByPiecePositioned.equals(pbmg) || moveGeneratorByPiecePositioned.equals(tbmg) || moveGeneratorByPiecePositioned.equals(cbmg) || moveGeneratorByPiecePositioned.equals(abmg) || moveGeneratorByPiecePositioned.equals(rebmg) || moveGeneratorByPiecePositioned.equals(rbmg)){
				generator.setMoveFactory(moveFactoryWhite);
			} else {
				generator.setMoveFactory(moveFactoryBlack);
			}
		}
		
		if (moveGeneratorByPiecePositioned instanceof AbstractKingMoveGenerator) {
			AbstractKingMoveGenerator generator = (AbstractKingMoveGenerator) moveGeneratorByPiecePositioned;
			generator.setBoardState(positionState);
			generator.setKingCacheBoard(kingSquare);
		}
	}
	

	private void setupEnPassantMoveGenerator() {
		ppmg.setBoardState(positionState);
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
	

	protected MoveGeneratorByPiecePositioned selectMoveGeneratorStrategy(Piece piece){
		MoveGeneratorByPiecePositioned value  = null;
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
