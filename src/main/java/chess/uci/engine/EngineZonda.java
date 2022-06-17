/**
 *
 */
package chess.uci.engine;

import chess.ai.BestMoveFinder;
import chess.ai.imp.smart.SmartLoop;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.representations.fen.FENDecoder;
import chess.uci.protocol.UCIEncoder;
import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.UCIMessageExecutor;
import chess.uci.protocol.requests.*;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspId;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.RspUciOk;
import chess.uci.protocol.stream.UCIOutputStream;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * @author Mauricio Coria
 */
public class EngineZonda implements Engine, UCIMessageExecutor {
    private final ExecutorService executor;
    private final BestMoveFinder bestMoveFinder;
    private Game game;
    private ZondaState currentState;
    private UCIOutputStream responseOutputStream;

    public EngineZonda(ExecutorService executor) {
        this.executor = executor;
        this.bestMoveFinder = new SmartLoop();
        this.currentState = new Ready();
    }


    @Override
    public void do_uci(CmdUci cmdUci) {
        responseOutputStream.write(new RspId(RspId.RspIdType.NAME, "Zonda"));
        responseOutputStream.write(new RspId(RspId.RspIdType.AUTHOR, "Mauricio Coria"));
        responseOutputStream.write(new RspUciOk());
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        responseOutputStream.write(new RspReadyOk());
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        game = CmdPosition.CmdType.STARTPOS == cmdPosition.getType() ? FENDecoder.loadGame(FENDecoder.INITIAL_FEN) : FENDecoder.loadGame(cmdPosition.getFen());
        executeMoves(cmdPosition.getMoves());
        currentState = new WaitCmdGo();
    }

    @Override
    public void do_go(CmdGo cmdGo) {
        currentState.do_go(cmdGo);
    }

    @Override
    public void do_stop(CmdStop cmdStop) {
        currentState.do_stop();
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        currentState.do_stop();
        try {
            responseOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO: esta malo, que pasa si hay mas de un engine procesando.....
        executor.shutdown();
    }

    @Override
    public void receive_uciOk(RspUciOk rspUciOk) {
    }

    @Override
    public void receive_id(RspId rspId) {
    }

    @Override
    public void receive_readyOk(RspReadyOk rspReadyOk) {
    }

    @Override
    public void receive_bestMove(RspBestMove rspBestMove) {
    }

    @Override
    public void write(UCIMessage message) {
        message.execute(this);
    }

    @Override
    public void close() throws IOException {
        responseOutputStream.close();
    }

    public Game getGame() {
        return game;
    }

    public void setResponseOutputStream(UCIOutputStream output) {
        this.responseOutputStream = output;
    }


    private void executeMoves(List<String> moves) {
        if (moves != null && !moves.isEmpty()) {
            UCIEncoder uciEncoder = new UCIEncoder();
            for (String moveStr : moves) {
                boolean findMove = false;
                for (Move move : game.getPossibleMoves()) {
                    String encodedMoveStr = uciEncoder.encode(move);
                    if (encodedMoveStr.equals(moveStr)) {
                        game.executeMove(move);
                        findMove = true;
                        break;
                    }
                }
                if (!findMove) {
                    throw new RuntimeException("No move found " + moveStr);
                }
            }
        }
    }

    ZondaState getCurrentState() {
        return currentState;
    }


    interface ZondaState {

        void do_go(CmdGo cmdGo);

        void do_stop();
    }


    class Ready implements ZondaState {
        @Override
        public void do_go(CmdGo cmdGo) {
        }

        @Override
        public void do_stop() {
        }
    }


    class WaitCmdGo implements ZondaState {
        @Override
        public void do_go(CmdGo cmdGo) {
            FindingBestMove findingBestMove = new FindingBestMove();
            currentState = findingBestMove;
            executor.execute(findingBestMove::findBestMove);
        }

        @Override
        public void do_stop() {
        }
    }

    class FindingBestMove implements ZondaState {

        private UCIEncoder uciEncoder = new UCIEncoder();


        @Override
        public void do_go(CmdGo cmdGo) {
        }

        @Override
        public void do_stop() {
            bestMoveFinder.stopProcessing();
        }

        public void findBestMove() {
            Move selectedMove = bestMoveFinder.findBestMove(game);

            responseOutputStream.write(new RspBestMove(uciEncoder.encode(selectedMove)));

            currentState = new Ready();
        }
    }
}
