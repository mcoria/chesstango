package builder;

import chess.Board;
import chess.BoardState;
import chess.Color;
import chess.Game;
import chess.Pieza;
import chess.Square;
import layers.ColorBoard;
import layers.DefaultDummyBoard;
import layers.DummyBoard;
import movegenerators.MoveGeneratorStrategy;

/*
 * Esto hay que mejorarlo, se me ocurre implementar un Director
 * hay pasos intermedios aca que podrian ser encapsulados.
 * por ejemplo todo aquello que tiene que ver con la creacion del BoardState
 */
public abstract class ChessBuilder {
	
	private Game game;
	
	private Board board;
	
	private MoveGeneratorStrategy moveGeneratorStrategy;
	
	private ColorBoard colorBoard;
	
	private BoardState boardState;
	
	private DummyBoard dummyBoard;

	private Pieza[][] tablero;
	
	private Color turno;
	
	private Square peonPasanteSquare;

	private boolean enroqueBlancoReinaPermitido;

	private boolean enroqueBlancoReyPermitido;

	private boolean enroqueNegroReinaPermitido;

	private boolean enroqueNegroReyPermitido;
	
	public Game buildGame(){
		if(game == null){
			game = new Game(buildBoard());
		}
		return game;
	}
	
	public Board buildBoard(){
		if(board == null){
			board = new Board(buildDummyBoard(), buildState(), this);
		}
		return board;
	}
	
	public MoveGeneratorStrategy buildMoveGeneratorStrategy() {
		if (moveGeneratorStrategy == null) {
			moveGeneratorStrategy = new MoveGeneratorStrategy();
			moveGeneratorStrategy.setDummyBoard(buildDummyBoard());
			moveGeneratorStrategy.setBoardState(buildState() );
			moveGeneratorStrategy.setColorBoard(buildColorBoard());			
		}
		return moveGeneratorStrategy;
	}
	
	public ColorBoard buildColorBoard() {
		if(colorBoard == null){
			colorBoard = new ColorBoard(buildDummyBoard());
		}
		return colorBoard;
	}

	public DummyBoard buildDummyBoard() {
		if (dummyBoard == null) {
			dummyBoard = new DefaultDummyBoard(tablero);
		}
		return dummyBoard;
	}

	public BoardState buildState() {
		if(boardState == null) {
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

	public ChessBuilder withTablero(Pieza[][] tablero) {
		this.tablero = tablero;
		return this;
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

}
