package benders;


import baseObject.Bag;
import baseObject.Item;
import ilog.concert.*;
import ilog.concert.cppimpl.IloAlgorithm;
import ilog.cp.IloCP;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-10-2022-10-23
 * @Description: BPP_Model
 * @version=1.0
 */
public class SubProblem2 {
    private final IloCplex sub;
    private final Instance instance;
    private  IloCP cp;

    public SubProblem2(Instance instance) throws IloException {
        sub = new IloCplex();
        cp = new IloCP();
        this.instance = instance;
    }

    public void BuildPaquayModel(ArrayList<Integer> items, int bag) {

    }

    public void BuildChenModel(ArrayList<Integer> items, int bag) throws IloException {
        int boxCount = items.size();
        sub.setOut(null);
        ArrayList<Bag> allBags = instance.getAllBags();
        ArrayList<Item> allItems = instance.getAllItems();
//        System.out.println(boxCount);
//        sub.setParam(IloCplex.Param.Threads, 8);
//        System.out.println(allBags.get(bag));
//        System.out.println(items + "," + bag);
//        IloNumVar[] x, y, z;
        IloNumVar[] x=new IloNumVar[boxCount];
        IloNumVar[] y=new IloNumVar[boxCount];
        IloNumVar[] z=new IloNumVar[boxCount];
        IloNumVar L, W, H;
        IloNumVar[][] a = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] b = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] c = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] d = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] e = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] f = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] l = new IloNumVar[boxCount][3];
        IloNumVar[][] w = new IloNumVar[boxCount][3];
        IloNumVar[][] h = new IloNumVar[boxCount][3];
        double Mx = allBags.get(bag).getX(), My = allBags.get(bag).getY(), Mz = Math.max(allBags.get(bag).getX(), allBags.get(bag).getY());
//        System.out.println(Mx+" "+My);
        for (int i = 0; i < boxCount; i++) {
            x[i] = sub.numVar(0, Mx, "x_" + i);
            y[i] = sub.numVar(0, My, "y_" + i);
            z[i] = sub.numVar(0, Mz, "z_" + i);
        }
        L = sub.numVar(0, Mx,"L");
        W = sub.numVar(0, My,"W");
        H = sub.numVar(0, Mz,"H");
        for (int i = 0; i < boxCount - 1; i++) {
            for (int j = i + 1; j < boxCount; j++) {
                a[i][j] = sub.intVar(0, 1, "a_" + i + "," + j);
                b[i][j] = sub.intVar(0, 1, "b_" + i + "," + j);
                c[i][j] = sub.intVar(0, 1, "c_" + i + "," + j);
                d[i][j] = sub.intVar(0, 1, "d_" + i + "," + j);
                e[i][j] = sub.intVar(0, 1, "r_" + i + "," + j);
                f[i][j] = sub.intVar(0, 1, "f_" + i + "," + j);
            }
        }
        for (int i = 0; i < boxCount; i++) {
            l[i][0] = sub.intVar(0, 1, "lx_" + i);
            l[i][1] = sub.intVar(0, 1, "ly_" + i);
            l[i][2] = sub.intVar(0, 1, "lz_" + i);
            w[i][0] = sub.intVar(0, 1, "wx_" + i);
            w[i][1] = sub.intVar(0, 1, "wy_" + i);
            w[i][2] = sub.intVar(0, 1, "wz_" + i);
            h[i][0] = sub.intVar(0, 1, "hx_" + i);
            h[i][1] = sub.intVar(0, 1, "hy_" + i);
            h[i][2] = sub.intVar(0, 1, "hz_" + i);
        }
        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                IloNumExpr con1 = sub.numExpr();
                con1 = sub.sum(con1, x[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][0]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][0]), sub.prod(allItems.get(items.get(i)).getR(), h[i][0]));
                sub.addLe(con1, sub.sum(x[k], sub.prod(sub.diff(1, a[i][k]), Mx)), "xi_" + i + "," + k);

                IloNumExpr con2 = sub.numExpr();
                con2 = sub.sum(con2, x[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][0]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][0]), sub.prod(allItems.get(items.get(k)).getR(), h[k][0]));
                sub.addLe(con2, sub.sum(x[i], sub.prod(sub.diff(1, b[i][k]), Mx)), "xk_" + i + "," + k);

                IloNumExpr con3 = sub.numExpr();
                con3 = sub.sum(con3, y[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][1]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][1]), sub.prod(allItems.get(items.get(i)).getR(), h[i][1]));
                sub.addLe(con3, sub.sum(y[k], sub.prod(sub.diff(1, c[i][k]), My)), "yi_" + i + "," + k);

                IloNumExpr con4 = sub.numExpr();
                con4 = sub.sum(con4, y[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][1]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][1]), sub.prod(allItems.get(items.get(k)).getR(), h[k][1]));
                sub.addLe(con4, sub.sum(y[i], sub.prod(sub.diff(1, d[i][k]), My)), "yk_" + i + "," + k);

                IloNumExpr con5 = sub.numExpr();
                con5 = sub.sum(con5, z[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][2]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][2]), sub.prod(allItems.get(items.get(i)).getR(), h[i][2]));
                sub.addLe(con5, sub.sum(z[k], sub.prod(sub.diff(1, e[i][k]), Mz)), "zi_" + i + "," + k);

                IloNumExpr con6 = sub.numExpr();
                con6 = sub.sum(con6, z[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][2]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][2]), sub.prod(allItems.get(items.get(k)).getR(), h[k][2]));
                sub.addLe(con6, sub.sum(z[i], sub.prod(sub.diff(1, f[i][k]), Mz)), "zk_" + i + "," + k);
            }
        }

        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                IloNumExpr con7 = sub.numExpr();
                con7 = sub.sum(con7, a[i][k], b[i][k], c[i][k], d[i][k], e[i][k], f[i][k]);
                sub.addGe(con7, 1, "position_"  + i + "," + k);
            }

        }
        for (int i = 0; i < boxCount; i++) {
            sub.addEq(sub.sum(l[i][0], l[i][1], l[i][2]), 1);
            sub.addEq(sub.sum(w[i][0], w[i][1], w[i][2]), 1);
            sub.addEq(sub.sum(h[i][0], h[i][1], h[i][2]), 1);
            sub.addEq(sub.sum(l[i][0], w[i][0], h[i][0]), 1);
            sub.addEq(sub.sum(l[i][1], w[i][1], h[i][1]), 1);
            sub.addEq(sub.sum(l[i][2], w[i][2], h[i][2]), 1);
        }
        for (int i = 0; i < boxCount; i++) {
            IloNumExpr con10 = sub.numExpr();
            con10 = sub.sum(x[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][0]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][0]), sub.prod(allItems.get(items.get(i)).getR(), h[i][0]));
            sub.addLe(con10, L, "length_" + i + "," + bag);
