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
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.pseudomovesgenerators.MoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionBuilderImp implements ChessPositionBuilder {
	
	private ChessFactory chessFactory = null;	
	
	private ChessPositionPartsBuilder builder = null;
	
	private ChessPosition chessPosition = null;
	
	private MoveGenerator moveGenerator = null;

	private ColorBoard colorBoard = null;
	
	private MoveCacheBoard moveCache = null;

	private KingCacheBoard kingCacheBoard = null;

	private PositionAnalyzer positionAnalyzer = null;

	private LegalMoveGenerator defaultMoveCalculator = null;

	private LegalMoveGenerator noCheckLegalMoveGenerator = null;

	private Capturer capturer = null;

	private MoveFilter moveFilter;
	
	private Game game = null;
	
	public ChessPositionBuilderImp() {
		this(new ChessFactory());
	}	

	public ChessPositionBuilderImp(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
		this.builder = new ChessPositionPartsBuilder(chessFactory);
	}	
	
	public ChessPosition getChessPosition() {
		if (chessPosition == null) {
			chessPosition = chessFactory.createChessPosition();

			chessPosition.setPiecePlacement(builder.getPiecePlacement());

			chessPosition.setBoardState(builder.getPositionState());

			chessPosition.setKingCacheBoard(buildKingCacheBoard());

			chessPosition.setColorBoard(buildColorBoard());

			chessPosition.setMoveCache(buildMoveCache());
		}
		return chessPosition;
	}	
	
	public Game getGame() {
		if (game == null) {
			game = new Game(getChessPosition());
			game.setAnalyzer(getAnalyzer());			
		}
		return game;
	}	
	
	public PositionAnalyzer getAnalyzer() {
		if (positionAnalyzer == null) {
			positionAnalyzer = new PositionAnalyzer();
			positionAnalyzer.setBoardState(builder.getPositionState());
			positionAnalyzer.setKingCacheBoard(buildKingCacheBoard());
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


	protected MoveCacheBoard buildMoveCache() {
		if (moveCache == null) {
			moveCache = chessFactory.createMoveCacheBoard(builder.getPiecePlacement(), buildMoveGeneratorStrategy());
		}
		return moveCache;
	}

	protected LegalMoveGenerator buildDefaultMoveCalculator() {
		if (defaultMoveCalculator == null) {
			defaultMoveCalculator = chessFactory.createDefaultLegalMoveGenerator(builder.getPiecePlacement(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), builder.getPositionState(), buildMoveGeneratorStrategy(), buildMoveFilter());
		}
		return this.defaultMoveCalculator;
	}

	protected LegalMoveGenerator buildNoCheckLegalMoveGenerator() {
		if (noCheckLegalMoveGenerator == null) {
			noCheckLegalMoveGenerator = chessFactory.createNoCheckLegalMoveGenerator(builder.getPiecePlacement(), buildKingCacheBoard(), buildColorBoard(),
					buildMoveCache(), builder.getPositionState(), buildMoveGeneratorStrategy(), buildMoveFilter());
		}
		return noCheckLegalMoveGenerator;
	}	

	protected MoveGenerator buildMoveGeneratorStrategy() {
		if (moveGenerator == null) {
			moveGenerator = new MoveGenerator();
			moveGenerator.setPiecePlacement(builder.getPiecePlacement());
			moveGenerator.setBoardState(builder.getPositionState());
			moveGenerator.setColorBoard(buildColorBoard());
		}
		return moveGenerator;
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
	public void withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		builder.withCastlingWhiteQueenAllowed(enroqueWhiteQueenAllowed);
	}

	@Override
	public void withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		builder.withCastlingWhiteKingAllowed(enroqueWhiteKingAllowed);
	}

	@Override
	public void withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		builder.withCastlingBlackQueenAllowed(enroqueBlackQueenAllowed);
	}

	@Override
	public void withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		builder.withCastlingBlackKingAllowed(enroqueBlackKingAllowed);
	}

	@Override
	public void withPieza(Square square, Piece piece) {
		builder.withPieza(square, piece);
	}
}
