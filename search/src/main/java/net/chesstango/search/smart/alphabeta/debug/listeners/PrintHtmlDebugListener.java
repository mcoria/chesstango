package net.chesstango.search.smart.alphabeta.debug.listeners;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByWindowsListener;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;
import net.chesstango.search.smart.alphabeta.debug.SearchTracker;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationEval;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PrintHtmlDebugListener implements Acceptor, SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {
    private final boolean withAspirationWindows;
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault());
    private final HexFormat hexFormat = HexFormat.of().withUpperCase();
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private PrintStream debugOut;

    @Setter
    private int depth;

    @Setter
    private SearchTracker searchTracker;

    @Setter
    private DebugNodeTrap debugNodeTrap;

    private List<String> debugErrorMessages;


    public PrintHtmlDebugListener(boolean withAspirationWindows) {
        this.withAspirationWindows = withAspirationWindows;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        try {
            fos = new FileOutputStream(String.format("DebugSearchTree-%s.html", dtFormatter.format(Instant.now())));
            bos = new BufferedOutputStream(fos);
            debugOut = new PrintStream(bos);
            printHeader();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterSearch() {
        try {
            printTail();
            debugOut.flush();
            debugOut.close();
            bos.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeSearchByDepth() {
        debugOut.printf("""
                <li>
                <span class="caret">Depth %d</span>
                <ul class="nested">
                """, depth);
    }

    @Override
    public void afterSearchByDepth() {
        debugOut.println("""
                </ul>
                </li>
                """);
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        debugOut.printf("""
                <li>
                <span class="caret">WIN alpha=%d beta=%d cycle=%d</span>
                <ul class="nested">
                """, alphaBound, betaBound, searchByWindowsCycle);
    }

    @Override
    public void afterSearchByWindows(boolean searchByWindowsFinished) {
        dumpSearchTracker();
        debugOut.println("""
                </ul>
                </li>
                """);
    }

    public void searchByDepthCompleted(SearchResultByDepth result) {
        /*
        if (!withAspirationWindows) {
            dumpSearchTracker();
        }

        debugOut.print("Search by depth completed\n");
        debugOut.printf("bestMove=%s; evaluation=%d; ", simpleMoveEncoder.encode(result.getBestMove()), result.getBestEvaluation());
        debugOut.printf("depth %d seldepth %d pv %s\n\n", result.getDepth(), result.getDepth(), simpleMoveEncoder.encode(result.getPrincipalVariation().stream().map(PrincipalVariation::move).toList()));
         */
    }

    private void dumpSearchTracker() {
        debugErrorMessages = new LinkedList<>();

        DebugNode rootNode = searchTracker.getRootNode();

        dumpNode(rootNode);

        //showNodePVTranspositionAccess(rootNode);

        //debugErrorMessages.forEach(debugOut::println);
        debugOut.flush();
    }

    private void dumpNode(DebugNode currentNode) {
        debugOut.print("""
                <li>
                <span class="caret">
                """);

        dumpNodeHeader(currentNode);

        debugOut.print("""
                </span>
                <ul class="nested">
                """);

        showNodeTranspositionAccess(currentNode);

        showNodeKillerMoves(currentNode);


        if (currentNode.getSortedMoves() != null) {
            debugOut.print("""
                <li>
                <span class="caret">
                """);
            debugOut.printf("Exploring: %s%n", currentNode.getSortedMoves());
            debugOut.print("""
                </span>
                <ul class="nested">
                """);
            dumpSorterOperations(currentNode);
            debugOut.println("""
                </ul>
                </li>
                """);
        }


        for (DebugNode childNode : currentNode.getChildNodes()) {
            dumpNode(childNode);
        }

        debugOut.println("""
                </ul>
                </li>
                """);
    }

    private void dumpNodeHeader(DebugNode currentNode) {
        if (currentNode.getPly() > 0) {
            String moveStr = simpleMoveEncoder.encode(currentNode.getSelectedMove());
            debugOut.printf("%s ", moveStr);
        }

        debugOut.printf("%s %s 0x%s alpha=%d beta=%d value=%d", currentNode.getFnString(), currentNode.getTopology(), hexFormat.formatHex(longToByte(currentNode.getZobristHash())), currentNode.getAlpha(), currentNode.getBeta(), currentNode.getValue());

        if (currentNode.getStandingPat() != null) {
            debugOut.printf(" SP=%d", currentNode.getStandingPat());
        }

        debugOut.printf(" %s", currentNode.getType());

        if (Objects.nonNull(currentNode.getParent()) &&
                currentNode.getParent().getChildNodes().stream()
                        .filter(otherNode -> otherNode.getZobristHash() == currentNode.getZobristHash())
                        .count() > 1) {
            debugOut.print(" DUPLICATED CHILD NODE");
            debugErrorMessages.add(String.format("DUPLICATED CHILD NODE %s", currentNode.getZobristHash()));
        }
    }

    private void showNodeTranspositionAccess(DebugNode currentNode) {
        currentNode.getEntryRead().forEach(readOp -> {
            TranspositionEntry entry = readOp.getEntry();
            int ttValue = entry.getValue();
            debugOut.print("<li>");
            debugOut.printf("Read  TT[ 0x%s %s draft=%d move=%s value=%d ]",
                    hexFormat.formatHex(longToByte(entry.getHash())),
                    entry.getBound(),
                    entry.getDraft(),
                    readOp.getMove(),
                    ttValue);
            if (currentNode.getZobristHash() != entry.getHash()) {
                debugOut.print(" WRONG TT_READ ENTRY");
                debugErrorMessages.add(String.format("WRONG TT_READ ENTRY 0x%s", hexFormat.formatHex(longToByte(currentNode.getZobristHash()))));
            }
            debugOut.println("</li>");
        });

        currentNode.getEntryWrite().forEach(writeOp -> {
            TranspositionEntry entry = writeOp.getEntry();
            int ttValue = entry.getValue();
            debugOut.print("<li>");
            debugOut.printf("Write TT[ 0x%s %s draft=%d move=%s value=%d ]",
                    hexFormat.formatHex(longToByte(entry.getHash())),
                    entry.getBound(),
                    entry.getDraft(),
                    writeOp.getMove(),
                    ttValue);

            if (currentNode.getZobristHash() != entry.getHash()) {
                String message = String.format(" WRONG TT_WRITE_HASH_REQUESTED ENTRY 0x%s", hexFormat.formatHex(longToByte(currentNode.getZobristHash())));
                debugOut.print(message);
                debugErrorMessages.add(message);
            }

            if (currentNode.getValue() != ttValue) {
                String message = String.format(" WRONG TT_WRITE_VALUE %s", currentNode.getValue());
                debugOut.print(message);
                debugErrorMessages.add(message);
            }
            debugOut.println("</li>");
        });
    }

    private void showNodeKillerMoves(DebugNode currentNode) {
        if (currentNode.getKillerMove() != null) {
            debugOut.print("<li>");
            debugOut.printf("Write KM %s%n", simpleMoveEncoder.encode(currentNode.getKillerMove()));
            debugOut.println("</li>");
        }
    }

    private void dumpSorterOperations(DebugNode currentNode) {
        List<String> sortedMoves = currentNode.getSortedMoves();

        List<DebugOperationTT> sortedReads = currentNode.getSorterReads();

        List<DebugOperationEval> evalCacheReads = currentNode.getEvalCacheReads();

        List<Move> sorterKms = currentNode.getSorterKm();

        debugOut.print("<li>");
        debugOut.printf("Sorter transpositions=%d cache=%d ply=%d%n",  sortedReads.size(), evalCacheReads.size(), currentNode.getSortedPly());
        debugOut.println("</li>");

        sortedMoves.forEach(moveStr -> {
            sortedReads
                    .stream()
                    .filter(debugNodeTT -> Objects.equals(moveStr, debugNodeTT.getMove()))
                    .forEach(ttOperation ->
                    {
                        TranspositionEntry entry = ttOperation.getEntry();
                        debugOut.print("<li>");
                        debugOut.printf("Sorter %s ReadTT[ %s 0x%s draft=%d move=? value=%d ]%n",
                                moveStr,
                                entry.getBound(),
                                hexFormat.formatHex(longToByte(entry.getHash())),
                                entry.getDraft(),
                                entry.getValue());
                        debugOut.println("</li>");
                    });


            evalCacheReads.stream()
                    .filter(debugOperationEval -> Objects.equals(moveStr, debugOperationEval.getMove()))
                    .forEach(debugOperationEval ->
                    {
                        debugOut.print("<li>");
                        debugOut.printf("Sorter %s CacheRead[ 0x%s value=%d ]%n",
                                moveStr,
                                hexFormat.formatHex(longToByte(debugOperationEval.getHashRequested())),
                                debugOperationEval.getEvaluation());
                        debugOut.println("</li>");
                    });

            sorterKms.stream()
                    .map(simpleMoveEncoder::encode)
                    .filter(kmStr -> Objects.equals(kmStr, moveStr))
                    .forEach(kmStr ->
                    {
                        debugOut.print("<li>");
                        debugOut.printf("Sorter %s KillerMove%n",
                                moveStr);
                        debugOut.println("</li>");
                    });
        });


        /**
         * NO ME QUEDA CLARO PARA QUE MOVIMIENTO LEEMOS EN ESTE CASO
         */
        sortedReads
                .stream()
                .filter(ttOperation -> "NO_MOVE".equals(ttOperation.getMove()))
                .forEach(ttOperation -> {
                    TranspositionEntry entry = ttOperation.getEntry();
                    int ttValue = entry.getValue();
                    debugOut.print("<li>");
                    debugOut.printf("Sorter NO_MOVE ReadTT[ %s 0x%s draft=%d move=? value=%d ]",
                            entry.getBound(),
                            hexFormat.formatHex(longToByte(entry.getHash())),
                            entry.getDraft(),
                            ttValue);
                    debugOut.println("</li>");
                });

    }

    private void showNodePVTranspositionAccess(DebugNode currentNode) {
        currentNode.getPvReads().forEach(readOp -> {
            TranspositionEntry entry = readOp.getEntry();
            debugOut.printf(" PV Read  TT[ 0x%s %s draft=%d move=0x%s value=%d ]\n",
                    hexFormat.formatHex(longToByte(entry.getHash())),
                    entry.getBound(),
                    entry.getDraft(),
                    hexFormat.toHexDigits(entry.getMove()),
                    entry.getValue());
        });
    }

    private byte[] longToByte(long lng) {
        return new byte[]{
                (byte) (lng >> 56),
                (byte) (lng >> 48),
                (byte) (lng >> 40),
                (byte) (lng >> 32),
                (byte) (lng >> 24),
                (byte) (lng >> 16),
                (byte) (lng >> 8),
                (byte) lng
        };
    }

    private void printHeader() {
        debugOut.print("""
                <!DOCTYPE html>
                <html>
                <head>
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <style>
                ul, #myUL {
                  list-style-type: none;
                }
                
                #myUL {
                  margin: 0;
                  padding: 0;
                }
                
                .caret {
                  cursor: pointer;
                  -webkit-user-select: none; /* Safari 3.1+ */
                  -moz-user-select: none; /* Firefox 2+ */
                  -ms-user-select: none; /* IE 10+ */
                  user-select: none;
                }
                
                .caret::before {
                  content: "\\25B6";
                  color: black;
                  display: inline-block;
                  margin-right: 6px;
                }
                
                .caret-down::before {
                  -ms-transform: rotate(90deg); /* IE 9 */
                  -webkit-transform: rotate(90deg); /* Safari */'
                  transform: rotate(90deg);
                }
                
                .nested {
                  display: none;
                }
                
                .active {
                  display: block;
                }
                </style>
                </head>
                <body>
                
                <h2>Tree View</h2>
                <p>A tree view represents a hierarchical view of information, where each item can have a number of subitems.</p>
                <p>Click on the arrow(s) to open or close the tree branches.</p>
                
                <ul id="myUL">
                """);
    }


    private void printTail() {
        debugOut.print("""
                </ul>
                
                <script>
                var toggler = document.getElementsByClassName("caret");
                var i;
                
                for (i = 0; i < toggler.length; i++) {
                  toggler[i].addEventListener("click", function() {
                    this.parentElement.querySelector(".nested").classList.toggle("active");
                    this.classList.toggle("caret-down");
                  });
                }
                </script>
                
                </body>
                </html>
                """);
    }
}
