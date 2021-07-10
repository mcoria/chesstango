package builder;

import chess.Board;
import chess.BoardAnalyzer;
import chess.Color;
import chess.Pieza;
import chess.Square;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;
import positioncaptures.ImprovedCapturer;

public class ChessBuilderBoard implements ChessBuilder{
	
	private ChessFactory chessFactory = new ChessFactory();	
	
	private ChessBuilderParts builder = new ChessBuilderParts();
	
	private Board board = null;
	
	private MoveGeneratorStrategy moveGeneratorStrategy = null;

	private ColorBoard colorBoard = null;
	
	private MoveCacheBoard moveCache = null;

	private KingCacheBoard kingCacheBoard = null;

	private BoardAnalyzer boardAnalyzer = null;

	private DefaultLegalMoveCalculator defaultMoveCalculator = null;

	private NoCheckLegalMoveCalculator noCheckLegalMoveCalculator = null;

	private ImprovedCapturer improvedCapturer = null;
	
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
	

	@Override
	public void withTurno(Color turno) {
		builder.withTurno(turno);
	}

	@Override
	public void withPeonPasanteSquare(Square peonPasanteSquare) {
		builder.withPeonPasanteSquare(peonPasanteSquare);
	}

	@Override
	public void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		builder.withEnroqueBlancoReinaPermitido(enroqueBlancoReinaPermitido);
	}

	@Override
	public void withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		builder.withEnroqueBlancoReyPermitido(enroqueBlancoReyPermitido);
	}

	@Override
	public void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		builder.withEnroqueNegroReinaPermitido(enroqueNegroReinaPermitido);
	}

	@Override
	public void withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		builder.withEnroqueNegroReyPermitido(enroqueNegroReyPermitido);
	}

	@Override
	public void withPieza(Square square, Pieza pieza) {
		builder.withPieza(square, pieza);
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

	protected ImprovedCapturer buildCapturer() {
		if(improvedCapturer == null){
			improvedCapturer = new ImprovedCapturer(builder.getPosicionPiezaBoard());
		}
		return improvedCapturer;
	}


	protected MoveCacheBoard buildMoveCache() {
		if (moveCache == null) {
			moveCache = new MoveCacheBoard();
		}
		return moveCache;
	}

	protected DefaultLegalMoveCalculator buildDefaultMoveCalculator() {
		if (defaultMoveCalculator == null) {
			defaultMoveCalculator = chessFactory.createDefaultLegalMoveCalculator(builder.getPosicionPiezaBoard(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), builder.getState(), buildMoveGeneratorStrategy());
		}
		return this.defaultMoveCalculator;
	}
	
	protected NoCheckLegalMoveCalculator buildNoCheckLegalMoveCalculator() {
		if (noCheckLegalMoveCalculator == null) {
			noCheckLegalMoveCalculator = chessFactory.createNoCheckLegalMoveCalculator(builder.getPosicionPiezaBoard(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), builder.getState(), buildMoveGeneratorStrategy());
		}
		return noCheckLegalMoveCalculator;
	}	

	protected MoveGeneratorStrategy buildMoveGeneratorStrategy() {
		if (moveGeneratorStrategy == null) {
			moveGeneratorStrategy = new MoveGeneratorStrategy();
			moveGeneratorStrategy.setDummyBoard(builder.getPosicionPiezaBoard());
			moveGeneratorStrategy.setBoardState(builder.getState());
			moveGeneratorStrategy.setColorBoard(buildColorBoard());
			moveGeneratorStrategy.setIsKingInCheck(() -> buildAnalyzer().isKingInCheck());
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
			colorBoard = chessFactory.createColorBoard(builder.getPosicionPiezaBoard(), buildKingCacheBoard());
		}
		return colorBoard;
	}	

}
