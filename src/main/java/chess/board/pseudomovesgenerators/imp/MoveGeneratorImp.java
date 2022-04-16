package chess.board.pseudomovesgenerators.imp;

import java.util.Collection;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.moves.Move;
import chess.board.moves.MoveFactory;
import chess.board.moves.imp.MoveFactoryBlack;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.PiecePlacementReader;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;
import chess.board.pseudomovesgenerators.MoveGenerator;
import chess.board.pseudomovesgenerators.MoveGeneratorByPiecePositioned;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;
import chess.board.pseudomovesgenerators.strategies.AbstractKingMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.AbstractMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.BishopMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.KingBlackMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.KingWhiteMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.KnightMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.PawnBlackMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.PawnWhiteMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.QueenMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.RookMoveGenerator;

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
	
	private PiecePlacementReader dummyBoard;
	private ColorBoard colorBoard;
	private PositionState positionState;	
	private KingCacheBoard kingCacheBoard;
	
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
		
		moveFactoryWhite = new MoveFactoryWhite();
		moveFactoryBlack = new MoveFactoryBlack();
	}
	

	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
		Piece piece = origen.getValue();
		//MoveGeneratorStrategy strategy = piece.selectMoveGeneratorStrategy(this);
		MoveGeneratorByPiecePositioned strategy = selectMoveGeneratorStrategy(piece);
		return strategy.generatePseudoMoves(origen);
	}
	
	@Override
	public Collection<Move> generateEnPassantPseudoMoves() {
		return ppmg.generateEnPassantPseudoMoves();
	}
	

	@Override
	public Collection<Move> generateCastlingPseudoMoves() {
		if (Color.WHITE.equals(positionState.getTurnoActual())) {
			return rbmg.generateCastlingPseudoMoves();
		} else {
			return rnmg.generateCastlingPseudoMoves();
		}
	}	

	public void setPiecePlacement(PiecePlacementReader dummyBoard) {
		this.dummyBoard = dummyBoard;
		settupMoveGenerators();
	}

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
		settupMoveGenerators();
	}

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
		settupMoveGenerators();
	}
	
	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
		settupMoveGenerators();
	}	
	
	private void settupMoveGenerators(){
		settupMoveGenerator(pbmg);

		settupMoveGenerator(pnmg);

		settupMoveGenerator(tbmg);
		
		settupMoveGenerator(tnmg);
		
		settupMoveGenerator(cbmg);
		
		settupMoveGenerator(cnmg);
		
		settupMoveGenerator(abmg);
		
		settupMoveGenerator(anmg);
		
		settupMoveGenerator(rebmg);
		
		settupMoveGenerator(renmg);
		
		settupMoveGenerator(rbmg);
		
		settupMoveGenerator(rnmg);
		
		settupEnPassantMoveGenerator();
	}	
	
	private void settupMoveGenerator(MoveGeneratorByPiecePositioned moveGeneratorByPiecePositioned) {
		if (moveGeneratorByPiecePositioned instanceof AbstractMoveGenerator) {
			AbstractMoveGenerator generator = (AbstractMoveGenerator) moveGeneratorByPiecePositioned;
			generator.setPiecePlacement(dummyBoard);
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
			generator.setKingCacheBoard(kingCacheBoard);
		}
	}
	

	private void settupEnPassantMoveGenerator() {
		ppmg.setBoardState(positionState);
		ppmg.setTablero(dummyBoard);
	}
	
	
	public PawnWhiteMoveGenerator getPawnWhiteMoveGenerator() {
		return pbmg;
	}

	public PawnBlackMoveGenerator getPawnBlackMoveGenerator() {
		return pnmg;
	}

	public RookMoveGenerator getRookBlancaMoveGenerator() {
		return tbmg;
	}

	public RookMoveGenerator getRookNegraMoveGenerator() {
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
	
	public QueenMoveGenerator getQueenBlancaMoveGenerator() {
		return rebmg;
	}
	
	public QueenMoveGenerator getQueenNegraMoveGenerator() {
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
