package chess.builder;

import chess.Board;
import chess.BoardAnalyzer;
import chess.Color;
import chess.Pieza;
import chess.Square;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.moves.MoveFactory;
import chess.positioncaptures.Capturer;
import chess.positioncaptures.ImprovedCapturer;
import chess.pseudomovesfilters.LegalMoveCalculator;
import chess.pseudomovesfilters.MoveFilter;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public class ChessBuilderBoard implements ChessBuilder {
	
	private ChessFactory chessFactory = null;	
	
	private ChessBuilderParts builder = null;
	
	private Board board = null;
	
	private MoveGeneratorStrategy moveGeneratorStrategy = null;

	private ColorBoard colorBoard = null;
	
	private MoveCacheBoard moveCache = null;

	private KingCacheBoard kingCacheBoard = null;

	private BoardAnalyzer boardAnalyzer = null;

	private LegalMoveCalculator defaultMoveCalculator = null;

	private LegalMoveCalculator noCheckLegalMoveCalculator = null;

	private ImprovedCapturer improvedCapturer = null;

	private MoveFilter moveFilter;
	
	public ChessBuilderBoard() {
		this(new ChessFactory());
	}

	public ChessBuilderBoard(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
		this.builder = new ChessBuilderParts(chessFactory);
	}	
	
	public Board getBoard() {
		if (board == null) {
			board = chessFactory.createBoard();

			board.setDummyBoard(builder.getPosicionPiezaBoard());

			board.setBoardState(builder.getState());

			board.setKingCacheBoard(buildKingCacheBoard());

			board.setColorBoard(buildColorBoard());

			board.setMoveCache(buildMoveCache());

			board.setAnalyzer(buildAnalyzer());

		}
		return board;
	}
	
	protected BoardAnalyzer buildAnalyzer() {
		if (boardAnalyzer == null) {
			boardAnalyzer = new BoardAnalyzer();
			boardAnalyzer.setBoardState(builder.getState());
			boardAnalyzer.setKingCacheBoard(buildKingCacheBoard());
			boardAnalyzer.setCapturer(buildCapturer());
			boardAnalyzer.setDefaultMoveCalculator(buildDefaultMoveCalculator());
			boardAnalyzer.setNoCheckLegalMoveCalculator(buildNoCheckLegalMoveCalculator());			
		}
		return boardAnalyzer;
	}

	protected Capturer buildCapturer() {
		if(improvedCapturer == null){
			improvedCapturer = new ImprovedCapturer(builder.getPosicionPiezaBoard());
		}
		return improvedCapturer;
	}


	protected MoveCacheBoard buildMoveCache() {
		if (moveCache == null) {
			moveCache = chessFactory.createMoveCacheBoard(builder.getPosicionPiezaBoard(), buildMoveGeneratorStrategy());
		}
		return moveCache;
	}

	protected LegalMoveCalculator buildDefaultMoveCalculator() {
		if (defaultMoveCalculator == null) {
			defaultMoveCalculator = chessFactory.createDefaultLegalMoveCalculator(builder.getPosicionPiezaBoard(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), builder.getState(), buildMoveGeneratorStrategy(), buildMoveFilter());
		}
		return this.defaultMoveCalculator;
	}

	protected LegalMoveCalculator buildNoCheckLegalMoveCalculator() {
		if (noCheckLegalMoveCalculator == null) {
			noCheckLegalMoveCalculator = chessFactory.createNoCheckLegalMoveCalculator(builder.getPosicionPiezaBoard(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), builder.getState(), buildMoveGeneratorStrategy(), buildMoveFilter());
		}
		return noCheckLegalMoveCalculator;
	}	

	protected MoveGeneratorStrategy buildMoveGeneratorStrategy() {
		if (moveGeneratorStrategy == null) {
			moveGeneratorStrategy = new MoveGeneratorStrategy();
			moveGeneratorStrategy.setMoveFactory(new MoveFactory());
			moveGeneratorStrategy.setDummyBoard(builder.getPosicionPiezaBoard());
			moveGeneratorStrategy.setBoardState(builder.getState());
			moveGeneratorStrategy.setColorBoard(buildColorBoard());
		}
		return moveGeneratorStrategy;
	}


	protected KingCacheBoard buildKingCacheBoard() {
		if (kingCacheBoard == null) {
			kingCacheBoard = chessFactory.createKingCacheBoard( builder.getPosicionPiezaBoard() );
		}
		return kingCacheBoard;
	}

	protected ColorBoard buildColorBoard() {
		if (colorBoard == null) {
			colorBoard = chessFactory.createColorBoard( builder.getPosicionPiezaBoard() );
		}
		return colorBoard;
	}
	
	protected MoveFilter buildMoveFilter() {
		if (moveFilter == null) {
			moveFilter = chessFactory.createMoveFilter(builder.getPosicionPiezaBoard(), buildKingCacheBoard(), buildColorBoard(),  builder.getState(), buildCapturer());
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
	public void withCastlingWhiteReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		builder.withCastlingWhiteReinaPermitido(enroqueBlancoReinaPermitido);
	}

	@Override
	public void withCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
		builder.withCastlingWhiteKingPermitido(enroqueBlancoKingPermitido);
	}

	@Override
	public void withCastlingBlackReinaPermitido(boolean enroqueNegroReinaPermitido) {
		builder.withCastlingBlackReinaPermitido(enroqueNegroReinaPermitido);
	}

	@Override
	public void withCastlingBlackKingPermitido(boolean enroqueNegroKingPermitido) {
		builder.withCastlingBlackKingPermitido(enroqueNegroKingPermitido);
	}

	@Override
	public void withPieza(Square square, Pieza pieza) {
		builder.withPieza(square, pieza);
	}
}
