package chess.builder;

import chess.BoardAnalyzer;
import chess.Color;
import chess.Piece;
import chess.Square;
import chess.moves.MoveFactory;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.positioncaptures.Capturer;
import chess.positioncaptures.ImprovedCapturer;
import chess.pseudomovesfilters.LegalMoveCalculator;
import chess.pseudomovesfilters.MoveFilter;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionBuilderImp implements ChessPositionBuilder {
	
	private ChessFactory chessFactory = null;	
	
	private ChessPartsBuilder builder = null;
	
	private ChessPosition chessPosition = null;
	
	private MoveGeneratorStrategy moveGeneratorStrategy = null;

	private ColorBoard colorBoard = null;
	
	private MoveCacheBoard moveCache = null;

	private KingCacheBoard kingCacheBoard = null;

	private BoardAnalyzer boardAnalyzer = null;

	private LegalMoveCalculator defaultMoveCalculator = null;

	private LegalMoveCalculator noCheckLegalMoveCalculator = null;

	private ImprovedCapturer improvedCapturer = null;

	private MoveFilter moveFilter;

	public ChessPositionBuilderImp(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
		this.builder = new ChessPartsBuilder(chessFactory);
	}	
	
	public ChessPosition getChessPosition() {
		if (chessPosition == null) {
			chessPosition = chessFactory.createChessPosition();

			chessPosition.setDummyBoard(builder.getPiecePlacement());

			chessPosition.setBoardState(builder.getPositionState());

			chessPosition.setKingCacheBoard(buildKingCacheBoard());

			chessPosition.setColorBoard(buildColorBoard());

			chessPosition.setMoveCache(buildMoveCache());

			chessPosition.setAnalyzer(buildAnalyzer());

		}
		return chessPosition;
	}
	
	protected BoardAnalyzer buildAnalyzer() {
		if (boardAnalyzer == null) {
			boardAnalyzer = new BoardAnalyzer();
			boardAnalyzer.setBoardState(builder.getPositionState());
			boardAnalyzer.setKingCacheBoard(buildKingCacheBoard());
			boardAnalyzer.setCapturer(buildCapturer());
			boardAnalyzer.setDefaultMoveCalculator(buildDefaultMoveCalculator());
			boardAnalyzer.setNoCheckLegalMoveCalculator(buildNoCheckLegalMoveCalculator());			
		}
		return boardAnalyzer;
	}

	protected Capturer buildCapturer() {
		if(improvedCapturer == null){
			improvedCapturer = new ImprovedCapturer(builder.getPiecePlacement());
		}
		return improvedCapturer;
	}


	protected MoveCacheBoard buildMoveCache() {
		if (moveCache == null) {
			moveCache = chessFactory.createMoveCacheBoard(builder.getPiecePlacement(), buildMoveGeneratorStrategy());
		}
		return moveCache;
	}

	protected LegalMoveCalculator buildDefaultMoveCalculator() {
		if (defaultMoveCalculator == null) {
			defaultMoveCalculator = chessFactory.createDefaultLegalMoveCalculator(builder.getPiecePlacement(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), builder.getPositionState(), buildMoveGeneratorStrategy(), buildMoveFilter());
		}
		return this.defaultMoveCalculator;
	}

	protected LegalMoveCalculator buildNoCheckLegalMoveCalculator() {
		if (noCheckLegalMoveCalculator == null) {
			noCheckLegalMoveCalculator = chessFactory.createNoCheckLegalMoveCalculator(builder.getPiecePlacement(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), builder.getPositionState(), buildMoveGeneratorStrategy(), buildMoveFilter());
		}
		return noCheckLegalMoveCalculator;
	}	

	protected MoveGeneratorStrategy buildMoveGeneratorStrategy() {
		if (moveGeneratorStrategy == null) {
			moveGeneratorStrategy = new MoveGeneratorStrategy();
			moveGeneratorStrategy.setMoveFactory(new MoveFactory());
			moveGeneratorStrategy.setDummyBoard(builder.getPiecePlacement());
			moveGeneratorStrategy.setBoardState(builder.getPositionState());
			moveGeneratorStrategy.setColorBoard(buildColorBoard());
		}
		return moveGeneratorStrategy;
	}


	protected KingCacheBoard buildKingCacheBoard() {
		if (kingCacheBoard == null) {
			kingCacheBoard = chessFactory.createKingCacheBoard( builder.getPiecePlacement() );
		}
		return kingCacheBoard;
	}

	protected ColorBoard buildColorBoard() {
		if (colorBoard == null) {
			colorBoard = chessFactory.createColorBoard( builder.getPiecePlacement() );
		}
		return colorBoard;
	}
	
	protected MoveFilter buildMoveFilter() {
		if (moveFilter == null) {
			moveFilter = chessFactory.createMoveFilter(builder.getPiecePlacement(), buildKingCacheBoard(), buildColorBoard(),  builder.getPositionState(), buildCapturer());
		}
		return moveFilter;
	}	

	@Override
	public void withTurno(Color turno) {
		builder.withTurno(turno);
	}

	@Override
	public void withPawnPasanteSquare(Square peonPasanteSquare) {
		builder.withPawnPasanteSquare(peonPasanteSquare);
	}

	@Override
	public void withCastlingWhiteQueenAllowed(boolean enroqueBlancoQueenAllowed) {
		builder.withCastlingWhiteQueenAllowed(enroqueBlancoQueenAllowed);
	}

	@Override
	public void withCastlingWhiteKingAllowed(boolean enroqueBlancoKingAllowed) {
		builder.withCastlingWhiteKingAllowed(enroqueBlancoKingAllowed);
	}

	@Override
	public void withCastlingBlackQueenAllowed(boolean enroqueNegroQueenAllowed) {
		builder.withCastlingBlackQueenAllowed(enroqueNegroQueenAllowed);
	}

	@Override
	public void withCastlingBlackKingAllowed(boolean enroqueNegroKingAllowed) {
		builder.withCastlingBlackKingAllowed(enroqueNegroKingAllowed);
	}

	@Override
	public void withPieza(Square square, Piece piece) {
		builder.withPieza(square, piece);
	}
}
