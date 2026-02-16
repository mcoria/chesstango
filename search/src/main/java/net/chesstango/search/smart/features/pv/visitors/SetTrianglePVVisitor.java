package net.chesstango.search.smart.features.pv.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.pv.filters.TriangularPV;

/**
 *
 * @author Mauricio Coria
 */
public class SetTrianglePVVisitor implements Visitor {

    /**
     * Se utiliza para el calculo de PV
     */
    private final short[][] trianglePV;

    public SetTrianglePVVisitor(short[][] trianglePV) {
        this.trianglePV = trianglePV;
    }


    @Override
    public void visit(TriangularPV triangularPV) {
        triangularPV.setTrianglePV(trianglePV);
    }
}
