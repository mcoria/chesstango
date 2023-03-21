/**
 *
 */
package net.chesstango.board.factory;

import net.chesstango.board.Game;
import net.chesstango.board.GameState;
import net.chesstango.board.analyzer.CheckAndPinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.imp.LegalMoveGeneratorImp;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCapturer;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorImp;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 *
 */
public class ChessInjector {

    private final ChessFactory chessFactory;

    private PiecePlacement piecePlacement = null;

    private PositionState positionState = null;

    private ColorBoard colorBoard = null;

    private MoveCacheBoard moveCache = null;

    private KingCacheBoard kingCacheBoard = null;

    private ZobristHash zobristHash  = null;

    private ChessPosition chessPosition = null;

    private MoveGenerator moveGenerator = null;

    private MoveGeneratorImp moveGeneratorImp = null;

    private PositionAnalyzer positionAnalyzer = null;

    private CheckAndPinnedAnalyzer checkAndPinnedAnalyzer;

    private LegalMoveGenerator defaultMoveCalculator = null;

    private LegalMoveGenerator noCheckLegalMoveGenerator = null;

    private LegalMoveGeneratorImp legalMoveGenerator = null;

    private FullScanSquareCapturer fullScanSquareCapturer = null;

    private MoveFilter checkMoveFilter;

    private MoveFilter noCheckMoveFilter;

    private GameState gameState = null;

    private Game game = null;

    private Map<Class, Object> objectMap = new HashMap<>();

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

            chessPositionImp.setZobristHash(getZobristHash());

            chessPosition = chessPositionImp;
        }
        return chessPosition;
    }

    public PiecePlacement getPiecePlacement() {
        if (piecePlacement == null) {
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

    protected KingCacheBoard getKingCacheBoard() {
        if (kingCacheBoard == null) {
            kingCacheBoard = chessFactory.createKingCacheBoard();
        }
        return kingCacheBoard;
    }

    protected ColorBoard getColorBoard() {
        if (colorBoard == null) {
            colorBoard = chessFactory.createColorBoard();
        }
        return colorBoard;
    }

    protected MoveCacheBoard getMoveCacheBoard() {
        if (moveCache == null) {
            moveCache = chessFactory.createMoveCacheBoard();
        }
        return moveCache;
    }

    protected ZobristHash getZobristHash() {
        if (zobristHash == null) {
            zobristHash = chessFactory.createZobristHash();
        }
        return zobristHash;
    }

    public Game getGame() {
        if (game == null) {
            game = chessFactory.createGame(getChessPosition(), getGameState(), getAnalyzer(), objectMap);
        }
        return game;
    }


    public GameState getGameState() {
        if (gameState == null) {
            gameState = chessFactory.createGameState();
        }
        return gameState;
    }

	public PositionAnalyzer getAnalyzer() {
        if (positionAnalyzer == null) {
            positionAnalyzer = chessFactory.createPositionAnalyzer();
            positionAnalyzer.setLegalMoveGenerator(getLegalMoveGenerator());
            positionAnalyzer.setGameState(getGameState());
            positionAnalyzer.setCheckAndPinnedAnalyzer(getCheckAndPinnedAnalyzer());
            positionAnalyzer.setPositionReader(getChessPosition());
        }
        return positionAnalyzer;
    }

    private CheckAndPinnedAnalyzer getCheckAndPinnedAnalyzer() {
        if (checkAndPinnedAnalyzer == null) {
            checkAndPinnedAnalyzer = chessFactory.createCheckAndPinnedAnalyzer(getChessPosition());
        }
        return checkAndPinnedAnalyzer;
    }


    private LegalMoveGenerator getLegalMoveGenerator() {
        if (this.legalMoveGenerator == null) {
            this.legalMoveGenerator = chessFactory.createLegalMoveGenerator();
            this.legalMoveGenerator.setCheckLegalMoveGenerator(getCheckLegalMoveGenerator());
            this.legalMoveGenerator.setNoCheckLegalMoveGenerator(getNoCheckLegalMoveGenerator());

        }
        return this.legalMoveGenerator;
    }

    public MoveGenerator getPseudoMoveGenerator() {
        if (moveGenerator == null) {
            moveGenerator = chessFactory.createMoveGeneratorWithCacheProxy(getMoveGeneratorImp(), getMoveCacheBoard());
            objectMap.put(MoveGenerator.class, moveGenerator);
        }
        return moveGenerator;
    }

    protected LegalMoveGenerator getCheckLegalMoveGenerator() {
        if (defaultMoveCalculator == null) {
            defaultMoveCalculator = chessFactory.createCheckLegalMoveGenerator(getChessPosition(), getPseudoMoveGenerator(), getCheckMoveFilter());
        }
        return this.defaultMoveCalculator;
    }

    protected LegalMoveGenerator getNoCheckLegalMoveGenerator() {
        if (noCheckLegalMoveGenerator == null) {
            noCheckLegalMoveGenerator = chessFactory.createNoCheckLegalMoveGenerator(getChessPosition(), getPseudoMoveGenerator(), getNoCheckMoveFilter());
        }
        return noCheckLegalMoveGenerator;
    }

    protected MoveGeneratorImp getMoveGeneratorImp() {
        if (moveGeneratorImp == null) {
            moveGeneratorImp = new MoveGeneratorImp();
            moveGeneratorImp.setPiecePlacement(getPiecePlacement());
            moveGeneratorImp.setBoardState(getPositionState());
            moveGeneratorImp.setColorBoard(getColorBoard());
            moveGeneratorImp.setKingCacheBoard(getKingCacheBoard());
        }
        return moveGeneratorImp;
    }

    private MoveFilter getCheckMoveFilter() {
        if (checkMoveFilter == null) {
            checkMoveFilter = chessFactory.createCheckMoveFilter(getPiecePlacement(), getKingCacheBoard(), getColorBoard(), getPositionState());
        }
        return checkMoveFilter;
    }


	private MoveFilter getNoCheckMoveFilter() {
        if (noCheckMoveFilter == null) {
            noCheckMoveFilter = chessFactory.createNoCheckMoveFilter(getPiecePlacement(), getKingCacheBoard(), getColorBoard(), getPositionState());
        }
        return noCheckMoveFilter;
    }

}
