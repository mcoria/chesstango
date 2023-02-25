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
    private EngineController engine1;
    private EngineController engine2;
    private final int depth;

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

        result.add(compete(fen));
        switchChairs();
        result.add(compete(fen));

        return result;
    }

    public MathResult compete(String fen) {
        startNewGame();

        Game game = FENDecoder.loadGame(fen);
        game.detectRepetitions(true);

        List<String> executedMovesStr = new ArrayList<>();
        EngineController currentTurn = engine1;

        while (game.getStatus().isInProgress()) {
            String moveStr = calculateBestMove(currentTurn, fen, executedMovesStr);

            Move move = decodeMove(fen, game, moveStr);

            game.executeMove(move);

            executedMovesStr.add(moveStr);

            currentTurn = (currentTurn == engine1 ? engine2 : engine1);
        }

        MathResult result = null;
        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            EngineController white = currentTurn == engine1 ? engine1 : engine2;
            EngineController black = white == engine1 ? engine2 : engine1;
            result = new MathResult(game, white, black);

        } else {
            EngineController black = currentTurn == engine1 ? engine1 : engine2;
            EngineController white = black == engine1 ? engine2 : engine1;
            result = new MathResult(game, white, black);
        }

        int matchPoints = evaluateByMaterial(game);

        if (GameStatus.DRAW_BY_FOLD_REPETITION.equals(game.getStatus())) {
            System.out.println("DRAW (por repeticion)");
            result.setPoints(matchPoints);

        } else if (GameStatus.DRAW_BY_FIFTY_RULE.equals(game.getStatus())) {
            System.out.println("DRAW (por fiftyMoveRule)");
            result.setPoints(matchPoints);

        } else if (GameStatus.DRAW.equals(game.getStatus())) {
            System.out.println("DRAW");
            result.setPoints(matchPoints);

        } else if (GameStatus.MATE.equals(game.getStatus())) {
            if(Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
                System.out.println("MATE WHITE " + result.getEngineWhite().getEngineName());
                result.setPoints(GameEvaluator.WHITE_LOST);

            } else if (Color.BLACK.equals(game.getChessPosition().getCurrentTurn())) {
                System.out.println("MATE BLACK " + result.getEngineBlack().getEngineName());
                result.setPoints(GameEvaluator.BLACK_LOST);

            }
        } else {
            printDebug(fen, game);
            throw new RuntimeException("Inconsistent game status");
        }


        //printDebug(fen, game);

        return result;
    }


    protected void startNewGame() {
        engine1.send_CmdUciNewGame();
        engine1.send_CmdIsReady();

        engine2.send_CmdUciNewGame();
        engine2.send_CmdIsReady();
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

    private Move decodeMove(String fen, Game game, String bestMove) {
        UCIEncoder uciEncoder = new UCIEncoder();
        for (Move move : game.getPossibleMoves()) {
            String encodedMoveStr = uciEncoder.encode(move);
            if (encodedMoveStr.equals(bestMove)) {
                return move;
            }
        }

        printDebug(fen, game);

        throw new RuntimeException("No move found " + bestMove);
    }

    private void printDebug(String fen, Game game) {
        System.out.println(game.toString());

        System.out.println();

        printPGN(fen, game);

        System.out.println();

        printMoveExecution(fen, game);

        System.out.println("--------------------------------------------------------------------------------");
    }

    private void printPGN(String fen, Game game) {
        PGNEncoder encoder = new PGNEncoder();
        PGNEncoder.PGNHeader pgnHeader = new PGNEncoder.PGNHeader();

        pgnHeader.setEvent("Computer chess game");
        pgnHeader.setWhite(engine1.getEngineName());
        pgnHeader.setBlack(engine2.getEngineName());
        pgnHeader.setFen(fen);

        System.out.println(encoder.encode(pgnHeader, game));
    }

    private void printMoveExecution(String fen, Game game) {
        GameDebugEncoder encoder = new GameDebugEncoder();

        System.out.println(encoder.encode(fen, game));
    }


    private void switchChairs() {
        EngineController tmpController = engine1;
        engine1 = engine2;
        engine2 = tmpController;
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
        return switch (piece){
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