//            System.out.println("kkkkkkk55");
            IloNumExpr con11 = sub.numExpr();
            con11 = sub.sum(y[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][1]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][1]), sub.prod(allItems.get(items.get(i)).getR(), h[i][1]));
            sub.addLe(con11, W, "width_" + i + "," + bag);
            IloNumExpr con12 = sub.numExpr();
            con12 = sub.sum(z[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][2]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][2]), sub.prod(allItems.get(items.get(i)).getR(), h[i][2]));
            sub.addLe(con12, H, "height_" + i + "," + bag);

        }
        //Branch and repair 里面的对称
        IloNumExpr con11 = sub.numExpr();
        con11 = sub.sum(con11, x[0], sub.prod(0.5 * allItems.get(items.get(0)).getP(), l[0][0]), sub.prod(0.5 * allItems.get(items.get(0)).getQ(), w[0][0]), sub.prod(0.5 * allItems.get(items.get(0)).getR(), h[0][0]));
        sub.addLe(con11, sub.prod(0.5, L));
        IloNumExpr con12 = sub.numExpr();
        con12 = sub.sum(con12, y[0], sub.prod(0.5 * allItems.get(items.get(0)).getP(), l[0][1]), sub.prod(0.5 * allItems.get(items.get(0)).getQ(), w[0][1]), sub.prod(0.5 * allItems.get(items.get(0)).getR(), h[0][1]));
        sub.addLe(con12, sub.prod(0.5, W));
        IloNumExpr con13 = sub.numExpr();
        con13 = sub.sum(con13, z[0], sub.prod(0.5 * allItems.get(items.get(0)).getP(), l[0][2]), sub.prod(0.5 * allItems.get(items.get(0)).getQ(), w[0][2]), sub.prod(0.5 * allItems.get(items.get(0)).getR(), h[0][2]));
        sub.addLe(con13, sub.prod(0.5, H));
        // 10.29 idea:a+b<=1 c+d<=1 e+f<=1
//        for (int i = 0; i < boxCount - 1; i++) {
//            for (int k = i + 1; k < boxCount; k++) {
//                sub.addLe(sub.sum(a[i][k], b[i][k]), 1);
//                sub.addLe(sub.sum(c[i][k], d[i][k]), 1);
//                sub.addLe(sub.sum(e[i][k], f[i][k]), 1);
//            }
//        }
        //11.06 idea:同type bdf=0
        for (int i = 0; i < boxCount; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                if (allItems.get(items.get(i)).getType() == allItems.get(items.get(k)).getType()) {
                    b[i][k].setUB(0);
                    d[i][k].setUB(0);
                    f[i][k].setUB(0);
//                    System.out.println("对称!!!!!!!!!!!!!!!" + i + " " + k);
                }
            }
        }
        //TODO 11.15 idea: H<Y/2;
        H.setUB(allBags.get(bag).getY()/2);
//        sub.addLe(H,allBags.get(bag).getY()/2,"transformCUt");
//        for (int i = 0; i < boxCount; i++) {
//            sub.addLe(z[i],allBags.get(bag).getY()/2,"z_cut"+"i");
//        }
        //袋子变形约束
        sub.addLe(sub.sum(L, H), allBags.get(bag).getX(), "transformX_" + bag);
        sub.addLe(sub.sum(W, H), allBags.get(bag).getY(), "transformY_" + bag);
        IloNumVar obj = sub.boolVar();
//        sub.setParam(IloCplex.Param.Emphasis.MIP,1);
//        sub.addMinimize(obj);
//        sub.solve();
//        double[]  lx, ly, lz;
//        lx = new double[boxCount];
//        ly = new double[boxCount];
//        lz = new double[boxCount];
//        System.out.println(sub.getValue(L)+" "+sub.getValue(W)+" "+sub.getValue(H));
//        System.out.println("x:"+ Arrays.toString(sub.getValues(x)));
//        System.out.println("y:"+ Arrays.toString(sub.getValues(y)));
//        System.out.println("z:"+ Arrays.toString(sub.getValues(z)));
//        for (int i = 0; i < boxCount; i++) {
//            double[] ll = sub.getValues(l[i]);
//            double[] ww = sub.getValues(w[i]);
//            double[] hh = sub.getValues(h[i]);
//            System.out.print("l:" + Arrays.toString(ll));
//            System.out.print("w:" + Arrays.toString(ww));
//            System.out.print("h:" + Arrays.toString(hh));
//            System.out.println();
//            if (ll[0] > 0.9) {
//                lx[i] = allItems.get(items.get(i)).getP();
//            } else {
//                if (ww[0] > 0.9) {
//                    lx[i] = allItems.get(items.get(i)).getQ();
//                } else {
//                    lx[i] = allItems.get(items.get(i)).getR();
//                }
//            }
//            if (ll[1] > 0.9) {
//                ly[i] = allItems.get(items.get(i)).getP();
//            } else {
//                if (ww[1] > 0.9) {
//                    ly[i] = allItems.get(items.get(i)).getQ();
//                } else {
//                    ly[i] = allItems.get(items.get(i)).getR();
//                }
//            }
//            if (ll[2] > 0.9) {
//                lz[i] = allItems.get(items.get(i)).getP();
//            } else {
//                if (ww[2] > 0.9) {
//                    lz[i] = allItems.get(items.get(i)).getQ();
//                } else {
//                    lz[i] = allItems.get(items.get(i)).getR();
//                }
//            }
//
//        }
//        System.out.println("lx:" + Arrays.toString(lx));
//        System.out.println("ly:" + Arrays.toString(ly));
//        System.out.println("lz:" + Arrays.toString(lz));

    }

    public void BuildChenModelWithCost(ArrayList<Integer> items, int bag) throws IloException {
//        sub.setOut(null);
        sub.setParam(IloCplex.Param.Threads, 2);
        int boxCount = items.size();
        int bagTypeCount = instance.getBagTypeCount();
        ArrayList<Item> allItems = instance.getAllItems();
        ArrayList<Bag> bags = instance.getBags();
        int targetType = instance.getAllBags().get(bag).getType();
        IloNumVar[] n = new IloNumVar[bagTypeCount];
        IloNumVar[] x = new IloNumVar[boxCount];
        IloNumVar[] y = new IloNumVar[boxCount];
        IloNumVar[] z = new IloNumVar[boxCount];
        IloNumVar[][] l = new IloNumVar[boxCount][3];
        IloNumVar[][] w = new IloNumVar[boxCount][3];
        IloNumVar[][] h = new IloNumVar[boxCount][3];
        IloNumVar[][] s = new IloNumVar[boxCount][bagTypeCount];
        IloNumVar[][] a = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] b = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] c = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] d = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] e = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] f = new IloNumVar[boxCount][boxCount];
        IloNumVar[] L = new IloNumVar[bagTypeCount];
        IloNumVar[] W = new IloNumVar[bagTypeCount];
        IloNumVar[] H = new IloNumVar[bagTypeCount];
        for (int i = 0; i < bagTypeCount; i++) {
            n[i] = sub.intVar(0, 1, "n_" + i);
            double dim = Math.max(instance.getBags().get(i).getX(), instance.getBags().get(i).getY());
//            maxDim = Math.max(maxDim, dim);
            L[i] = sub.numVar(0, dim, "L_" + i);
            W[i] = sub.numVar(0, dim, "W_" + i);
            H[i] = sub.numVar(0, dim, "H_" + i);
        }

        for (int i = 0; i < boxCount - 1; i++) {
            for (int j = i + 1; j < boxCount; j++) {
                a[i][j] = sub.intVar(0, 1, "a_" + i + "," + j);
                b[i][j] = sub.intVar(0, 1, "b_" + i + "," + j);
                c[i][j] = sub.intVar(0, 1, "c_" + i + "," + j);
                d[i][j] = sub.intVar(0, 1, "d_" + i + "," + j);
                e[i][j] = sub.intVar(0, 1, "r_" + i + "," + j);
                f[i][j] = sub.intVar(0, 1, "f_" + i + "," + j);
            }
        }
        for (int i = 0; i < boxCount; i++) {
            l[i][0] = sub.intVar(0, 1, "lx_" + i);
            l[i][1] = sub.intVar(0, 1, "ly_" + i);
            l[i][2] = sub.intVar(0, 1, "lz_" + i);
            w[i][0] = sub.intVar(0, 1, "wx_" + i);
            w[i][1] = sub.intVar(0, 1, "wy_" + i);
            w[i][2] = sub.intVar(0, 1, "wz_" + i);
            h[i][0] = sub.intVar(0, 1, "hx_" + i);
            h[i][1] = sub.intVar(0, 1, "hy_" + i);
            h[i][2] = sub.intVar(0, 1, "hz_" + i);
        }
        for (int i = 0; i < boxCount; i++) {
            for (int j = 0; j < bagTypeCount; j++) {
                s[i][j] = sub.intVar(0, 1, "s_" + i + "," + j);
            }
        }
        double M1 = 0, M2 = 0, M3 = 0, M4 = 0, M5 = 0, M6 = 0, M9 = boxCount + 1, M10 = 0, M11 = 0, M12 = 0;
        double maxDim = 0;

        for (Bag tempBag : bags) {
            M1 = M2 = M10 = Math.max(M10, tempBag.getX());
            M3 = M4 = M11 = Math.max(M11, tempBag.getY());
            M5 = M6 = M12 = Math.max(Math.max(M12, M10), M11);
        }
