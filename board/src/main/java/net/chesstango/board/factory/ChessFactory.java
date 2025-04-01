package net.chesstango.board.factory;

import net.chesstango.board.GameImp;
import net.chesstango.board.GameVisitorAcceptor;
import net.chesstango.board.analyzer.KingSafePositionsAnalyzer;
import net.chesstango.board.analyzer.PinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryBlack;
import net.chesstango.board.moves.factories.imp.MoveFactoryWhite;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;
import net.chesstango.board.moves.generators.legal.imp.LegalMoveGeneratorImp;
import net.chesstango.board.moves.generators.legal.imp.check.CheckLegalMoveFilter;
import net.chesstango.board.moves.generators.legal.imp.check.CheckLegalMoveGenerator;
import net.chesstango.board.moves.generators.legal.imp.nocheck.NoCheckLegalMoveFilter;
import net.chesstango.board.moves.generators.legal.imp.nocheck.NoCheckLegalMoveGenerator;
import net.chesstango.board.moves.generators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.imp.MoveGeneratorCache;
import net.chesstango.board.moves.generators.pseudo.imp.MoveGeneratorImp;
import net.chesstango.board.position.*;
import net.chesstango.board.position.imp.*;

/**
 * @author Mauricio Coria
 */
public class ChessFactory {

    public ChessPositionImp createChessPosition() {
        return new ChessPositionImp();
    }

    public LegalMoveGenerator createLegalMoveGenerator(LegalMoveGenerator checkLegalMoveGenerator, LegalMoveGenerator noCheckLegalMoveGenerator) {
        return new LegalMoveGeneratorImp(checkLegalMoveGenerator, noCheckLegalMoveGenerator);
    }

    public LegalMoveGenerator createCheckLegalMoveGenerator(ChessPositionReader positionReader,
                                                            MoveGenerator buildMoveGeneratorStrategy, LegalMoveFilter filter) {
        return new CheckLegalMoveGenerator(positionReader, buildMoveGeneratorStrategy, filter);
    }

    public LegalMoveGenerator createNoCheckLegalMoveGenerator(ChessPositionReader positionReader,
                                                              MoveGenerator buildMoveGeneratorStrategy, LegalMoveFilter filter) {
        return new NoCheckLegalMoveGenerator(positionReader, buildMoveGeneratorStrategy, filter);
    }

    public BitBoard createColorBoard() {
        return new BitBoardImp();
    }

    public KingSquare createKingCacheBoard() {
        return new KingSquareImp();
    }

    public MoveCacheBoard createMoveCacheBoard() {
        return new MoveCacheBoardImp();
    }

    public LegalMoveFilter createCheckMoveFilter(SquareBoard dummySquareBoard, KingSquare kingCacheBoard, BitBoard bitBoard,
                                                      PositionState positionState) {
        return new CheckLegalMoveFilter(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
    }

    public LegalMoveFilter createNoCheckMoveFilter(SquareBoard dummySquareBoard, KingSquare kingCacheBoard, BitBoard bitBoard,
                                                          PositionState positionState) {
        return new NoCheckLegalMoveFilter(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
    }

    public SquareBoard createPiecePlacement() {
        return new SquareBoardImp();
    }

    public PositionState createPositionState() {
        return new PositionStateImp();
    }

    public MoveGenerator createMoveGeneratorWithCacheProxy(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
        return new MoveGeneratorCache(moveGenerator, moveCacheBoard);
    }

    public MoveGeneratorImp createMoveGenerator(MoveFactory moveFactoryWhite, MoveFactory moveFactoryBlack) {
        return new MoveGeneratorImp(moveFactoryWhite, moveFactoryBlack);
    }

    public GameImp createGame(ChessPosition chessPosition, GameState gameState, GameVisitorAcceptor visitorAcceptor) {
        return new GameImp(chessPosition, gameState, visitorAcceptor);
    }

    public PositionAnalyzer createPositionAnalyzer() {
        return new PositionAnalyzer();
    }

    public GameState createGameState() {
        return new GameStateImp();
    }

    public PinnedAnalyzer createPinnedAnalyzer(ChessPosition chessPosition) {
        return new PinnedAnalyzer(chessPosition);
    }

    public KingSafePositionsAnalyzer createKingSafePositionsAnalyzer(ChessPositionReader positionReader) {
        return new KingSafePositionsAnalyzer(positionReader);
    }

    public ZobristHash createZobristHash() {
        return new ZobristHashImp();
    }

    public MoveFactory createMoveFactoryBlack(GameImp gameImp) {
        return new MoveFactoryBlack(gameImp);
    }

    public MoveFactory createMoveFactoryWhite(GameImp gameImp) {
        return new MoveFactoryWhite(gameImp);
    }
}
