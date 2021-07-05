package builder;

import chess.Board;
import chess.BoardAnalyzer;
import chess.BoardState;
import chess.Color;
import chess.Game;
import chess.Pieza;
import chess.Square;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import layers.imp.ArrayPosicionPiezaBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;
import positioncaptures.ImprovedCapturer;

/*
 * Esto hay que mejorarlo, se me ocurre implementar un Director
 * hay pasos intermedios aca que podrian ser encapsulados.
 * por ejemplo todo aquello que tiene que ver con la creacion del BoardState
 */
public class ChessBuilder {

	private Game game;

	private Board board;

	private MoveGeneratorStrategy moveGeneratorStrategy;

	private ColorBoard colorBoard;

	private BoardState boardState;
	
	private MoveCacheBoard moveCache;	

	private PosicionPiezaBoard posicionPiezaBoard;

	private KingCacheBoard kingCacheBoard;

	private Color turno;

	private Square peonPasanteSquare;

	private boolean enroqueBlancoReinaPermitido;

	private boolean enroqueBlancoReyPermitido;

	private boolean enroqueNegroReinaPermitido;

	private boolean enroqueNegroReyPermitido;

	private BoardAnalyzer boardAnalyzer;

	private DefaultLegalMoveCalculator defaultMoveCalculator;

	private NoCheckLegalMoveCalculator noCheckLegalMoveCalculator;

	private ImprovedCapturer improvedCapturer;
	
	private ChessFactory chessFactory = new ChessFactory();

	public Game buildGame() {
		if (game == null) {
			game = new Game(buildBoard());
		}
		return game;
	}

	public Board buildBoard() {
		if (board == null) {
			board = chessFactory.createBoard();

			board.setDummyBoard(buildPosicionPiezaBoard());

			board.setBoardState(buildState());

			board.setKingCacheBoard(buildKingCacheBoard());

			board.setColorBoard(buildColorBoard());

			board.setMoveCache(buildMoveCache());

			board.setAnalyzer(buildAnalyzer());

			board.setDefaultMoveCalculator(buildDefaultMoveCalculator());

			board.setNoCheckLegalMoveCalculator(buildNoCheckLegalMoveCalculator());

		}
		return board;
	}

	private BoardAnalyzer buildAnalyzer() {
		if (boardAnalyzer == null) {
			boardAnalyzer = new BoardAnalyzer();
			boardAnalyzer.setBoardState(buildState());
			boardAnalyzer.setKingCacheBoard(buildKingCacheBoard());
			boardAnalyzer.setCapturer(buildCapturer());
		}
		return boardAnalyzer;
	}

	protected ImprovedCapturer buildCapturer() {
		if(improvedCapturer == null){
			improvedCapturer = new ImprovedCapturer(buildPosicionPiezaBoard());
		}
		return improvedCapturer;
	}


	private MoveCacheBoard buildMoveCache() {
		if (moveCache == null) {
			moveCache = new MoveCacheBoard();
		}
		return moveCache;
	}

	protected DefaultLegalMoveCalculator buildDefaultMoveCalculator() {
		if (defaultMoveCalculator == null) {
			defaultMoveCalculator = chessFactory.createDefaultLegalMoveCalculator(buildPosicionPiezaBoard(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), buildState(), buildMoveGeneratorStrategy());
		}
		return this.defaultMoveCalculator;
	}
	
	protected NoCheckLegalMoveCalculator buildNoCheckLegalMoveCalculator() {
		if (noCheckLegalMoveCalculator == null) {
			noCheckLegalMoveCalculator = chessFactory.createNoCheckLegalMoveCalculator(buildPosicionPiezaBoard(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), buildState(), buildMoveGeneratorStrategy());
		}
		return noCheckLegalMoveCalculator;
	}	

	protected MoveGeneratorStrategy buildMoveGeneratorStrategy() {
		if (moveGeneratorStrategy == null) {
			moveGeneratorStrategy = new MoveGeneratorStrategy();
			moveGeneratorStrategy.setDummyBoard(buildPosicionPiezaBoard());
			moveGeneratorStrategy.setBoardState(buildState());
			moveGeneratorStrategy.setColorBoard(buildColorBoard());
			moveGeneratorStrategy.setIsKingInCheck(() -> buildAnalyzer().isKingInCheck());
		}
		return moveGeneratorStrategy;
	}

	protected KingCacheBoard buildKingCacheBoard() {
		if (kingCacheBoard == null) {
			kingCacheBoard = new KingCacheBoard(buildPosicionPiezaBoard());
		}
		return kingCacheBoard;
	}

	protected ColorBoard buildColorBoard() {
		if (colorBoard == null) {
			colorBoard = chessFactory.createColorBoard(buildPosicionPiezaBoard(), buildKingCacheBoard());
		}
		return colorBoard;
	}


	public PosicionPiezaBoard buildPosicionPiezaBoard() {
		if (posicionPiezaBoard == null) {
			posicionPiezaBoard = new ArrayPosicionPiezaBoard();
		}
		return posicionPiezaBoard;
	}

	protected BoardState buildState() {
		if (boardState == null) {
			boardState = new BoardState();
			boardState.setTurnoActual(this.turno == null ? Color.BLANCO : this.turno);
			boardState.setPeonPasanteSquare(peonPasanteSquare);
			boardState.setEnroqueBlancoReinaPermitido(enroqueBlancoReinaPermitido);
			boardState.setEnroqueBlancoReyPermitido(enroqueBlancoReyPermitido);
			boardState.setEnroqueNegroReinaPermitido(enroqueNegroReinaPermitido);
			boardState.setEnroqueNegroReyPermitido(enroqueNegroReyPermitido);
		}
		return boardState;
	}

	public ChessBuilder withTurno(Color turno) {
		this.turno = turno;
		return this;
	}

	public ChessBuilder withPeonPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
		return this;
	}

	public ChessBuilder withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
		return this;
	}

	public ChessBuilder withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		this.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
		return this;
	}

	public ChessBuilder withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
		return this;
	}

	public ChessBuilder withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		this.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
		return this;
	}

	public void withPieza(Square square, Pieza pieza) {
		buildPosicionPiezaBoard().setPieza(square, pieza);
	}
	
	public ChessBuilder withChessFactory(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
		return this;
	}	

}
