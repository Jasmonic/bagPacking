package experiment;

import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.cplex.IloCplex;

import java.util.Arrays;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-11-2022-11-09
 * @Description: BPP_Model
 * @version=1.0
 */
public class GetFromImportModel {
    public static void main(String[] args) throws IloException {
        IloCplex cplex=new IloCplex();
        cplex.importModel("lp/Bagali2_3_abcde.lp");
        IloLPMatrix lp = (IloLPMatrix)cplex.LPMatrixIterator().next();
        System.out.println(Arrays.toString(lp.getNumVars()));
//        System.out.println(lp);
    }
}
