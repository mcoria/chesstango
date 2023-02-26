package net.chesstango.uci.arena;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.GameDebugEncoder;
import net.chesstango.board.representations.PGNEncoder;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Match {
    private final EngineController engine1;
    private final EngineController engine2;
    private final int depth;

    private EngineController white;
    private EngineController black;
    private String fen;
    private Game game;

    private boolean debugEnabled;


    public Match(EngineController engine1, EngineController engine2, int depth) {
        this.engine1 = engine1;
        this.engine2 = engine2;
        this.depth = depth;
    }

    public List<MathResult> play(List<String> fenList) {
        List<MathResult> result = new ArrayList<>();

        fenList.stream().forEach(fen -> {
            result.addAll(play(fen));
        });

        return result;
    }

    public List<MathResult> play(String fen) {
        List<MathResult> result = new ArrayList<>();

        setFen(fen);

        setChairs(engine1, engine2);

        result.add(compete());

        setChairs(engine2, engine1);

        result.add(compete());

        return result;
    }

    protected MathResult compete() {
        this.game = FENDecoder.loadGame(fen);
        this.game.detectRepetitions(true);

        final List<String> executedMovesStr = new ArrayList<>();

        EngineController currentTurn;

        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            currentTurn = white;
        } else {
            currentTurn = black;
        }

        startNewGame();
        while (game.getStatus().isInProgress()) {
            String moveStr = calculateBestMove(currentTurn, fen, executedMovesStr);

            Move move = decodeMove(fen, game, moveStr, white, black);

            game.executeMove(move);

            executedMovesStr.add(moveStr);

            currentTurn = (currentTurn == white ? black : white);
        }

        return createResult();
    }

    private MathResult createResult() {
        int matchPoints = evaluateByMaterial(game);

        if (GameStatus.DRAW_BY_FOLD_REPETITION.equals(game.getStatus())) {
            System.out.println("DRAW (por repeticion)");

        } else if (GameStatus.DRAW_BY_FIFTY_RULE.equals(game.getStatus())) {
            System.out.println("DRAW (por fiftyMoveRule)");

        } else if (GameStatus.DRAW.equals(game.getStatus())) {
            System.out.println("DRAW");

        } else if (GameStatus.MATE.equals(game.getStatus())) {
            if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
                System.out.println("BLACK WON " + black.getEngineName());
                matchPoints = GameEvaluator.BLACK_WON;

            } else if (Color.BLACK.equals(game.getChessPosition().getCurrentTurn())) {
                System.out.println("WHITE WON " + white.getEngineName());
                matchPoints = GameEvaluator.WHITE_WON;

            }
        } else {
            printDebug();
            throw new RuntimeException("Inconsistent game status");
        }

        if (debugEnabled) {
            printDebug();
        }

        return new MathResult(game, white, black, matchPoints);
    }

    public Match setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

    protected void setFen(String fen) {
        this.fen = fen;
    }

    protected void setChairs(EngineController engine1, EngineController engine2) {
        if (engine1 != this.engine1 && engine1 != this.engine2 || engine2 != this.engine1 && engine2 != this.engine2) {
            throw new RuntimeException("Invalid opponents");
        }
        this.white = engine1;
        this.black = engine2;
    }

    private void startNewGame() {
        engine1.startNewGame();
        engine2.startNewGame();
    }

    private String calculateBestMove(EngineController currentTurn, String fen, List<String> moves) {
        if (FENDecoder.INITIAL_FEN.equals(fen)) {
            currentTurn.send_CmdPosition(new CmdPosition(moves));
        } else {
            currentTurn.send_CmdPosition(new CmdPosition(fen, moves));
        }

        RspBestMove bestMove = currentTurn.send_CmdGo(new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(depth));

        return bestMove.getBestMove();
    }

    private Move decodeMove(String fen, Game game, String bestMove, EngineController white, EngineController black) {
        UCIEncoder uciEncoder = new UCIEncoder();
        for (Move move : game.getPossibleMoves()) {
            String encodedMoveStr = uciEncoder.encode(move);
            if (encodedMoveStr.equals(bestMove)) {
                return move;
            }
        }

        printDebug();

        throw new RuntimeException("No move found " + bestMove);
    }

    private void printDebug() {
        System.out.println(game.toString());

        System.out.println();

        printPGN();

        System.out.println();

        printMoveExecution();

        System.out.println("--------------------------------------------------------------------------------");
    }

    private void printPGN() {
        PGNEncoder encoder = new PGNEncoder();
        PGNEncoder.PGNHeader pgnHeader = new PGNEncoder.PGNHeader();

        pgnHeader.setEvent("Computer chess game");
        pgnHeader.setWhite(white.getEngineName());
        pgnHeader.setBlack(black.getEngineName());
        pgnHeader.setFen(fen);

        System.out.println(encoder.encode(pgnHeader, game));
    }

    private void printMoveExecution() {
        GameDebugEncoder encoder = new GameDebugEncoder();

        System.out.println(encoder.encode(fen, game));
    }

    static public int evaluateByMaterial(Game game) {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getPiece();
            evaluation += getPieceValue(piece);
        }
        return evaluation;
    }

    static public int getPieceValue(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> 1;
            case PAWN_BLACK -> -1;
            case KNIGHT_WHITE -> 3;
            case KNIGHT_BLACK -> -3;
            case BISHOP_WHITE -> 3;
            case BISHOP_BLACK -> -3;
            case ROOK_WHITE -> 5;
            case ROOK_BLACK -> -5;
            case QUEEN_WHITE -> 9;
            case QUEEN_BLACK -> -9;
            case KING_WHITE -> 10;
            case KING_BLACK -> -10;
        };
    }
}
