package net.chesstango.uci.arena.imp;

import net.chesstango.uci.arena.EngineController;
import net.chesstango.uci.engine.Engine;
import net.chesstango.uci.protocol.UCIGui;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.UCIRequest;
import net.chesstango.uci.protocol.UCIResponse;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;
import net.chesstango.uci.protocol.stream.UCIOutputStream;

import java.io.IOException;

/**
 * @author Mauricio Coria
 */
public class EngineControllerImp implements UCIOutputStream, EngineController {
    private final Engine engine;

    private final UCIGui messageExecutor;

    private EngineClientState currentState;

    private String engineName;
    private String engineAuthor;

    public EngineControllerImp(Engine engine) {
        this.engine = engine;
        this.engine.setResponseOutputStream(this);
        this.messageExecutor = new UCIGui() {
            @Override
            public void receive_uciOk(RspUciOk rspUciOk) {
                currentState.receive_uciOk(rspUciOk);
            }

            @Override
            public void receive_readyOk(RspReadyOk rspReadyOk) {
                currentState.receive_readyOk(rspReadyOk);
            }

            @Override
            public void receive_bestMove(RspBestMove rspBestMove) {
                currentState.receive_bestMove(rspBestMove);
            }

            @Override
            public void receive_id(RspId rspId) {
                currentState.receive_id(rspId);
            }
        };
    }

    @Override
    public void send_CmdUci() {
        engine.open();
        currentState = new WaitRspUciOk();
        currentState.sendRequest(new CmdUci(), true);
    }

    @Override
    public void send_CmdIsReady() {
        currentState = new WaitRspReadyOk();
        currentState.sendRequest(new CmdIsReady(), true);
    }

    @Override
    public void send_CmdUciNewGame() {
        currentState = new NoWaitRsp();
        currentState.sendRequest(new CmdUciNewGame(), false);
    }

    @Override
    public void send_CmdPosition(CmdPosition cmdPosition) {
        currentState = new NoWaitRsp();
        currentState.sendRequest(cmdPosition, false);
    }

    @Override
    public RspBestMove send_CmdGo(CmdGo cmdGo) {
        currentState = new WaitRspBestMove();
        currentState.sendRequest(cmdGo, true);
        return (RspBestMove) currentState.getResponse();
    }

    @Override
    public void send_CmdStop() {
        currentState = new NoWaitRsp();
        currentState.sendRequest(new CmdStop(), false);
    }

    @Override
    public void send_CmdQuit() {
        currentState = new NoWaitRsp();
        currentState.sendRequest(new CmdQuit(), false);
        engine.close();
    }

    @Override
    public String getEngineName() {
        return engineName;
    }

    @Override
    public String getEngineAuthor() {
        return engineAuthor;
    }

    @Override
    public void accept(UCIMessage message) {

        if(message instanceof UCIResponse) {
            ((UCIResponse) message).execute(messageExecutor);
        }
    }

    @Override
    public void close() throws IOException {
    }


    private interface EngineClientState {
        void receive_uciOk(RspUciOk rspUciOk);

        void receive_readyOk(RspReadyOk rspReadyOk);

        void receive_bestMove(RspBestMove rspBestMove);

        void receive_id(RspId rspId);

        void sendRequest(UCIRequest request, boolean waitResponse);

        UCIResponse getResponse();
    }

    private abstract class RspAbstract implements EngineClientState {

        private UCIResponse response;

        @Override
        public synchronized void sendRequest(UCIRequest request, boolean waitResponse) {
            engine.accept(request);
            if (waitResponse) {
                try {
                    if (response == null) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        protected synchronized void responseReceived(UCIResponse response) {
            this.response = response;
            notifyAll();
        }

        @Override
        public UCIResponse getResponse() {
            return response;
        }
    }

    private class WaitRspUciOk extends RspAbstract {
        @Override
        public void receive_uciOk(RspUciOk rspUciOk) {
            responseReceived(rspUciOk);
        }

        @Override
        public void receive_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void receive_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void receive_id(RspId rspId) {
            if (RspId.RspIdType.NAME.equals(rspId.getIdType())) {
                engineName = rspId.getText();
            }
            if (RspId.RspIdType.AUTHOR.equals(rspId.getIdType())) {
                engineAuthor = rspId.getText();
            }
        }

    }

    private class WaitRspReadyOk extends RspAbstract {
        @Override
        public void receive_uciOk(RspUciOk rspUciOk) {
        }

        @Override
        public void receive_readyOk(RspReadyOk rspReadyOk) {
            responseReceived(rspReadyOk);
        }

        @Override
        public void receive_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void receive_id(RspId rspId) {

        }
    }

    private class NoWaitRsp extends RspAbstract {
        @Override
        public void receive_uciOk(RspUciOk rspUciOk) {
        }

        @Override
        public void receive_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void receive_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void receive_id(RspId rspId) {
        }
    }


    private class WaitRspBestMove extends RspAbstract {
        @Override
        public void receive_uciOk(RspUciOk rspUciOk) {
        }

        @Override
        public void receive_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void receive_bestMove(RspBestMove rspBestMove) {
            responseReceived(rspBestMove);
        }

        @Override
        public void receive_id(RspId rspId) {
        }
    }
}
