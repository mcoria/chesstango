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
public class ChessBuilderConcrete implements ChessBuilder {

	private Color turno;

	private Square peonPasanteSquare;

	private boolean enroqueBlancoReinaPermitido;

	private boolean enroqueBlancoReyPermitido;

	private boolean enroqueNegroReinaPermitido;
	
	private PosicionPiezaBoard posicionPiezaBoard;
	
	private Game game;

	private Board board;

	private MoveGeneratorStrategy moveGeneratorStrategy;

	private ColorBoard colorBoard;

	private BoardState boardState;
	
	private MoveCacheBoard moveCache;	

	private KingCacheBoard kingCacheBoard;

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

			board.setDummyBoard(getPosicionPiezaBoard());

			board.setBoardState(buildState());

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
			boardAnalyzer.setBoardState(buildState());
			boardAnalyzer.setKingCacheBoard(buildKingCacheBoard());
			boardAnalyzer.setCapturer(buildCapturer());
			boardAnalyzer.setDefaultMoveCalculator(buildDefaultMoveCalculator());
			boardAnalyzer.setNoCheckLegalMoveCalculator(buildNoCheckLegalMoveCalculator());			
		}
		return boardAnalyzer;
	}

	protected ImprovedCapturer buildCapturer() {
		if(improvedCapturer == null){
			improvedCapturer = new ImprovedCapturer(getPosicionPiezaBoard());
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
			defaultMoveCalculator = chessFactory.createDefaultLegalMoveCalculator(getPosicionPiezaBoard(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), buildState(), buildMoveGeneratorStrategy());
		}
		return this.defaultMoveCalculator;
	}
	
	protected NoCheckLegalMoveCalculator buildNoCheckLegalMoveCalculator() {
		if (noCheckLegalMoveCalculator == null) {
			noCheckLegalMoveCalculator = chessFactory.createNoCheckLegalMoveCalculator(getPosicionPiezaBoard(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), buildState(), buildMoveGeneratorStrategy());
		}
		return noCheckLegalMoveCalculator;
	}	

	protected MoveGeneratorStrategy buildMoveGeneratorStrategy() {
		if (moveGeneratorStrategy == null) {
			moveGeneratorStrategy = new MoveGeneratorStrategy();
			moveGeneratorStrategy.setDummyBoard(getPosicionPiezaBoard());
			moveGeneratorStrategy.setBoardState(buildState());
			moveGeneratorStrategy.setColorBoard(buildColorBoard());
			moveGeneratorStrategy.setIsKingInCheck(() -> buildAnalyzer().isKingInCheck());
		}
		return moveGeneratorStrategy;
	}

	protected KingCacheBoard buildKingCacheBoard() {
		if (kingCacheBoard == null) {
			kingCacheBoard = new KingCacheBoard(getPosicionPiezaBoard());
		}
		return kingCacheBoard;
	}

	protected ColorBoard buildColorBoard() {
		if (colorBoard == null) {
			colorBoard = chessFactory.createColorBoard(getPosicionPiezaBoard(), buildKingCacheBoard());
		}
		return colorBoard;
	}


	public PosicionPiezaBoard getPosicionPiezaBoard() {
		if(posicionPiezaBoard == null){
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


	@Override
	public void withTurno(Color turno) {
		this.turno = turno;
	}


	@Override
	public void withPeonPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}


	@Override
	public void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
	}

	@Override
	public void withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		this.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
	}


	@Override
	public void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
	}


	@Override
	public void withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		this.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
	}

	public void withPieza(Square square, Pieza pieza) {
		getPosicionPiezaBoard().setPieza(square, pieza);
	}

	public void withChessFactory(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
	}	

}