//                M1 = M2 = M3 = M4 = M5 = M6 = M9 = M10 = M11 = M12 = 10000;
        for (int i = 0; i < boxCount; i++) {
            x[i] = sub.numVar(0, M1, "x_" + i);
            y[i] = sub.numVar(0, M2, "y_" + i);
            z[i] = sub.numVar(0, M3, "z_" + i);
        }
        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                IloNumExpr con1 = sub.numExpr();
                con1 = sub.sum(con1, x[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][0]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][0]), sub.prod(allItems.get(items.get(i)).getR(), h[i][0]));
                sub.addLe(con1, sub.sum(x[k], sub.prod(sub.diff(1, a[i][k]), M1)), "xi_" + i + "," + k);

                IloNumExpr con2 = sub.numExpr();
                con2 = sub.sum(con2, x[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][0]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][0]), sub.prod(allItems.get(items.get(k)).getR(), h[k][0]));
                sub.addLe(con2, sub.sum(x[i], sub.prod(sub.diff(1, b[i][k]), M2)), "xk_" + i + "," + k);

                IloNumExpr con3 = sub.numExpr();
                con3 = sub.sum(con3, y[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][1]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][1]), sub.prod(allItems.get(items.get(i)).getR(), h[i][1]));
                sub.addLe(con3, sub.sum(y[k], sub.prod(sub.diff(1, c[i][k]), M3)), "yi_" + i + "," + k);

                IloNumExpr con4 = sub.numExpr();
                con4 = sub.sum(con4, y[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][1]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][1]), sub.prod(allItems.get(items.get(k)).getR(), h[k][1]));
                sub.addLe(con4, sub.sum(y[i], sub.prod(sub.diff(1, d[i][k]), M4)), "yk_" + i + "," + k);

                IloNumExpr con5 = sub.numExpr();
                con5 = sub.sum(con5, z[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][2]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][2]), sub.prod(allItems.get(items.get(i)).getR(), h[i][2]));
                sub.addLe(con5, sub.sum(z[k], sub.prod(sub.diff(1, e[i][k]), M5)), "zi_" + i + "," + k);

                IloNumExpr con6 = sub.numExpr();
                con6 = sub.sum(con6, z[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][2]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][2]), sub.prod(allItems.get(items.get(k)).getR(), h[k][2]));
                sub.addLe(con6, sub.sum(z[i], sub.prod(sub.diff(1, f[i][k]), M6)), "zk_" + i + "," + k);
            }
        }
        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                for (int j = 0; j < bagTypeCount; j++) {
                    IloNumExpr con7 = sub.numExpr();
                    con7 = sub.sum(con7, a[i][k], b[i][k], c[i][k], d[i][k], e[i][k], f[i][k]);
                    con7 = sub.sum(con7, 1);
                    sub.addGe(con7, sub.sum(s[i][j], s[k][j]), "position_" + "," + i + "," + k + "," + j);
                }
            }
        }
        for (int i = 0; i < boxCount; i++) {
            IloLinearNumExpr con8 = sub.linearNumExpr();
            for (int j = 0; j < bagTypeCount; j++) {
                con8.addTerm(1, s[i][j]);
            }
            sub.addEq(con8, 1, "bag_and_box" + i);
        }
        for (int j = 0; j < bagTypeCount; j++) {
            IloLinearNumExpr con9 = sub.linearNumExpr();
            for (int i = 0; i < boxCount; i++) {
                con9.addTerm(1, s[i][j]);
            }
            sub.addLe(con9, sub.prod(boxCount, n[j]), "bagUsed_" + j);
        }
        for (int i = 0; i < boxCount; i++) {
            for (int j = 0; j < bagTypeCount; j++) {
                IloNumExpr con10 = sub.numExpr();
                con10 = sub.sum(x[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][0]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][0]), sub.prod(allItems.get(items.get(i)).getR(), h[i][0]));
                sub.addLe(con10, sub.sum(L[j], sub.prod(sub.diff(1, s[i][j]), M10)), "length_" + i + "," + j);

                IloNumExpr con11 = sub.numExpr();
                con11 = sub.sum(y[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][1]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][1]), sub.prod(allItems.get(items.get(i)).getR(), h[i][1]));
                sub.addLe(con11, sub.sum(W[j], sub.prod(sub.diff(1, s[i][j]), M11)), "width_" + i + "," + j);
                IloNumExpr con12 = sub.numExpr();
                con12 = sub.sum(z[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][2]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][2]), sub.prod(allItems.get(items.get(i)).getR(), h[i][2]));
                sub.addLe(con12, sub.sum(H[j], sub.prod(sub.diff(1, s[i][j]), M12)), "height_" + i + "," + j);
            }
        }
        for (int i = 0; i < boxCount; i++) {
            sub.addEq(sub.sum(l[i][0], l[i][1], l[i][2]), 1);
            sub.addEq(sub.sum(w[i][0], w[i][1], w[i][2]), 1);
            sub.addEq(sub.sum(h[i][0], h[i][1], h[i][2]), 1);
            sub.addEq(sub.sum(l[i][0], w[i][0], h[i][0]), 1);
            sub.addEq(sub.sum(l[i][1], w[i][1], h[i][1]), 1);
            sub.addEq(sub.sum(l[i][2], w[i][2], h[i][2]), 1);
        }
        for (int i = 0; i < bagTypeCount; i++) {
            sub.addLe(sub.sum(L[i], H[i]), bags.get(i).getX(), "transformX_" + i);
            sub.addLe(sub.sum(W[i], H[i]), bags.get(i).getY(), "transformY_" + i);
        }
        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                sub.addLe(sub.sum(a[i][k], b[i][k]), 1);
                sub.addLe(sub.sum(c[i][k], d[i][k]), 1);
                sub.addLe(sub.sum(e[i][k], f[i][k]), 1);
            }
        }
        //11.06 idea:同type bef=0
        for (int i = 0; i < boxCount; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                if (allItems.get(items.get(i)).getType() == allItems.get(items.get(k)).getType()) {
                    b[i][k].setUB(0);
                    d[i][k].setUB(0);
                    f[i][k].setUB(0);
//                    System.out.println("对称!!!!!!!!!!!!!!!" + i + " " + k);
                }
            }
        }
        //Branch and repair 里面的对称
        IloNumExpr con11 = sub.numExpr();
        con11 = sub.sum(con11, x[0], sub.prod(0.5 * allItems.get(items.get(0)).getP(), l[0][0]), sub.prod(0.5 * allItems.get(items.get(0)).getQ(), w[0][0]), sub.prod(0.5 * allItems.get(items.get(0)).getR(), h[0][0]));
        sub.addLe(con11, sub.prod(0.5, L[targetType]));
        IloNumExpr con12 = sub.numExpr();
        con12 = sub.sum(con12, y[0], sub.prod(0.5 * allItems.get(items.get(0)).getP(), l[0][1]), sub.prod(0.5 * allItems.get(items.get(0)).getQ(), w[0][1]), sub.prod(0.5 * allItems.get(items.get(0)).getR(), h[0][1]));
        sub.addLe(con12, sub.prod(0.5, W[targetType]));
        IloNumExpr con13 = sub.numExpr();
        con13 = sub.sum(con13, z[0], sub.prod(0.5 * allItems.get(items.get(0)).getP(), l[0][2]), sub.prod(0.5 * allItems.get(items.get(0)).getQ(), w[0][2]), sub.prod(0.5 * allItems.get(items.get(0)).getR(), h[0][2]));
        sub.addLe(con13, sub.prod(0.5, H[targetType]));
        IloLinearNumExpr con10 = sub.linearNumExpr();
        for (int i = 0; i < bagTypeCount; i++) {
            con10.addTerm(1, n[i]);
        }
        sub.addLe(con10, 1);
        sub.addEq(n[targetType], 1);
        IloLinearNumExpr obj = sub.linearNumExpr();
        for (int j = 0; j < bagTypeCount; j++) {
            obj.addTerm(bags.get(j).getCost(), n[j]);
        }
        sub.addMinimize(obj);
    }
    public void BuildChenModelMinCount(ArrayList<Integer> items, int bag) throws IloException {
        sub.setParam(IloCplex.Param.Threads, 2);
        int boxCount = items.size();
        int bagCount = 2;
        ArrayList<Item> allItems = instance.getAllItems();
        ArrayList<Bag> bags = new ArrayList<>();
        for (int i = 0; i <bagCount; i++) {
            bags.add(instance.getAllBags().get(bag));
        }
        IloNumVar[] n = new IloNumVar[bagCount];
        IloNumVar[] x = new IloNumVar[boxCount];
        IloNumVar[] y = new IloNumVar[boxCount];
        IloNumVar[] z = new IloNumVar[boxCount];
        IloNumVar[][] l = new IloNumVar[boxCount][3];
        IloNumVar[][] w = new IloNumVar[boxCount][3];
        IloNumVar[][] h = new IloNumVar[boxCount][3];
        IloNumVar[][] s = new IloNumVar[boxCount][bagCount];
        IloNumVar[][] a = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] b = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] c = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] d = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] e = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] f = new IloNumVar[boxCount][boxCount];
        IloNumVar[] L = new IloNumVar[bagCount];
        IloNumVar[] W = new IloNumVar[bagCount];
        IloNumVar[] H = new IloNumVar[bagCount];
        for (int i = 0; i < bagCount; i++) {
            n[i] = sub.intVar(0, 1, "n_" + i);
            double dim = Math.max(instance.getBags().get(i).getX(), instance.getBags().get(i).getY());
//            maxDim = Math.max(maxDim, dim);
            L[i] = sub.numVar(0, dim, "L_" + i);
            W[i] = sub.numVar(0, dim, "W_" + i);
            H[i] = sub.numVar(0, dim, "H_" + i);
        }

        for (int i = 0; i < boxCount - 1; i++) {
            for (int j = i + 1; j < boxCount; j++) {
                a[i][j] = sub.intVar(0, 1, "a_" + i + "," + j);
                b[i][j] = sub.intVar(0, 1, "b_" + i + "," + j);
                c[i][j] = sub.intVar(0, 1, "c_" + i + "," + j);
                d[i][j] = sub.intVar(0, 1, "d_" + i + "," + j);
                e[i][j] = sub.intVar(0, 1, "r_" + i + "," + j);
                f[i][j] = sub.intVar(0, 1, "f_" + i + "," + j);
            }
        }
        for (int i = 0; i < boxCount; i++) {
            l[i][0] = sub.intVar(0, 1, "lx_" + i);
            l[i][1] = sub.intVar(0, 1, "ly_" + i);
            l[i][2] = sub.intVar(0, 1, "lz_" + i);
            w[i][0] = sub.intVar(0, 1, "wx_" + i);
            w[i][1] = sub.intVar(0, 1, "wy_" + i);
            w[i][2] = sub.intVar(0, 1, "wz_" + i);
            h[i][0] = sub.intVar(0, 1, "hx_" + i);
            h[i][1] = sub.intVar(0, 1, "hy_" + i);
            h[i][2] = sub.intVar(0, 1, "hz_" + i);
        }
        for (int i = 0; i < boxCount; i++) {
            for (int j = 0; j < bagCount; j++) {
                s[i][j] = sub.intVar(0, 1, "s_" + i + "," + j);
            }
        }
        double M1 = 0, M2 = 0, M3 = 0, M4 = 0, M5 = 0, M6 = 0, M9 = boxCount + 1, M10 = 0, M11 = 0, M12 = 0;
        double maxDim = 0;

        for (Bag tempBag : bags) {
            M1 = M2 = M10 = Math.max(M10, tempBag.getX());
            M3 = M4 = M11 = Math.max(M11, tempBag.getY());
            M5 = M6 = M12 = Math.max(Math.max(M12, M10), M11);
        }
