package net.chesstango.search.smart.pv.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.pv.model.PVCalculator;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.pv.filters.PropagatePV;
import net.chesstango.search.smart.pv.model.PVWalkerFromTT;
import net.chesstango.search.smart.pv.model.PVTable;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTrianglePVVisitor implements Visitor {

    /**
     * Se utiliza para el calculo de PV
     */
    private final PVTable trianglePV;

    public LinkTrianglePVVisitor(PVTable trianglePV) {
        this.trianglePV = trianglePV;
    }

    @Override
    public void visit(PVCalculator setTrianglePV) {
        setTrianglePV.setTrianglePV(trianglePV);
    }

    @Override
    public void visit(ExtendPV extendPV) {
        extendPV.setTrianglePV(trianglePV);
    }

    @Override
    public void visit(PropagatePV propagatePV) {
        propagatePV.setTrianglePV(trianglePV);
    }

    @Override
    public void visit(DebugFilter debugFilter) {
        debugFilter.setTrianglePV(trianglePV);
    }

    @Override
    public void visit(PVWalkerFromTT pvWalkerFromTT) {
        pvWalkerFromTT.setTrianglePV(trianglePV);
    }
}
