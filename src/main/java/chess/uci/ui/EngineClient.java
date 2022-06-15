package chess.uci.ui;

import chess.uci.engine.Engine;
import chess.uci.protocol.stream.UCIInputStream;
import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.UCIRequest;
import chess.uci.protocol.UCIResponse;
import chess.uci.protocol.requests.*;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspId;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.RspUciOk;

import java.util.ArrayList;

public class EngineClient implements EngineClientResponseListener, EngineClientRequestSender {
    private final Engine engine;

    private EngineClientState currentState;

    private String engineName;
    private String engineAuthor;

    public EngineClient(Engine engine) {
        this.engine = engine;
        this.engine.setOutputStream(null);
    }

    @Override
    public void send_CmdUci() {
        currentState = new WaitRspUciOk();
        currentState.sendRequest(new CmdUci(), true);
    }

    @Override
    public void receive_uciOk(RspUciOk rspUciOk) {
        currentState.receive_uciOk(rspUciOk);
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
    public void receive_readyOk(RspReadyOk rspReadyOk) {
        currentState.receive_readyOk(rspReadyOk);
    }

    @Override
    public void send_CmdPosition() {
        currentState = new NoWaitRsp();
        currentState.sendRequest(new CmdPosition(CmdPosition.CmdType.STARTPOS, null, new ArrayList<String>()), false);
    }

    @Override
    public void send_CmdGo() {
        currentState = new WaitRspBestMove();
        currentState.sendRequest(new CmdGo(), true);
    }

    @Override
    public void receive_bestMove(RspBestMove rspBestMove) {
        currentState.receive_bestMove(rspBestMove);
    }

    @Override
    public void receive_id(RspId rspId) {
        currentState.receive_id(rspId);
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
    }

    protected UCIInputStream input;
    protected boolean keepProcessing;
    public void mainReadResponseLoop(){
        while (keepProcessing) {
            UCIMessage message = input.read();
            if(message != null && message instanceof UCIResponse){
                UCIResponse response = (UCIResponse) message;
                //response.execute(this);
            }
        }
    }

    public String getEngineName() {
        return engineName;
    }

    public String getEngineAuthor() {
        return engineAuthor;
    }


    private interface EngineClientState extends EngineClientResponseListener {
        void sendRequest(UCIRequest request, boolean waitResponse);
    }

    private abstract class RspAbstract implements EngineClientState {
        private boolean responseReceived = false;

        @Override
        public synchronized void sendRequest(UCIRequest request, boolean waitResponse) {
            //request.execute(engine);
            if(waitResponse) {
                try {
                    if(!responseReceived){
                        wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        protected synchronized void responseReceived(){
            responseReceived = true;
            notifyAll();
        }
    }

    private class WaitRspUciOk extends RspAbstract {
        @Override
        public void receive_uciOk(RspUciOk rspUciOk) {
            responseReceived();
        }

        @Override
        public void receive_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void receive_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void receive_id(RspId rspId) {
            if(RspId.RspIdType.NAME.equals(rspId.getIdType())){
                engineName = rspId.getText();
            }
            if(RspId.RspIdType.AUTHOR.equals(rspId.getIdType())){
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
            responseReceived();
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
        }

        @Override
        public void receive_id(RspId rspId) {

        }
    }
}