//                M1 = M2 = M3 = M4 = M5 = M6 = M9 = M10 = M11 = M12 = 10000;
        for (int i = 0; i < boxCount; i++) {
            x[i] = sub.numVar(0, M1, "x_" + i);
            y[i] = sub.numVar(0, M2, "y_" + i);
            z[i] = sub.numVar(0, M3, "z_" + i);
        }
        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                IloNumExpr con1 = sub.numExpr();
                con1 = sub.sum(con1, x[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][0]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][0]), sub.prod(allItems.get(items.get(i)).getR(), h[i][0]));
                sub.addLe(con1, sub.sum(x[k], sub.prod(sub.diff(1, a[i][k]), M1)), "xi_" + i + "," + k);

                IloNumExpr con2 = sub.numExpr();
                con2 = sub.sum(con2, x[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][0]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][0]), sub.prod(allItems.get(items.get(k)).getR(), h[k][0]));
                sub.addLe(con2, sub.sum(x[i], sub.prod(sub.diff(1, b[i][k]), M2)), "xk_" + i + "," + k);

                IloNumExpr con3 = sub.numExpr();
                con3 = sub.sum(con3, y[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][1]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][1]), sub.prod(allItems.get(items.get(i)).getR(), h[i][1]));
                sub.addLe(con3, sub.sum(y[k], sub.prod(sub.diff(1, c[i][k]), M3)), "yi_" + i + "," + k);

                IloNumExpr con4 = sub.numExpr();
                con4 = sub.sum(con4, y[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][1]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][1]), sub.prod(allItems.get(items.get(k)).getR(), h[k][1]));
                sub.addLe(con4, sub.sum(y[i], sub.prod(sub.diff(1, d[i][k]), M4)), "yk_" + i + "," + k);

                IloNumExpr con5 = sub.numExpr();
                con5 = sub.sum(con5, z[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][2]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][2]), sub.prod(allItems.get(items.get(i)).getR(), h[i][2]));
                sub.addLe(con5, sub.sum(z[k], sub.prod(sub.diff(1, e[i][k]), M5)), "zi_" + i + "," + k);

                IloNumExpr con6 = sub.numExpr();
                con6 = sub.sum(con6, z[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][2]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][2]), sub.prod(allItems.get(items.get(k)).getR(), h[k][2]));
                sub.addLe(con6, sub.sum(z[i], sub.prod(sub.diff(1, f[i][k]), M6)), "zk_" + i + "," + k);
            }
        }
        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                for (int j = 0; j < bagCount; j++) {
                    IloNumExpr con7 = sub.numExpr();
                    con7 = sub.sum(con7, a[i][k], b[i][k], c[i][k], d[i][k], e[i][k], f[i][k]);
                    con7 = sub.sum(con7, 1);
                    sub.addGe(con7, sub.sum(s[i][j], s[k][j]), "position_" + "," + i + "," + k + "," + j);
                }
            }
        }
        for (int i = 0; i < boxCount; i++) {
            IloLinearNumExpr con8 = sub.linearNumExpr();
            for (int j = 0; j < bagCount; j++) {
                con8.addTerm(1, s[i][j]);
            }
            sub.addEq(con8, 1, "bag_and_box" + i);
        }
        for (int j = 0; j < bagCount; j++) {
            IloLinearNumExpr con9 = sub.linearNumExpr();
            for (int i = 0; i < boxCount; i++) {
                con9.addTerm(1, s[i][j]);
            }
            sub.addLe(con9, sub.prod(boxCount, n[j]), "bagUsed_" + j);
        }

        for (int i = 0; i < boxCount; i++) {
            for (int j = 0; j < bagCount; j++) {
                IloNumExpr con10 = sub.numExpr();
                con10 = sub.sum(x[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][0]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][0]), sub.prod(allItems.get(items.get(i)).getR(), h[i][0]));
                sub.addLe(con10, sub.sum(L[j], sub.prod(sub.diff(1, s[i][j]), M10)), "length_" + i + "," + j);

                IloNumExpr con11 = sub.numExpr();
                con11 = sub.sum(y[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][1]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][1]), sub.prod(allItems.get(items.get(i)).getR(), h[i][1]));
                sub.addLe(con11, sub.sum(W[j], sub.prod(sub.diff(1, s[i][j]), M11)), "width_" + i + "," + j);
                IloNumExpr con12 = sub.numExpr();
                con12 = sub.sum(z[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][2]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][2]), sub.prod(allItems.get(items.get(i)).getR(), h[i][2]));
                sub.addLe(con12, sub.sum(H[j], sub.prod(sub.diff(1, s[i][j]), M12)), "height_" + i + "," + j);
            }
        }
        for (int i = 0; i < boxCount; i++) {
            sub.addEq(sub.sum(l[i][0], l[i][1], l[i][2]), 1);
            sub.addEq(sub.sum(w[i][0], w[i][1], w[i][2]), 1);
            sub.addEq(sub.sum(h[i][0], h[i][1], h[i][2]), 1);
            sub.addEq(sub.sum(l[i][0], w[i][0], h[i][0]), 1);
            sub.addEq(sub.sum(l[i][1], w[i][1], h[i][1]), 1);
            sub.addEq(sub.sum(l[i][2], w[i][2], h[i][2]), 1);
        }
        for (int i = 0; i < bagCount; i++) {
            sub.addLe(sub.sum(L[i], H[i]), bags.get(i).getX(), "transformX_" + i);
            sub.addLe(sub.sum(W[i], H[i]), bags.get(i).getY(), "transformY_" + i);
        }
        for (int j = 0; j < bagCount - 1; j++) {
            sub.addGe(n[j],n[j+1]);
        }
        IloLinearNumExpr obj=sub.linearNumExpr();
        for (int j = 0; j < bagCount; j++) {
            obj.addTerm(1,n[j]);
        }
        sub.addMinimize(obj);
    }
    public void BuildChenModelConstraintProgramming(ArrayList<Integer> items, int bag) throws IloException {
//        cp.setOut(null);
//        IloCP cp = new IloCP();
        int boxCount = items.size();
        System.out.println(items+" "+bag);
        ArrayList<Bag> allBags = instance.getAllBags();
        int bagTypeCount = instance.getBagTypeCount();
        ArrayList<Item> allItems = instance.getAllItems();
        ArrayList<Bag> bags = instance.getBags();
        double totalVolume=0;
        for (int i = 0; i < items.size(); i++) {
            totalVolume+=allItems.get(items.get(i)).getVolume();
        }
        //TODO 求集合
//        ArrayList<Integer> domain=new ArrayList<>();
        ArrayList<Integer> domain=new ArrayList<>();
//        int[] dimList;
//        ArrayList<Integer> dimList=new ArrayList<>();
        HashSet<Integer> dimSet=new HashSet<>();
        for (int i = 0; i <items.size() ; i++) {
            dimSet.add((int)allItems.get(items.get(i)).getP());
            dimSet.add((int)allItems.get(items.get(i)).getQ());
            dimSet.add((int)allItems.get(items.get(i)).getR());
            System.out.println((int)allItems.get(items.get(i)).getP()+" "+(int)allItems.get(items.get(i)).getQ()+" "+(int)allItems.get(items.get(i)).getR());
        }
//        dimList=new int[dimSet.size()];
        int[] dimList = dimSet.stream().mapToInt(Integer::intValue).toArray();
        Arrays.sort(dimList);
        System.out.println(Arrays.toString(dimList));
        Boolean[] index=new Boolean[(int)allBags.get(bag).getX()];
        Arrays.fill(index, false);
        index[0]=true;
        index[dimList[0]]=true;
        domain.add(0);
        domain.add(dimList[0]);
        for (int i = dimList[0]+1; i < index.length-dimList[0]; i++) {
            for (int j = 0; j < dimList.length; j++) {
                if (i-dimList[j]<dimList[0] && i-dimList[j]!=0) break;
                if (index[i-dimList[j]]==true){
                    index[i]=true;
                    domain.add(i);
                    break;
                }
            }
        }
        System.out.println(domain);
        System.out.println(index.length);
        System.out.println(domain.size());
//        assert false;
        IloIntVar[] n = new IloIntVar[bagTypeCount];
        IloNumVar[] x = new IloNumVar[boxCount];
        IloNumVar[] y = new IloNumVar[boxCount];
        IloNumVar[] z = new IloNumVar[boxCount];
        IloIntVar[][] l = new IloIntVar[boxCount][3];
        IloIntVar[][] w = new IloIntVar[boxCount][3];
        IloIntVar[][] h = new IloIntVar[boxCount][3];
        IloIntVar[][] a = new IloIntVar[boxCount][boxCount];
        IloIntVar[][] b = new IloIntVar[boxCount][boxCount];
        IloIntVar[][] c = new IloIntVar[boxCount][boxCount];
        IloIntVar[][] d = new IloIntVar[boxCount][boxCount];
        IloIntVar[][] e = new IloIntVar[boxCount][boxCount];
        IloIntVar[][] f = new IloIntVar[boxCount][boxCount];
        IloNumVar L, W, H;
        for (int i = 0; i < bagTypeCount; i++) {
            n[i] = cp.intVar(0, 1, "n_" + i);
            double dim = Math.max(instance.getBags().get(i).getX(), instance.getBags().get(i).getY());
//            maxDim = Math.max(maxDim, dim);
        }
        double Mx = allBags.get(bag).getX(), My = allBags.get(bag).getY(),
                Mz = Math.max(allBags.get(bag).getX(), allBags.get(bag).getY());
        for (int i = 0; i < boxCount; i++) {
            x[i] = cp.intVar(0, (int)Mx,"x_"+i);
            y[i] = cp.intVar(0, (int)My,"y_"+i);
            z[i] = cp.intVar(0, (int)Mz,"z_"+i);
        }
        L = cp.intVar(0, (int)Mx,"L");
        W = cp.intVar(0, (int)My,"W");
        H = cp.intVar(0, (int)Mz,"H");
        for (int i = 0; i < boxCount - 1; i++) {
            for (int j = i + 1; j < boxCount; j++) {
                a[i][j] = cp.intVar(0, 1, "a_" + i + "," + j);
                b[i][j] = cp.intVar(0, 1, "b_" + i + "," + j);
                c[i][j] = cp.intVar(0, 1, "c_" + i + "," + j);
                d[i][j] = cp.intVar(0, 1, "d_" + i + "," + j);
                e[i][j] = cp.intVar(0, 1, "r_" + i + "," + j);
                f[i][j] = cp.intVar(0, 1, "f_" + i + "," + j);
            }
        }
        for (int i = 0; i < boxCount; i++) {
            l[i][0] = cp.intVar(0, 1, "lx_" + i);
            l[i][1] = cp.intVar(0, 1, "ly_" + i);
            l[i][2] = cp.intVar(0, 1, "lz_" + i);
            w[i][0] = cp.intVar(0, 1, "wx_" + i);
            w[i][1] = cp.intVar(0, 1, "wy_" + i);
            w[i][2] = cp.intVar(0, 1, "wz_" + i);
            h[i][0] = cp.intVar(0, 1, "hx_" + i);
            h[i][1] = cp.intVar(0, 1, "hy_" + i);
            h[i][2] = cp.intVar(0, 1, "hz_" + i);
        }

//        for (int i = 0; i < boxCount - 1; i++) {
//            for (int k = i + 1; k < boxCount; k++) {
//                IloNumExpr con1 = cp.numExpr();
//                con1 = cp.sum(con1, x[i], cp.prod(allItems.get(items.get(i)).getP(), l[i][0]), cp.prod(allItems.get(items.get(i)).getQ(), w[i][0]), cp.prod(allItems.get(items.get(i)).getR(), h[i][0]));
////                cp.addLe(con1, cp.sum(x[k], cp.prod(cp.diff(1, a[i][k]), Mx)), "xi_" + i + "," + k);
//                IloConstraint constraint1=cp.le(con1,x[k]);
//
//                IloNumExpr con2 = cp.numExpr();
//                con2 = cp.sum(con2, x[k], cp.prod(allItems.get(items.get(k)).getP(), l[k][0]), cp.prod(allItems.get(items.get(k)).getQ(), w[k][0]), cp.prod(allItems.get(items.get(k)).getR(), h[k][0]));
////                cp.addLe(con2, cp.sum(x[i], cp.prod(cp.diff(1, b[i][k]), Mx)), "xk_" + i + "," + k);
//                IloConstraint constraint2=cp.le(con2,x[i]);
//
//                IloNumExpr con3 = cp.numExpr();
//                con3 = cp.sum(con3, y[i], cp.prod(allItems.get(items.get(i)).getP(), l[i][1]), cp.prod(allItems.get(items.get(i)).getQ(), w[i][1]), cp.prod(allItems.get(items.get(i)).getR(), h[i][1]));
////                cp.addLe(con3, cp.sum(y[k], cp.prod(cp.diff(1, c[i][k]), My)), "yi_" + i + "," + k);
//                IloConstraint constraint3=cp.le(con3,y[k]);
//
//                IloNumExpr con4 = cp.numExpr();
//                con4 = cp.sum(con4, y[k], cp.prod(allItems.get(items.get(k)).getP(), l[k][1]), cp.prod(allItems.get(items.get(k)).getQ(), w[k][1]), cp.prod(allItems.get(items.get(k)).getR(), h[k][1]));
////                cp.addLe(con4, cp.sum(y[i], cp.prod(cp.diff(1, d[i][k]), My)), "yk_" + i + "," + k);
//                IloConstraint constraint4=cp.le(con4,y[i]);
//
//                IloNumExpr con5 = cp.numExpr();
//                con5 = cp.sum(con5, z[i], cp.prod(allItems.get(items.get(i)).getP(), l[i][2]), cp.prod(allItems.get(items.get(i)).getQ(), w[i][2]), cp.prod(allItems.get(items.get(i)).getR(), h[i][2]));
////                cp.addLe(con5, cp.sum(z[k], cp.prod(cp.diff(1, e[i][k]), Mz)), "zi_" + i + "," + k);
//                IloConstraint constraint5=cp.le(con5,z[k]);
//
//                IloNumExpr con6 = cp.numExpr();
//                con6 = cp.sum(con6, z[k], cp.prod(allItems.get(items.get(k)).getP(), l[k][2]), cp.prod(allItems.get(items.get(k)).getQ(), w[k][2]), cp.prod(allItems.get(items.get(k)).getR(), h[k][2]));
////                cp.addLe(con6, cp.sum(z[i], cp.prod(cp.diff(1, f[i][k]), Mz)), "zk_" + i + "," + k);
//                IloConstraint constraint6=cp.le(con6,z[i]);
//                IloOr or= cp.or();
//                or=cp.or(or,constraint1);
//                or=cp.or(or,constraint2);
//                or=cp.or(or,constraint3);
//                or=cp.or(or,constraint4);
//                or=cp.or(or,constraint5);
//                or=cp.or(or,constraint6);
//                cp.add(or);
//            }
//        }
        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                IloNumExpr con1 = cp.numExpr();
                con1 = cp.sum(con1, x[i], cp.prod(allItems.get(items.get(i)).getP(), l[i][0]), cp.prod(allItems.get(items.get(i)).getQ(), w[i][0]), cp.prod(allItems.get(items.get(i)).getR(), h[i][0]));
                cp.addLe(con1, cp.sum(x[k], cp.prod(cp.diff(1, a[i][k]), Mx)), "xi_" + i + "," + k);

                IloNumExpr con2 = cp.numExpr();
                con2 = cp.sum(con2, x[k], cp.prod(allItems.get(items.get(k)).getP(), l[k][0]), cp.prod(allItems.get(items.get(k)).getQ(), w[k][0]), cp.prod(allItems.get(items.get(k)).getR(), h[k][0]));
                cp.addLe(con2, cp.sum(x[i], cp.prod(cp.diff(1, b[i][k]), Mx)), "xk_" + i + "," + k);

                IloNumExpr con3 = cp.numExpr();
                con3 = cp.sum(con3, y[i], cp.prod(allItems.get(items.get(i)).getP(), l[i][1]), cp.prod(allItems.get(items.get(i)).getQ(), w[i][1]), cp.prod(allItems.get(items.get(i)).getR(), h[i][1]));
                cp.addLe(con3, cp.sum(y[k], cp.prod(cp.diff(1, c[i][k]), My)), "yi_" + i + "," + k);

                IloNumExpr con4 = cp.numExpr();
                con4 = cp.sum(con4, y[k], cp.prod(allItems.get(items.get(k)).getP(), l[k][1]), cp.prod(allItems.get(items.get(k)).getQ(), w[k][1]), cp.prod(allItems.get(items.get(k)).getR(), h[k][1]));
                cp.addLe(con4, cp.sum(y[i], cp.prod(cp.diff(1, d[i][k]), My)), "yk_" + i + "," + k);

                IloNumExpr con5 = cp.numExpr();
                con5 = cp.sum(con5, z[i], cp.prod(allItems.get(items.get(i)).getP(), l[i][2]), cp.prod(allItems.get(items.get(i)).getQ(), w[i][2]), cp.prod(allItems.get(items.get(i)).getR(), h[i][2]));
                cp.addLe(con5, cp.sum(z[k], cp.prod(cp.diff(1, e[i][k]), Mz)), "zi_" + i + "," + k);

                IloNumExpr con6 = cp.numExpr();
                con6 = cp.sum(con6, z[k], cp.prod(allItems.get(items.get(k)).getP(), l[k][2]), cp.prod(allItems.get(items.get(k)).getQ(), w[k][2]), cp.prod(allItems.get(items.get(k)).getR(), h[k][2]));
                cp.addLe(con6, cp.sum(z[i], cp.prod(cp.diff(1, f[i][k]), Mz)), "zk_" + i + "," + k);
            }
        }


        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                IloNumExpr con7 = cp.numExpr();
                con7 = cp.sum(con7, a[i][k], b[i][k], c[i][k], d[i][k], e[i][k], f[i][k]);
                cp.addGe(con7, 1, "position_"  + i + "," + k);
            }
        }

        for (int i = 0; i < boxCount; i++) {
            cp.addEq(cp.sum(l[i][0], l[i][1], l[i][2]), 1);
            cp.addEq(cp.sum(w[i][0], w[i][1], w[i][2]), 1);
            cp.addEq(cp.sum(h[i][0], h[i][1], h[i][2]), 1);
            cp.addEq(cp.sum(l[i][0], w[i][0], h[i][0]), 1);
            cp.addEq(cp.sum(l[i][1], w[i][1], h[i][1]), 1);
            cp.addEq(cp.sum(l[i][2], w[i][2], h[i][2]), 1);
        }
        for (int i = 0; i < boxCount; i++) {
            IloNumExpr con10 = cp.numExpr();
            con10 = cp.sum(x[i], cp.prod(allItems.get(items.get(i)).getP(), l[i][0]), cp.prod(allItems.get(items.get(i)).getQ(), w[i][0]), cp.prod(allItems.get(items.get(i)).getR(), h[i][0]));
            cp.addLe(con10, cp.diff(allBags.get(bag).getX(),H), "length_" + i + "," + bag);
            IloNumExpr con11 = cp.numExpr();
            con11 = cp.sum(y[i], cp.prod(allItems.get(items.get(i)).getP(), l[i][1]), cp.prod(allItems.get(items.get(i)).getQ(), w[i][1]), cp.prod(allItems.get(items.get(i)).getR(), h[i][1]));
            cp.addLe(con11, cp.diff(allBags.get(bag).getY(),H), "width_" + i + "," + bag);
            IloNumExpr con12 = cp.numExpr();
            con12 = cp.sum(z[i], cp.prod(allItems.get(items.get(i)).getP(), l[i][2]), cp.prod(allItems.get(items.get(i)).getQ(), w[i][2]), cp.prod(allItems.get(items.get(i)).getR(), h[i][2]));
            cp.addLe(con12, H, "height_" + i + "," + bag);
        }

        for (int i = 0; i < boxCount; i++) {
            IloOr orX=cp.or();
            IloOr orY=cp.or();
//            IloOr orZ=cp.or();
            for (int j = 0; j < domain.size(); j++) {
                orX=cp.or(orX,cp.eq(x[i],domain.get(j)));
                orY=cp.or(orY,cp.eq(y[i],domain.get(j)));
//                orZ=cp.or(orZ,cp.eq(z[i],domain.get(j)));
            }
            cp.add(orX);
            cp.add(orY);
//            cp.add(orZ);
        }
        IloOr orH=cp.or();
        for (int i = 0; i < domain.size(); i++) {
            if (2*domain.get(i)>allBags.get(bag).getY()) break;
            if (domain.get(i)*(allBags.get(bag).getX()-domain.get(i))*(allBags.get(bag).getY()-domain.get(i))>totalVolume){
                orH=cp.or(orH,cp.eq(H,domain.get(i)));
            }
        }
        cp.add(orH);
        for (int i = 0; i < boxCount; i++) {
            IloOr orZ=cp.or();
            for (int j = 0; j < domain.size(); j++) {
                if (2*domain.get(j)>allBags.get(bag).getY()) break;
                orZ=cp.or(orZ,cp.eq(z[i],domain.get(j)));
            }
            cp.add(orZ);
        }
        for (int i = 0; i < domain.size(); i++) {
            if (2*domain.get(i)>allBags.get(bag).getY()) break;
            System.out.print(domain.get(i)*(allBags.get(bag).getX()-domain.get(i))*(allBags.get(bag).getY()-domain.get(i))+" ");
        }
        System.out.println();
        System.out.println(totalVolume);
        System.out.println(1);
        System.out.println(orH);
