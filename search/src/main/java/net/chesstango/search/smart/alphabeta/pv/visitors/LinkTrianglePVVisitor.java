package net.chesstango.search.smart.alphabeta.pv.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.pv.model.PVCalculatorTriangular;
import net.chesstango.search.smart.alphabeta.pv.filters.CalculatePV;
import net.chesstango.search.smart.alphabeta.pv.filters.ExtendPV;
import net.chesstango.search.smart.alphabeta.pv.filters.PropagatePV;
import net.chesstango.search.smart.alphabeta.pv.model.PVWalkerFromTT;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTrianglePVVisitor implements Visitor {

    /**
     * Se utiliza para el calculo de PV
     */
    private final TriangularPVTable trianglePV;

    public LinkTrianglePVVisitor(TriangularPVTable trianglePV) {
        this.trianglePV = trianglePV;
    }

    @Override
    public void visit(PVCalculatorTriangular setTrianglePV) {
        setTrianglePV.setTrianglePV(trianglePV);
    }

    @Override
    public void visit(CalculatePV calculatePV) {
        calculatePV.setTrianglePV(trianglePV);
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
