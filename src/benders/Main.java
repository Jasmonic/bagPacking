package benders;


import baseObject.*;
import benders.lowerBound.LowerBound;
import benders.upperBound.UpperBound;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-10-2022-10-21
 * @Description: BPP_Model
 * @version=1.0
 */
public class Main {

    public static void main(String[] args) throws IOException, IloException {
        long start;
        long end;
        Instance instance=new Instance();
        instance.initialze(args[0],args[1]);
        for (int i = 0; i < instance.getBags().size(); i++) {
            System.out.println(instance.getBags().get(i).getCost());
            System.out.println(instance.getBags().get(i).getMaxVolume());
            System.out.println(instance.getBags().get(i).getCost()/instance.getBags().get(i).getMaxVolume());
        }
//        assert false;
        System.out.println(instance.getBags());
        UpperBound upperBound=new UpperBound(instance);
        start = System.currentTimeMillis();
//        start = System.currentTimeMillis();
        upperBound.calculateUpperCounts();
        end = System.currentTimeMillis();

        System.out.println("Total time =" + (end - start) * 1.0 / 1000 + "s");
        int[]ub=upperBound.getUpperCounts();
        System.out.println(Arrays.toString(ub));
//        assert false;
        boolean feasible=false;
        for (int i : ub) {
            if (i > 0) {
                feasible = true;
                break;
            }
        }
        if (!feasible){
            System.out.println("infeasible");
            assert false;
        }
        for (int i = 0; i < ub.length; i++) {
            instance.getBags().get(i).setNum(ub[i]);
        }
        instance.bagTransformToOneDimension();
        instance.setCount();
        System.out.println(instance.getBagCount());
//        double ubCost= upperBound.calUbCost();

//        System.out.println(ubCost);
//        System.out.println(instance.getBags());
//        assert false;
        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
//        PrintStream ps = System.out;
//        ps = new PrintStream(new BufferedOutputStream(new FileOutputStream("./log/log"+args[1]+formatter.format(date)+".txt")), true);
//        System.setOut(ps);

//        LowerBound lowerBound=new LowerBound(instance);
//        double lb=lowerBound.calLB();
//        System.out.println(lb);

//        System.exit(0);

//           System.out.println(Arrays.toString(ub));
            System.out.println(instance.getAllItems());
            System.out.println(instance.getBags().toString());
            System.out.println(instance.countToString());
            System.out.println(instance.getBags());
            System.out.println(instance.getBagTypeCount());
            MasterProblem masterProblem = new MasterProblem(instance);
//            masterProblem.addUpperBound(ubCost);
//            masterProblem.addLowerBound(lb);
            instance.setStart(System.currentTimeMillis());
            int threadNum = 1;
            masterProblem.setThreadNum(threadNum);
            BendersCallback callback = new BendersCallback(threadNum, masterProblem, instance);
            masterProblem.attach(callback);
//            masterProblem.master.setParam(IloCplex.Param.TimeLimit,3);
            masterProblem.solve();
             end = System.currentTimeMillis();
        if (masterProblem.getStatus() == IloCplex.Status.Feasible) {
            System.out.println(masterProblem.getObjective());
            System.out.println("!!");
            masterProblem.printResult();
            System.out.println("??");
        }
            if (masterProblem.getStatus() == IloCplex.Status.Optimal) {
                System.out.println(masterProblem.getObjective());
//                System.out.println("!!");
                masterProblem.printResult();
//                System.out.println("??");
            }else {
                System.out.println(masterProblem.getStatus());
            }
//            instance.getFinalResult(masterProblem.getSs());
            System.out.println("Total time =" + (end - start) * 1.0 / 1000 + "s");
            System.out.println("——————————————————————————————————————————————————");
//        for (int i = 0; i < instance.objList.size(); i++) {
//            System.out.println(instance.getTimeList().get(i)*1.0/1000+"   "+instance.objList.get(i)+"    "+instance.statusList.get(i));
//        }

        FileWriter fw = new FileWriter("D:\\java\\BPP_Model\\bishe\\part3\\BS_preprocess.txt",true);
        fw.write(args[1]+" "+masterProblem.getStatus());
        if (masterProblem.getStatus()== IloCplex.Status.Optimal){
            fw.write(" "+masterProblem.getObjective());
        }
        fw.write(" "+(end - start) * 1.0 / 1000 + "s");
        fw.write("\n");//Windows平台下用\r\n，Linux/Unix平台下用\n
        fw.close();


//        assert false;
//        SubProblem subProblem = new SubProblem(instance);
//        ArrayList<Integer> items=new ArrayList<>();
//        int[] itemss={0, 3, 4,5};
//        for (int i = 0; i < itemss.length; i++) {
//            items.add(itemss[i]);
//        }
//// [0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 18]  放进袋子  38  instance48
////        subProblem.BuildChenModelMinCount(items,38);
////        subProblem.solveLP();
////        System.out.println(subProblem.getLPStatus());
////        System.out.println(subProblem.getSubObj());
//        subProblem.BuildChenModel(items,12);
////        subProblem.BuildChenKnapsack(items,12);
//        subProblem.solveLP();
//        System.out.println(subProblem.getLPStatus());
//        System.out.println(subProblem.getSubObj());
//        subProblem.exportLP("sub38.lp");
////        subProblem.solveCP();
////        System.out.println(subProblem.getCPStatus());
//        subProblem.exportCP("sub48.cpo");
//
//
//        assert false;
//        subProblem.BuildChenModel(items,17);
//        subProblem.solveLP();
//        System.out.println(subProblem.getLPStatus());
//
////        System.out.println(subProblem.getStatus());
////        System.out.println(subProblem.getSubObj());
    }
}
