package chess.position;

import chess.ChessPositionReader;
import chess.Color;
import chess.PiecePositioned;
import chess.Square;
import chess.ascii.ASCIIEncoder;
import chess.builder.ChessPositionBuilder;
import chess.fen.FENEncoder;
import chess.moves.Move;
import chess.moves.MoveKing;
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.imp.MoveGeneratorImp;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPosition implements ChessPositionReader {

	// PosicionPiezaBoard y ColorBoard son representaciones distintas del tablero. Uno con mas informacion que la otra.
	protected PiecePlacement piecePlacement = null;
	protected ColorBoard colorBoard = null;
	protected KingCacheBoard kingCacheBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected PositionState positionState = null;	

	public void acceptForExecute(Move move) {
		move.executeMove(this);
	}

	public void executeMove(Move move) {
		move.executeMove(this.piecePlacement);

		move.executeMove(this.colorBoard);

		move.executeMove(this.moveCache);

		move.executeMove(this.positionState);	
		
	}

	public void executeMove(MoveKing move) {
		executeMove((Move)move);
		
		move.executeMove(this.kingCacheBoard);
		
	}	

	public void acceptForUndo(Move move) {
		move.undoMove(this);
	}

	public void undoMove(Move move) {
		move.undoMove(this.positionState);

		move.undoMove(this.moveCache);

		move.undoMove(this.colorBoard);

		move.undoMove(this.piecePlacement);
		
	}
	

	public void undoMove(MoveKing move) {
		undoMove((Move)move);

		move.undoMove(this.kingCacheBoard);
	}	
	
	@Override
	public void constructBoardRepresentation(ChessPositionBuilder<?> builder){		
		builder.withTurno(positionState.getTurnoActual())
				.withCastlingWhiteQueenAllowed(positionState.isCastlingWhiteQueenAllowed())
				.withCastlingWhiteKingAllowed(positionState.isCastlingWhiteKingAllowed())
				.withCastlingBlackQueenAllowed(positionState.isCastlingBlackQueenAllowed())
				.withCastlingBlackKingAllowed(positionState.isCastlingBlackKingAllowed())
				.withPawnPasanteSquare(positionState.getPawnPasanteSquare());
		
		for(PiecePositioned pieza: piecePlacement){
			builder.withPieza(pieza.getKey(), pieza.getValue());
		}
	}
	
	
	@Override
	public String toString() {
		FENEncoder fenEncoder = new FENEncoder();
		ASCIIEncoder asciiEncoder = new ASCIIEncoder();
		
		constructBoardRepresentation(fenEncoder);
		constructBoardRepresentation(asciiEncoder);
		
	    return asciiEncoder.getResult() + fenEncoder.getResult();
	}

	public void setPiecePlacement(PiecePlacement dummyBoard) {
		this.piecePlacement = dummyBoard;
	}

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}


	public void setMoveCache(MoveCacheBoard moveCache) {
		this.moveCache = moveCache;
	}

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}


	@Override
	public Color getTurnoActual() {
		return this.positionState.getTurnoActual();
	}

	@Override
	public Square getPawnPasanteSquare() {
		return this.positionState.getPawnPasanteSquare();
	}

	@Override
	public boolean isCastlingWhiteQueenAllowed() {
		return this.positionState.isCastlingWhiteQueenAllowed();
	}	

	@Override
	public boolean isCastlingWhiteKingAllowed() {
		return this.positionState.isCastlingWhiteKingAllowed();
	}


	@Override
	public boolean isCastlingBlackQueenAllowed() {
		return this.positionState.isCastlingBlackQueenAllowed();
	}


	@Override
	public boolean isCastlingBlackKingAllowed() {
		return this.positionState.isCastlingBlackKingAllowed();
	}


	public void init() {
		colorBoard.init(piecePlacement);
		kingCacheBoard.init(piecePlacement);		
		initCache();
	}
	
	private void initCache() {
		MoveGeneratorImp moveGeneratorImp = new MoveGeneratorImp();
		moveGeneratorImp.setPiecePlacement(piecePlacement);
		moveGeneratorImp.setBoardState(positionState);
		moveGeneratorImp.setColorBoard(colorBoard);
		
		for(PiecePositioned origen: piecePlacement){
			
			if(origen.getValue() != null){

				MoveGeneratorResult generatorResult = moveGeneratorImp.generatePseudoMoves(origen);
	
				moveCache.setPseudoMoves(origen.getKey(), generatorResult);
			}
		}		
		
	}	
	
}
