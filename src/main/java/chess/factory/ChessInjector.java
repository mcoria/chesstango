/**
 * 
 */
package chess.factory;

import chess.Game;
import chess.GameState;
import chess.analyzer.Capturer;
import chess.analyzer.PositionAnalyzer;
import chess.legalmovesgenerators.LegalMoveGenerator;
import chess.legalmovesgenerators.MoveFilter;
import chess.position.ChessPosition;
import chess.position.PiecePlacement;
import chess.position.imp.ChessPositionImp;
import chess.position.imp.ColorBoard;
import chess.position.imp.KingCacheBoard;
import chess.position.imp.MoveCacheBoard;
import chess.position.imp.PositionState;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.imp.MoveGeneratorImp;

/**
 * @author Mauricio Coria
 *
 */
public class ChessInjector {
	private PiecePlacement piecePlacement = null;
	
	private PositionState positionState = null;
	
	private ColorBoard colorBoard = null;
	
	private MoveCacheBoard moveCache = null;

	private KingCacheBoard kingCacheBoard = null;	
	
	private ChessPosition chessPosition = null;	
	
	private MoveGenerator moveGenerator = null;

	private PositionAnalyzer positionAnalyzer = null;

	private LegalMoveGenerator defaultMoveCalculator = null;

	private LegalMoveGenerator noCheckLegalMoveGenerator = null;

	private Capturer capturer = null;

	private MoveFilter moveFilter;
	
	private GameState gameState = null;	
	
	private Game game = null;	
	
	private final ChessFactory chessFactory;
	
	public ChessInjector() {
		this.chessFactory = new ChessFactory();
	}
	
	public ChessInjector(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
	}
	
	public ChessPosition getChessPosition() {
		if (chessPosition == null) {
			ChessPositionImp chessPositionImp = chessFactory.createChessPosition();

			chessPositionImp.setPiecePlacement(getPiecePlacement());

			chessPositionImp.setBoardState(getPositionState());

			chessPositionImp.setKingCacheBoard(getKingCacheBoard());

			chessPositionImp.setColorBoard(getColorBoard());

			chessPositionImp.setMoveCache(getMoveCacheBoard());
			
			chessPosition = chessPositionImp;
		}
		return chessPosition;
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
	
	public KingCacheBoard getKingCacheBoard() {
		if (kingCacheBoard == null) {
			kingCacheBoard = chessFactory.createKingCacheBoard();
		}
		return kingCacheBoard;
	}

	public ColorBoard getColorBoard() {
		if (colorBoard == null) {
			colorBoard = chessFactory.createColorBoard();
		}
		return colorBoard;
	}
	
	public MoveCacheBoard getMoveCacheBoard() {
		if (moveCache == null) {
			moveCache = chessFactory.createMoveCacheBoard();
		}
		return moveCache;
	}
	
	public Game getGame() {
		if (game == null) {
			game = chessFactory.createGame(getChessPosition(), getAnalyzer(), getGameState());
			game.init();
		}
		return game;
	}
	

	public GameState getGameState() {
		if (gameState == null) {
			gameState = chessFactory.createGameState();
		}
		return gameState ;
	}

	public PositionAnalyzer getAnalyzer() {
		if (positionAnalyzer == null) {
			positionAnalyzer = chessFactory.createPositionAnalyzer();
			positionAnalyzer.setPositionReader(getChessPosition());
			positionAnalyzer.setCapturer(getCapturer());
			positionAnalyzer.setDefaultMoveCalculator(getDefaultMoveCalculator());
			positionAnalyzer.setNoCheckLegalMoveGenerator(getNoCheckLegalMoveGenerator());			
			positionAnalyzer.setGameState(getGameState());
		}
		return positionAnalyzer;
	}

	public Capturer getCapturer() {
		if(capturer == null){
			capturer = chessFactory.creareCapturer(getPiecePlacement());
		}
		return capturer;
	}

	public LegalMoveGenerator getDefaultMoveCalculator() {
		if (defaultMoveCalculator == null) {
			defaultMoveCalculator = chessFactory.createDefaultLegalMoveGenerator(getChessPosition(), getMoveGenerator(), getMoveFilter());
		}
		return this.defaultMoveCalculator;
	}

	public LegalMoveGenerator getNoCheckLegalMoveGenerator() {
		if (noCheckLegalMoveGenerator == null) {
			noCheckLegalMoveGenerator = chessFactory.createNoCheckLegalMoveGenerator(getChessPosition(), getMoveGenerator(), getMoveFilter());
		}
		return noCheckLegalMoveGenerator;
	}	

	public MoveGenerator getMoveGenerator() {
		if (moveGenerator == null) {
			MoveGeneratorImp moveGeneratorImp = new MoveGeneratorImp();
			moveGeneratorImp.setPiecePlacement(getPiecePlacement());
			moveGeneratorImp.setBoardState(getPositionState());
			moveGeneratorImp.setColorBoard(getColorBoard());
			
			moveGenerator =  chessFactory.createMoveGenaratorWithCacheProxy(moveGeneratorImp, getMoveCacheBoard());
		}
		return moveGenerator;
	}
	
	public MoveFilter getMoveFilter() {
		if (moveFilter == null) {
			moveFilter = chessFactory.createMoveFilter(getPiecePlacement(), getKingCacheBoard(), getColorBoard(),  getPositionState(), getCapturer());
		}
		return moveFilter;
	}		
}