//        assert false;
//        cp.addEq(cp.sum(L, H), allBags.get(bag).getX(), "transformX_" + bag);
//        cp.addEq(cp.sum(W, H), allBags.get(bag).getY(), "transformY_" + bag);
    }
    public void BuildChenKnapsack(ArrayList<Integer> items, int bag) throws IloException {
        int boxCount = items.size();
        ArrayList<Bag> allBags = instance.getAllBags();
        ArrayList<Item> allItems = instance.getAllItems();
        IloNumVar[] x=new IloNumVar[boxCount];
        IloNumVar[] y=new IloNumVar[boxCount];
        IloNumVar[] z=new IloNumVar[boxCount];
        IloNumVar[] s=new IloNumVar[boxCount];
        IloNumVar L, W, H;
        IloNumVar[][] a = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] b = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] c = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] d = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] e = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] f = new IloNumVar[boxCount][boxCount];
        IloNumVar[][] l = new IloNumVar[boxCount][3];
        IloNumVar[][] w = new IloNumVar[boxCount][3];
        IloNumVar[][] h = new IloNumVar[boxCount][3];
        double Mx = allBags.get(bag).getX(), My = allBags.get(bag).getY(), Mz = Math.max(allBags.get(bag).getX(), allBags.get(bag).getY());
        for (int i = 0; i < boxCount; i++) {
            x[i] = sub.numVar(0, Mx, "x_" + i);
            y[i] = sub.numVar(0, My, "y_" + i);
            z[i] = sub.numVar(0, Mz, "z_" + i);
            s[i]=sub.boolVar("s_"+i);
        }
        L = sub.numVar(0, Mx,"L");
        W = sub.numVar(0, My,"W");
        H = sub.numVar(0, Mz,"H");
        for (int i = 0; i < boxCount - 1; i++) {
            for (int j = i + 1; j < boxCount; j++) {
                a[i][j] = sub.intVar(0, 1, "a_" + i + "," + j);
                b[i][j] = sub.intVar(0, 1, "b_" + i + "," + j);
                c[i][j] = sub.intVar(0, 1, "c_" + i + "," + j);
                d[i][j] = sub.intVar(0, 1, "d_" + i + "," + j);
                e[i][j] = sub.intVar(0, 1, "r_" + i + "," + j);
                f[i][j] = sub.intVar(0, 1, "f_" + i + "," + j);
            }
        }
        for (int i = 0; i < boxCount; i++) {
            l[i][0] = sub.intVar(0, 1, "lx_" + i);
            l[i][1] = sub.intVar(0, 1, "ly_" + i);
            l[i][2] = sub.intVar(0, 1, "lz_" + i);
            w[i][0] = sub.intVar(0, 1, "wx_" + i);
            w[i][1] = sub.intVar(0, 1, "wy_" + i);
            w[i][2] = sub.intVar(0, 1, "wz_" + i);
            h[i][0] = sub.intVar(0, 1, "hx_" + i);
            h[i][1] = sub.intVar(0, 1, "hy_" + i);
            h[i][2] = sub.intVar(0, 1, "hz_" + i);
        }
        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                IloNumExpr con1 = sub.numExpr();
                con1 = sub.sum(con1, x[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][0]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][0]), sub.prod(allItems.get(items.get(i)).getR(), h[i][0]));
                sub.addLe(con1, sub.sum(x[k], sub.prod(sub.diff(1, a[i][k]), Mx)), "xi_" + i + "," + k);

                IloNumExpr con2 = sub.numExpr();
                con2 = sub.sum(con2, x[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][0]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][0]), sub.prod(allItems.get(items.get(k)).getR(), h[k][0]));
                sub.addLe(con2, sub.sum(x[i], sub.prod(sub.diff(1, b[i][k]), Mx)), "xk_" + i + "," + k);

                IloNumExpr con3 = sub.numExpr();
                con3 = sub.sum(con3, y[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][1]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][1]), sub.prod(allItems.get(items.get(i)).getR(), h[i][1]));
                sub.addLe(con3, sub.sum(y[k], sub.prod(sub.diff(1, c[i][k]), My)), "yi_" + i + "," + k);

                IloNumExpr con4 = sub.numExpr();
                con4 = sub.sum(con4, y[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][1]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][1]), sub.prod(allItems.get(items.get(k)).getR(), h[k][1]));
                sub.addLe(con4, sub.sum(y[i], sub.prod(sub.diff(1, d[i][k]), My)), "yk_" + i + "," + k);

                IloNumExpr con5 = sub.numExpr();
                con5 = sub.sum(con5, z[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][2]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][2]), sub.prod(allItems.get(items.get(i)).getR(), h[i][2]));
                sub.addLe(con5, sub.sum(z[k], sub.prod(sub.diff(1, e[i][k]), Mz)), "zi_" + i + "," + k);

                IloNumExpr con6 = sub.numExpr();
                con6 = sub.sum(con6, z[k], sub.prod(allItems.get(items.get(k)).getP(), l[k][2]), sub.prod(allItems.get(items.get(k)).getQ(), w[k][2]), sub.prod(allItems.get(items.get(k)).getR(), h[k][2]));
                sub.addLe(con6, sub.sum(z[i], sub.prod(sub.diff(1, f[i][k]), Mz)), "zk_" + i + "," + k);
            }
        }
        for (int i = 0; i < boxCount - 1; i++) {
            for (int k = i + 1; k < boxCount; k++) {
                IloNumExpr con7 = sub.numExpr();
                con7 = sub.sum(con7, a[i][k], b[i][k], c[i][k], d[i][k], e[i][k], f[i][k]);
                con7=sub.sum(con7,1);
                sub.addGe(con7, sub.sum(s[i],s[k]), "position_"  + i + "," + k);
            }
        }
        for (int i = 0; i < boxCount; i++) {
            sub.addEq(sub.sum(l[i][0], l[i][1], l[i][2]), 1);
            sub.addEq(sub.sum(w[i][0], w[i][1], w[i][2]), 1);
            sub.addEq(sub.sum(h[i][0], h[i][1], h[i][2]), 1);
            sub.addEq(sub.sum(l[i][0], w[i][0], h[i][0]), 1);
            sub.addEq(sub.sum(l[i][1], w[i][1], h[i][1]), 1);
            sub.addEq(sub.sum(l[i][2], w[i][2], h[i][2]), 1);
        }
        for (int i = 0; i < boxCount; i++) {
            IloNumExpr con10 = sub.numExpr();
            con10 = sub.sum(x[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][0]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][0]), sub.prod(allItems.get(items.get(i)).getR(), h[i][0]));
            sub.addLe(con10, sub.sum(L,sub.prod(sub.diff(1,s[i]),Mx)), "length_" + i + "," + bag);
            IloNumExpr con11 = sub.numExpr();
            con11 = sub.sum(y[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][1]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][1]), sub.prod(allItems.get(items.get(i)).getR(), h[i][1]));
            sub.addLe(con11, sub.sum(W,sub.prod(sub.diff(1,s[i]),My)), "width_" + i + "," + bag);
            IloNumExpr con12 = sub.numExpr();
            con12 = sub.sum(z[i], sub.prod(allItems.get(items.get(i)).getP(), l[i][2]), sub.prod(allItems.get(items.get(i)).getQ(), w[i][2]), sub.prod(allItems.get(items.get(i)).getR(), h[i][2]));
            sub.addLe(con12, sub.sum(H,sub.prod(sub.diff(1,s[i]),Mz)), "height_" + i + "," + bag);
        }
        sub.addLe(sub.sum(L, H), allBags.get(bag).getX(), "transformX_" + bag);
        sub.addLe(sub.sum(W, H), allBags.get(bag).getY(), "transformY_" + bag);
        IloLinearNumExpr obj=sub.linearNumExpr();
        H.setUB(allBags.get(bag).getY()/2);
        for (int i = 0; i < boxCount; i++) {
            obj.addTerm(allItems.get(items.get(i)).getVolume(),s[i]);
        }
        sub.addMaximize(obj);
    }
    public void solveLP() throws IloException {
        sub.solve();
    }
    public void solveCP() throws IloException {
        cp.solve();
    }
//    public IloAlgorithm.Status getCPStatus() throws IloException {
//        return cp.getStatus();
//    }

    public IloCplex.Status getLPStatus() throws IloException {
        return sub.getStatus();
    }

    public void end() throws IloException {
        sub.end();
    }

    public void clearLP() throws IloException {
        sub.endModel();
    }

    public void exportLP(String path) throws IloException {
        sub.exportModel(path);
    }
    public void exportCP(String path) throws IloException {
        cp.exportModel(path);
    }
    public double getSubObj() throws IloException {
        return sub.getObjValue();
    }
    public IloAlgorithm.Status getCPStatus(){
        return cp.getStatus();
    }
    public void clearCP() throws IloException {
        cp.end();
        cp=new IloCP();
        System.out.println(cp);
    }
//    public IloRange generateNoGoodCuts{
//
//        return
//    }
}
