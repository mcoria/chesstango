package net.chesstango.search.smart.alphabeta.pv.groupsorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.sorters.GroupSorter;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationGroup implements SearchByCycleListener, SearchByDepthListener, GroupSorter {

    private final PrincipalVariation[] principalVariations;

    @Setter
    @Getter
    private GroupSorter next;

    @Setter
    private List<PrincipalVariation> lastPrincipalVariations;

    @Setter
    private Game game;

    private int currentPly;

    public PrincipalVariationGroup() {
        principalVariations = new PrincipalVariation[40];
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        lastPrincipalVariations = null;
        for (int i = 0; i < 40; i++) {
            principalVariations[i] = null;
        }
    }

    @Override
    public void beforeSearchByDepth() {
        if (lastPrincipalVariations != null) {
            int i = 0;
            for (PrincipalVariation pv : lastPrincipalVariations) {
                principalVariations[i] = pv;
                i++;
            }
        }
    }

    @Override
    public void beforeSort(int currentPly) {
        this.currentPly = currentPly;
        if (principalVariations[currentPly] != null) {
            long hash = game.getPosition().getZobristHash();
            PrincipalVariation principalVariation = lastPrincipalVariations.get(currentPly);
            if (principalVariation.hash() != hash) {
                throw new RuntimeException("Principal variation hash mismatch");
            }
        }
    }

    @Override
    public void afterSort() {
        if (principalVariations[currentPly] != null) {
            principalVariations[currentPly] = null;
        }
    }

    @Override
    public boolean offer(Move move) {
        boolean result = false;
        if (principalVariations[currentPly] != null) {
            result =  move.equals(principalVariations[currentPly].move());
        }
        return !result ? next.offer(move) : result;
    }

    @Override
    public void collect(List<Move> moves) {
        if (principalVariations[currentPly] != null) {
            moves.add(principalVariations[currentPly].move());
        }
    }
}
