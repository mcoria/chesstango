package net.chesstango.board.internal.factory;

import net.chesstango.board.analyzer.KingSafePositionsAnalyzer;
import net.chesstango.board.analyzer.PinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.internal.GameImp;
import net.chesstango.board.internal.moves.factories.MoveFactoryBlack;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.internal.moves.generators.legal.LegalMoveGeneratorImp;
import net.chesstango.board.internal.moves.generators.legal.check.CheckLegalMoveFilter;
import net.chesstango.board.internal.moves.generators.legal.check.CheckLegalMoveGenerator;
import net.chesstango.board.internal.moves.generators.legal.nocheck.NoCheckLegalMoveFilter;
import net.chesstango.board.internal.moves.generators.legal.nocheck.NoCheckLegalMoveGenerator;
import net.chesstango.board.internal.moves.generators.pseudo.MoveGeneratorCache;
import net.chesstango.board.internal.moves.generators.pseudo.MoveGeneratorImp;
import net.chesstango.board.internal.position.*;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public class ChessFactory {

    public PositionImp createChessPosition() {
        return new PositionImp();
    }

    public LegalMoveGenerator createLegalMoveGenerator(LegalMoveGenerator checkLegalMoveGenerator, LegalMoveGenerator noCheckLegalMoveGenerator) {
        return new LegalMoveGeneratorImp(checkLegalMoveGenerator, noCheckLegalMoveGenerator);
    }

    public LegalMoveGenerator createCheckLegalMoveGenerator(PositionReader positionReader,
                                                            MoveGenerator buildMoveGeneratorStrategy, LegalMoveFilter filter) {
        return new CheckLegalMoveGenerator(positionReader, buildMoveGeneratorStrategy, filter);
    }

    public LegalMoveGenerator createNoCheckLegalMoveGenerator(PositionReader positionReader,
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

    public SquareBoard createSquareBoard() {
        return new SquareBoardImp();
    }

    public PositionState createPositionState() {
        return new PositionStateImp();
    }

    public ZobristHash createZobristHash() {
        return new ZobristHashImp();
    }

    public LegalMoveFilter createCheckMoveFilter(SquareBoard dummySquareBoard, KingSquare kingCacheBoard, BitBoard bitBoard,
                                                 PositionState positionState) {
        return new CheckLegalMoveFilter(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
    }

    public LegalMoveFilter createNoCheckMoveFilter(SquareBoard dummySquareBoard, KingSquare kingCacheBoard, BitBoard bitBoard,
                                                   PositionState positionState) {
        return new NoCheckLegalMoveFilter(dummySquareBoard, kingCacheBoard, bitBoard, positionState);
    }

    public MoveGenerator createMoveGeneratorWithCacheProxy(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
        return new MoveGeneratorCache(moveGenerator, moveCacheBoard);
    }

    public MoveGeneratorImp createMoveGenerator() {
        return new MoveGeneratorImp();
    }

    public GameImp createGame(Position position, GameState gameState, GameHistory gameHistory) {
        return new GameImp(position, gameState, gameHistory);
    }

    public PositionAnalyzer createPositionAnalyzer() {
        return new PositionAnalyzer();
    }

    public GameState createGameState() {
        return new GameStateImp();
    }

    public PinnedAnalyzer createPinnedAnalyzer(Position position) {
        return new PinnedAnalyzer(position);
    }

    public KingSafePositionsAnalyzer createKingSafePositionsAnalyzer(PositionReader positionReader) {
        return new KingSafePositionsAnalyzer(positionReader);
    }

    public MoveFactory createMoveFactoryBlack(GameImp gameImp) {
        return new MoveFactoryBlack(gameImp);
    }

    public MoveFactory createMoveFactoryWhite(GameImp gameImp) {
        return new MoveFactoryWhite(gameImp);
    }

    public GameHistory createCareTaker() {
        return new GameHistoryImp();
    }
}
