package net.chesstango.board.factory;

import net.chesstango.board.Game;
import net.chesstango.board.GameState;
import net.chesstango.board.analyzer.CheckAndPinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.imp.LegalMoveGeneratorImp;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorImp;
import net.chesstango.board.position.*;
import net.chesstango.board.position.imp.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 *
 */
public class ChessInjector {

    private final ChessFactory chessFactory;

    private SquareBoard squareBoard = null;

    private PositionState positionState = null;

    private BitBoard bitBoard = null;

    private MoveCacheBoardImp moveCache = null;

    private KingSquareImp kingCacheBoard = null;

    private ZobristHash zobristHash  = null;

    private ChessPosition chessPosition = null;

    private MoveGenerator moveGenerator = null;

    private MoveGeneratorImp moveGeneratorImp = null;

    private PositionAnalyzer positionAnalyzer = null;

    private CheckAndPinnedAnalyzer checkAndPinnedAnalyzer;

    private LegalMoveGenerator defaultMoveCalculator = null;

    private LegalMoveGenerator noCheckLegalMoveGenerator = null;

    private LegalMoveGeneratorImp legalMoveGenerator = null;

    private FullScanSquareCaptured fullScanSquareCapturer = null;

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

            chessPositionImp.setColorBoard(getBitBoard());

            chessPositionImp.setMoveCache(getMoveCacheBoard());

            chessPositionImp.setZobristHash(getZobristHash());

            chessPosition = chessPositionImp;
        }
        return chessPosition;
    }

    public SquareBoard getPiecePlacement() {
        if (squareBoard == null) {
            squareBoard = chessFactory.createPiecePlacement();
        }
        return squareBoard;
    }

    public PositionState getPositionState() {
        if (positionState == null) {
            positionState = chessFactory.createPositionState();
        }
        return positionState;
    }

    protected KingSquareImp getKingCacheBoard() {
        if (kingCacheBoard == null) {
            kingCacheBoard = chessFactory.createKingCacheBoard();
        }
        return kingCacheBoard;
    }

    protected BitBoard getBitBoard() {
        if (bitBoard == null) {
            bitBoard = chessFactory.createColorBoard();
        }
        return bitBoard;
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
            //moveGenerator = getMoveGeneratorImp();
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

    protected MoveGenerator getMoveGeneratorImp() {
        if (moveGeneratorImp == null) {
            moveGeneratorImp = chessFactory.createMoveGenerator();
            moveGeneratorImp.setPiecePlacement(getPiecePlacement());
            moveGeneratorImp.setBoardState(getPositionState());
            moveGeneratorImp.setColorBoard(getBitBoard());
            moveGeneratorImp.setKingSquare(getKingCacheBoard());
        }
        return moveGeneratorImp;
    }

    private MoveFilter getCheckMoveFilter() {
        if (checkMoveFilter == null) {
            checkMoveFilter = chessFactory.createCheckMoveFilter(getPiecePlacement(), getKingCacheBoard(), getBitBoard(), getPositionState());
        }
        return checkMoveFilter;
    }


	private MoveFilter getNoCheckMoveFilter() {
        if (noCheckMoveFilter == null) {
            noCheckMoveFilter = chessFactory.createNoCheckMoveFilter(getPiecePlacement(), getKingCacheBoard(), getBitBoard(), getPositionState());
        }
        return noCheckMoveFilter;
    }

}
