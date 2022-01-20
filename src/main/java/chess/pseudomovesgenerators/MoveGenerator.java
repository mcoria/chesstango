package chess.pseudomovesgenerators;

import java.util.Collection;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.moves.imp.MoveFactoryBlack;
import chess.moves.imp.MoveFactoryWhite;
import chess.position.ColorBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.pseudomovesgenerators.strategies.AbstractKingMoveGenerator;
import chess.pseudomovesgenerators.strategies.AbstractMoveGenerator;
import chess.pseudomovesgenerators.strategies.BishopMoveGenerator;
import chess.pseudomovesgenerators.strategies.KingBlackMoveGenerator;
import chess.pseudomovesgenerators.strategies.KingWhiteMoveGenerator;
import chess.pseudomovesgenerators.strategies.KnightMoveGenerator;
import chess.pseudomovesgenerators.strategies.PawnBlackMoveGenerator;
import chess.pseudomovesgenerators.strategies.PawnWhiteMoveGenerator;
import chess.pseudomovesgenerators.strategies.QueenMoveGenerator;
import chess.pseudomovesgenerators.strategies.RookMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGenerator implements MoveGeneratorStrategy{
	private PiecePlacement dummyBoard;
	private ColorBoard colorBoard;
	private PositionState positionState;
	
	private MoveFactory moveFactoryWhite;
	private MoveFactory moveFactoryBlack;
	
	private PawnWhiteMoveGenerator pbmg;
	private PawnBlackMoveGenerator pnmg;
	private RookMoveGenerator tbmg;
	private RookMoveGenerator tnmg;
	private KnightMoveGenerator cbmg;
	private KnightMoveGenerator cnmg;
	private BishopMoveGenerator abmg;
	private BishopMoveGenerator anmg;
	private QueenMoveGenerator rebmg;
	private QueenMoveGenerator renmg;
	private KingWhiteMoveGenerator rbmg;
	private KingBlackMoveGenerator rnmg;

	private PawnPasanteMoveGenerator ppmg = null;
	
	public MoveGenerator() {
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
		
		ppmg = new PawnPasanteMoveGenerator();
		
		moveFactoryWhite = new MoveFactoryWhite();
		moveFactoryBlack = new MoveFactoryBlack();
	}
	

	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
		Piece piece = origen.getValue();
		MoveGeneratorStrategy strategy = piece.selectMoveGeneratorStrategy(this);
		return strategy.generatePseudoMoves(origen);
	}
	
	public Collection<Move> generatoPawnPasantePseudoMoves() {
		return ppmg.generatePseudoMoves();
	}	
	
	//TODO: Necesitamos esto por el metodo kingMoveGenerator.getPinnedSquare(kingSquare). Deberiamos moverlo a su propia clase
	public AbstractKingMoveGenerator getKingMoveGenerator(Color color) {
		return Color.WHITE.equals(color) ? this.rbmg : this.rnmg;
	}

	public void setPiecePlacement(PiecePlacement dummyBoard) {
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
		
		settupPawnPasanteMoveGenerator();
	}	
	
	private void settupMoveGenerator(MoveGeneratorStrategy moveGeneratorStrategy) {
		if (moveGeneratorStrategy instanceof AbstractMoveGenerator) {
			((AbstractMoveGenerator)moveGeneratorStrategy).setTablero(dummyBoard);
			((AbstractMoveGenerator)moveGeneratorStrategy).setColorBoard(colorBoard);		
			
			if(moveGeneratorStrategy.equals(pbmg) || moveGeneratorStrategy.equals(tbmg) || moveGeneratorStrategy.equals(cbmg) || moveGeneratorStrategy.equals(abmg) || moveGeneratorStrategy.equals(rebmg) || moveGeneratorStrategy.equals(rbmg)){
				((AbstractMoveGenerator)moveGeneratorStrategy).setMoveFactory(moveFactoryWhite);
			} else {
				((AbstractMoveGenerator)moveGeneratorStrategy).setMoveFactory(moveFactoryBlack);
			}
		}
		
		if (moveGeneratorStrategy instanceof AbstractKingMoveGenerator) {
			AbstractKingMoveGenerator generator = (AbstractKingMoveGenerator) moveGeneratorStrategy;
			generator.setBoardState(positionState);
		}
		
	}
	

	private void settupPawnPasanteMoveGenerator() {
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
	
	/*
	 * //MoveGeneratorStrategy strategy = selectMoveGeneratorStrategy(origen.getValue());
	 */
	/*
	protected MoveGeneratorStrategy selectMoveGeneratorStrategy(Piece piece){
		MoveGeneratorStrategy value  = null;
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
	}*/	

}
