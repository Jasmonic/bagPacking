package benders;

import ilog.concert.IloException;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import singleBag.BeamSearch;
import singleBag.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-10-2022-10-23
 * @Description: BPP_Model
 * @version=1.0
 */
public class BendersCallback implements IloCplex.Callback.Function {
    //    private final int bagCount;
//    private final int boxCount;
    private final MasterProblem master;
    private final Instance instance;       // the problem being solved
    //    private final MasterProblem master;  // the master problem
//    private final ThreadLocal<Subproblem> subproblem;
    private final SubProblem[] subProblems;
    final HashMap<Pattern, Boolean> isPatternFeasible;

    public BendersCallback(int threadNum, MasterProblem master, Instance instance) {
        subProblems = new SubProblem[threadNum];
        this.master = master;
        this.instance = instance;
        isPatternFeasible = instance.getIsPatternFeasible();
    }

    @Override
    public void invoke(IloCplex.Callback.Context context) throws IloException {
        int threadNo = context.getIntInfo(IloCplex.Callback.Context.Info.ThreadId);
        int nth = context.getIntInfo(IloCplex.Callback.Context.Info.Threads);
        switch ((int) context.getId()) {
            case (int) IloCplex.Callback.Context.Id.ThreadUp:
                subProblems[threadNo] = new SubProblem(instance);
                System.out.println("!!Thread UP");
                return;

            case (int) IloCplex.Callback.Context.Id.ThreadDown:
                subProblems[threadNo].end();
                System.out.println("!!Thread down");
                return;

            case (int) IloCplex.Callback.Context.Id.Candidate:
                SubProblem subProblem = subProblems[threadNo];
                double[][] s = master.getS(context);
                double[] n = master.getN(context);
                ArrayList<IloRange> cutsList = new ArrayList<>();
//                System.out.println("print s[i]");
//                for (int i = 0; i < s.length; i++) {
//                    System.out.println(Arrays.toString(s[i]));
//                }
                System.out.println("print n");
                System.out.println(Arrays.toString(n));
//                System.out.println("print obj");
//                System.out.println(master.getObj(context));
                for (int j = 0; j < s[0].length; j++) {
                    double n_j = 0;
//                    System.out.println(67);
                    for (int i = 0; i < s.length; i++) {
                        n_j += s[i][j];
//                        System.out.println("s_ij"+s[i][j]+","+n_j);
                    }
//                    System.out.println(n_j);
                    if (n_j > 0.5) {
                        ArrayList<Integer> items = new ArrayList<>();
                        for (int i = 0; i < s.length; i++) {
                            if (s[i][j] > 0.5) {
                                items.add(i);
                            }
                        }
                        //TODO if 如果检测到同样的pattern 直接reject
                        System.out.print("将物品  " + items + "  放进袋子  " + j + "  袋子种类为" + instance.getAllBags().get(j).getType()+"              ");
                        Pattern pattern = new Pattern(instance, items, j);
//                        System.out.println("pattern good");
                        boolean needToSolve = true;
                        boolean needToAddCut = false;
                        if (isPatternFeasible.containsKey(pattern)) {
                            needToSolve = false;
                            if (isPatternFeasible.get(pattern) == true) {
                                needToAddCut = false;
                            } else {
                                needToAddCut = true;
                            }
                        }
                        boolean useHeuristic= false;
                        Node lastNode=null;
                        if (needToSolve == true) {

                            if (items.size()<5){
                                //TODO 一般lp建模    备选项 subProblem.BuildPaquayModel(items,j);
                                //小于10个用精确解
                                subProblem.BuildChenModel(items, j);
//                            subProblem.BuildChenModelWithCost(items,j);
//                            subProblem.BuildChenModelMinCount(items, j);
                                System.out.print("子问题建模完毕-->");
                                subProblem.solveLP();
                                System.out.print("求解完毕-->");
                                System.out.print("子问题状态=" + subProblem.getLPStatus()+"           ");
                                if (subProblem.getLPStatus() == IloCplex.Status.Infeasible) {
                                    needToAddCut = true;
                                    isPatternFeasible.put(pattern, false);
                                } else {
//                                if (Math.abs(subProblem.getSubObj() - 1) > 0.1) {
//                                    needToAddCut = true;
//                                    isPatternFeasible.put(pattern, false);
//                                } else {
                                    needToAddCut=false;
                                    isPatternFeasible.put(pattern, true);
                                    instance.getPatternToPositions().put(pattern,subProblem.getChenModelResults(items,j));
//                                }
                                }
                            subProblem.clearLP();
//                            //TODO CP建模
//                            subProblem.BuildChenModelConstraintProgramming(items,j);
//                            System.out.println("子问题建模完毕");
//                            subProblem.solveCP();
//                            System.out.println("子问题求解完毕");
//                            System.out.println("子问题状态=" + subProblem.getCPStatus());
//                            if (subProblem.getCPStatus() == IloAlgorithm.Status.Infeasible) {
//                                needToAddCut = true;
//                                isPatternFeasible.put(pattern, false);
//                            } else {
//                                isPatternFeasible.put(pattern, true);
//                            }
//                            subProblem.clearCP();
                            }else{
                                //大于5个用启发式
                                useHeuristic=true;
                                BeamSearch epFfd=new BeamSearch();
                                epFfd.pack(instance,instance.getAllBags().get(j),items);
                                lastNode=epFfd.lastNode;
                                if (epFfd.successToPack==false){
                                    needToAddCut=true;
                                    isPatternFeasible.put(pattern, false);
                                }else{
                                    needToAddCut=false;
                                    isPatternFeasible.put(pattern, true);
                                    instance.getPatternToPositions().put(pattern, lastNode.getPackedPosition());
                                }
//                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                System.exit(0);
                            }

                        }

                        if (needToAddCut) {
                            if (useHeuristic){
//                                cutsList.add(master.generateNoGoodCutForHeuristic(lastNode,j));
                                cutsList.add(master.generateNoGoodCut(items, j));
                            }else{
                                cutsList.add(master.generateNoGoodCut(items, j));
                            }

//                            System.out.println("addCuts——" + cutsList.get(cutsList.size() - 1) + "无法将物品 " + items + "  放进袋子 " + j + " 袋子种类为" + instance.getAllBags().get(j).getType());
                        }
                        System.out.println("needToSolve=" + needToSolve + " needToAddCut=" + needToAddCut);

                    }
                }
                IloRange[] cuts = new IloRange[cutsList.size()];
                cutsList.toArray(cuts);
                if (!cutsList.isEmpty()) {
                    context.rejectCandidate(cuts);
                }
                instance.recordObj(System.currentTimeMillis()- instance.getStart(), master.getObj(context),cutsList.isEmpty());

                return;


            default:
                System.err.println("Callback was called from an invalid context: "
                        + context.getId() + ".\n");
        }
    }
}
