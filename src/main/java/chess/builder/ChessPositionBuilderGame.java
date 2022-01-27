package chess.builder;

import chess.Color;
import chess.Game;
import chess.Piece;
import chess.Square;
import chess.analyzer.Capturer;
import chess.analyzer.PositionAnalyzer;
import chess.legalmovesgenerators.LegalMoveGenerator;
import chess.legalmovesgenerators.MoveFilter;
import chess.position.ChessPosition;
import chess.pseudomovesgenerators.MoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionBuilderGame implements ChessPositionBuilder<Game> {
	
	private ChessFactory chessFactory = null;	
	
	private ChessPositionBuilderImp builder = null;
	
	private MoveGenerator moveGenerator = null;

	private PositionAnalyzer positionAnalyzer = null;

	private LegalMoveGenerator defaultMoveCalculator = null;

	private LegalMoveGenerator noCheckLegalMoveGenerator = null;

	private Capturer capturer = null;

	private MoveFilter moveFilter;
	
	private Game game = null;
	
	public ChessPositionBuilderGame() {
		this(new ChessFactory());
	}	

	public ChessPositionBuilderGame(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
		this.builder = new ChessPositionBuilderImp(chessFactory);
	}
	
	
	@Override
	public Game getResult() {
		if (game == null) {
			game = new Game(builder.getResult());
			game.setAnalyzer(getAnalyzer());			
		}
		return game;
	}
	
	public ChessPosition getChessPosition() {
		return builder.getResult();
	}	
	
	public PositionAnalyzer getAnalyzer() {
		if (positionAnalyzer == null) {
			positionAnalyzer = new PositionAnalyzer();
			positionAnalyzer.setBoardState(builder.getPositionState());
			positionAnalyzer.setKingCacheBoard(builder.getKingCacheBoard());
			positionAnalyzer.setCapturer(buildCapturer());
			positionAnalyzer.setDefaultMoveCalculator(buildDefaultMoveCalculator());
			positionAnalyzer.setNoCheckLegalMoveGenerator(buildNoCheckLegalMoveGenerator());			
		}
		return positionAnalyzer;
	}

	protected Capturer buildCapturer() {
		if(capturer == null){
			capturer = new Capturer(builder.getPiecePlacement());
		}
		return capturer;
	}

	protected LegalMoveGenerator buildDefaultMoveCalculator() {
		if (defaultMoveCalculator == null) {
			defaultMoveCalculator = chessFactory.createDefaultLegalMoveGenerator(builder.getPiecePlacement(), builder.getKingCacheBoard(), builder.getColorBoard(),
					builder.getMoveCache(), builder.getPositionState(), buildMoveGeneratorStrategy(), buildMoveFilter());
		}
		return this.defaultMoveCalculator;
	}

	protected LegalMoveGenerator buildNoCheckLegalMoveGenerator() {
		if (noCheckLegalMoveGenerator == null) {
			noCheckLegalMoveGenerator = chessFactory.createNoCheckLegalMoveGenerator(builder.getPiecePlacement(), builder.getKingCacheBoard(), builder.getColorBoard(),
					builder.getMoveCache(), builder.getPositionState(), buildMoveGeneratorStrategy(), buildMoveFilter());
		}
		return noCheckLegalMoveGenerator;
	}	

	protected MoveGenerator buildMoveGeneratorStrategy() {
		if (moveGenerator == null) {
			moveGenerator = new MoveGenerator();
			moveGenerator.setPiecePlacement(builder.getPiecePlacement());
			moveGenerator.setBoardState(builder.getPositionState());
			moveGenerator.setColorBoard(builder.getColorBoard());
		}
		return moveGenerator;
	}
	
	protected MoveFilter buildMoveFilter() {
		if (moveFilter == null) {
			moveFilter = chessFactory.createMoveFilter(builder.getPiecePlacement(), builder.getKingCacheBoard(), builder.getColorBoard(),  builder.getPositionState(), buildCapturer());
		}
		return moveFilter;
	}	

	@Override
	public ChessPositionBuilder<Game> withTurno(Color turno) {
		builder.withTurno(turno);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withPawnPasanteSquare(Square peonPasanteSquare) {
		builder.withPawnPasanteSquare(peonPasanteSquare);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		builder.withCastlingWhiteQueenAllowed(enroqueWhiteQueenAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		builder.withCastlingWhiteKingAllowed(enroqueWhiteKingAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		builder.withCastlingBlackQueenAllowed(enroqueBlackQueenAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		builder.withCastlingBlackKingAllowed(enroqueBlackKingAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withPieza(Square square, Piece piece) {
		builder.withPieza(square, piece);
		return this;
	}
}
