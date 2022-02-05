package chess.builder;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorResult;


/**
 * @author Mauricio Coria
 *
 */
//TODO: El modelo de objetos es complejo, es necesario DI para inyectar dependencias
public class ChessPositionBuilderImp implements ChessPositionBuilder<ChessPosition> {
	
	private PiecePlacement piecePlacement;
	
	private PositionState positionState;
	
	private ColorBoard colorBoard = null;
	
	private MoveCacheBoard moveCache = null;

	private KingCacheBoard kingCacheBoard = null;	
	
	private ChessPosition chessPosition = null;	
	
	private ChessFactory chessFactory = null;
	
	public ChessPositionBuilderImp(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
	}
	
	@Override
	public ChessPosition getResult() {
		if (chessPosition == null) {
			chessPosition = chessFactory.createChessPosition();

			chessPosition.setPiecePlacement(getPiecePlacement());

			chessPosition.setBoardState(getPositionState());

			chessPosition.setKingCacheBoard(getKingCacheBoard());

			chessPosition.setColorBoard(getColorBoard());

			chessPosition.setMoveCache(getMoveCache());
		}
		return chessPosition;
	}	


	private void initCache() {
		MoveGenerator moveGenerator = new MoveGenerator();
		moveGenerator.setPiecePlacement(piecePlacement);
		moveGenerator.setBoardState(positionState);
		moveGenerator.setColorBoard(colorBoard);
		
		for(PiecePositioned origen: piecePlacement){
			
			if(origen.getValue() != null){

				MoveGeneratorResult generatorResult = moveGenerator.generatePseudoMoves(origen);
	
				moveCache.setPseudoMoves(origen.getKey(), generatorResult);
			}
		}		
		
	}

	public PiecePlacement getPiecePlacement() {
		if(piecePlacement == null){
			piecePlacement = chessFactory.createPiecePlacement();
		}
		return piecePlacement;
	}

	public PositionState getPositionState() {
		if (positionState == null) {
			positionState = chessFactory.createPositionState();
		}
		return positionState;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withTurno(Color turno) {
		this.getPositionState().setTurnoActual(turno);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withPawnPasanteSquare(Square peonPasanteSquare) {
		this.getPositionState().setPawnPasanteSquare(peonPasanteSquare);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		this.getPositionState().setCastlingWhiteQueenAllowed(enroqueWhiteQueenAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		this.getPositionState().setCastlingWhiteKingAllowed(enroqueWhiteKingAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		this.getPositionState().setCastlingBlackQueenAllowed(enroqueBlackQueenAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		this.getPositionState().setCastlingBlackKingAllowed(enroqueBlackKingAllowed);
		return this;
	}

	public ChessPositionBuilder<ChessPosition> withPieza(Square square, Piece piece) {
		this.getPiecePlacement().setPieza(square, piece);
		return this;
	}
	
	public KingCacheBoard getKingCacheBoard() {
		if (kingCacheBoard == null) {
			kingCacheBoard = chessFactory.createKingCacheBoard( getPiecePlacement() );
		}
		return kingCacheBoard;
	}

	public ColorBoard getColorBoard() {
		if (colorBoard == null) {
			colorBoard = chessFactory.createColorBoard( getPiecePlacement() );
		}
		return colorBoard;
	}
	
	public MoveCacheBoard getMoveCache() {
		if (moveCache == null) {
			moveCache = chessFactory.createMoveCacheBoard();
			initCache();
		}
		return moveCache;
	}	
	

}
