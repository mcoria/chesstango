package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.requests.go.CmdGoDepth;
import net.chesstango.uci.protocol.requests.go.CmdGoFast;
import net.chesstango.uci.protocol.requests.go.CmdGoInfinite;
import net.chesstango.uci.protocol.requests.go.CmdGoTime;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class UCIDecoder {

    /**
     * @param input
     * @return
     */
    public UCIMessage parseMessage(String input) {
        UCIMessage result = null;

        String[] words = input.split(" ");

        if (words.length > 0) {
            String command = words[0].toUpperCase();
            result = switch (command) {

                // ====================== REQUESTS
                case "UCI" -> new CmdUci();
                case "SETOPTION" -> parseSetOption(words);
                case "UCINEWGAME" -> new CmdUciNewGame();
                case "POSITION" -> parsePosition(words);
                case "QUIT" -> new CmdQuit();
                case "ISREADY" -> new CmdIsReady();
                case "GO" -> parseGo(words);
                case "STOP" -> new CmdStop();

                // ====================== RESPONSES
                case "READYOK" -> new RspReadyOk();
                case "UCIOK" -> new RspUciOk();
                case "BESTMOVE" -> parseBestMove(words);
                case "ID" -> parseId(words);

                // ====================== UNKNOWN
                default -> new UCIMessageUnknown(input);
            };
        }
        return result;
    }

    private UCIMessage parseId(String[] words) {
        UCIMessage result = null;
        if (words.length > 2) {
            String typeStr = words[1].toUpperCase();
            RspId.RspIdType type = null;
            if (RspId.RspIdType.AUTHOR.toString().equalsIgnoreCase(typeStr)) {
                type = RspId.RspIdType.AUTHOR;
            } else if (RspId.RspIdType.NAME.toString().equalsIgnoreCase(typeStr)) {
                type = RspId.RspIdType.NAME;
            }

            if (type != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < words.length; i++) {
                    sb.append(words[i]);
                    if (i < words.length - 1) {
                        sb.append(" ");
                    }
                }

                result = new RspId(type, sb.toString());
            }
        }
        return result == null ? new UCIMessageUnknown(words.toString()) : result;
    }

    private UCIMessage parseBestMove(String[] words) {
        String bestMove = words[1];
        String ponderMove = null;
        if (words.length == 4) {
            if ("ponder".equalsIgnoreCase(words[2])) {
                ponderMove = words[3];
            }
        }
        return new RspBestMove(bestMove, ponderMove);
    }

    private UCIMessage parseSetOption(String[] words) {
        return new CmdSetOption(words[2], words[4]);
    }


    private UCIMessage parseGo(String[] words) {
        CmdGo result = null;
        if (words.length > 1) {
            String goType = words[1].toUpperCase();
            switch (goType) {
                case "INFINITE":
                    result = new CmdGoInfinite();
                    break;
                case "DEPTH":
                    result = parseGoDepth(words);
                    break;
                case "MOVETIME":
                    result = parseGoMoveTime(words);
                    break;
                case "WTIME":
                case "BTIME":
                case "WINC":
                case "BINC":
                    result = parseGoMoveByClock(words);
                    break;
                default:
                    break;
            }
        } else {
            result = new CmdGoInfinite();
        }
        return result == null ? new UCIMessageUnknown(words.toString()) : result;
    }

    private CmdGo parseGoDepth(String[] words) {
        CmdGoDepth result = new CmdGoDepth();

        String depth = words[2].toUpperCase();
        int depthInt = Integer.parseInt(depth);

        result.setDepth(depthInt);

        return result;
    }

    private CmdGo parseGoMoveTime(String[] words) {
        CmdGoTime result = new CmdGoTime();

        String timeOut = words[2].toUpperCase();
        int timeOutInt = Integer.parseInt(timeOut);

        result.setTimeOut(timeOutInt);

        return result;
    }

    private CmdGo parseGoMoveByClock(String[] words) {
        CmdGoFast result = new CmdGoFast();

        for (int i = 1; i < words.length; i += 2) {
            switch (words[i]) {
                case "wtime":
                    result.setWTime(Integer.parseInt(words[i + 1]));
                    break;
                case "winc":
                    result.setWInc(Integer.parseInt(words[i + 1]));
                    break;
                case "btime":
                    result.setBTime(Integer.parseInt(words[i + 1]));
                    break;
                case "binc":
                    result.setBInc(Integer.parseInt(words[i + 1]));
                    break;
            }
        }


        return result;
    }


    private UCIMessage parsePosition(String[] words) {
        UCIMessage result = null;
        if (words.length > 1) {
            String positionType = words[1].toUpperCase();
            switch (positionType) {
                case "FEN":
                    result = parsePositionFEN(words);
                    break;
                case "STARTPOS":
                    result = parsePositionSTARTPOS(words);
                    break;
                default:
                    break;
            }
        }
        return result == null ? new UCIMessageUnknown(words.toString()) : result;
    }

    private UCIMessage parsePositionSTARTPOS(String[] words) {
        List<String> moves = new ArrayList<String>();
        if (words.length > 2) {
            String movesword = words[2].toUpperCase();
            if ("MOVES".equals(movesword) && words.length > 3) {
                for (int i = 3; i < words.length; i++) {
                    moves.add(words[i]);
                }
            }

        }
        return new CmdPosition(moves);
    }


    private UCIMessage parsePositionFEN(String[] words) {
        boolean readingFen = true;
        String fenString = "";
        List<String> moves = new ArrayList<String>();
        for (int i = 2; i < words.length; i++) {
            if (readingFen) {
                if ("MOVES".equalsIgnoreCase(words[i])) {
                    readingFen = false;
                } else {
                    if (fenString.length() == 0) {
                        fenString = fenString.concat(words[i]);
                    } else {
                        fenString = fenString.concat(" " + words[i]);
                    }
                }
            } else {
                moves.add(words[i]);
            }
        }

        return new CmdPosition(fenString, moves);
    }

}
