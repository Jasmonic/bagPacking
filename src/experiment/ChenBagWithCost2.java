package experiment;


import baseObject.*;

import java.util.*;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-08-2022-08-31
 * @Description: BPP_Model
 * @version=1.0
 */

public class ChenBagWithCost2 {
    public static ArrayList<Bag> bags = new ArrayList<>();
    public static ArrayList<Item> items = new ArrayList<>();

//    public static void Initialze(String url, String suanli) throws IOException {
//        int pro_num = Integer.parseInt(suanli);
//        BufferedReader br = new BufferedReader(new java.io.FileReader(
//                "./data/br/" + url + ".txt"));
//        String line;
//        String[] splitLine;
//        Box tempBox;
//        int tol_pro = Integer.parseInt(br.readLine().trim());
//        if (tol_pro < pro_num || pro_num < 1) {
//            System.out.println("算例编号最小值为1，最大值为" + tol_pro);
//        } else {
//            while ((line = br.readLine()) != null) {
//                splitLine = line.split("\\s+");
//                if (splitLine.length == 3 && splitLine[1].equals(String.valueOf(pro_num))) {
////                    splitLine = br.readLine().split("\\s+");
//                    int bag_in = Integer.parseInt(br.readLine().trim());
//                    System.out.println(bag_in);
//                    for (int i = 0; i < bag_in; i++) {
//                        splitLine = br.readLine().split("\\s+");
//                        Bag tempBag = new Bag(Integer.parseInt(splitLine[1]) - 1, Integer.parseInt(splitLine[2]), Integer.parseInt(splitLine[3]), Double.parseDouble(splitLine[4]), Integer.parseInt(splitLine[5]));
//                        System.out.println(tempBag.cost);
//                        bags.add(tempBag);
//                    }
//                    int box_in = Integer.parseInt(br.readLine().trim());
//                    for (int i = 0; i < box_in; i++) {
//                        splitLine = br.readLine().split("\\s+");
//                        tempBox = new Box(Integer.parseInt(splitLine[1]) - 1, Integer.parseInt(splitLine[2]), Integer.parseInt(splitLine[3]),
//                                Integer.parseInt(splitLine[4]), Integer.parseInt(splitLine[5]));
//                        System.out.println(tempBox);
//                        boxes.add(tempBox);
//                    }
////                    System.out.println(bags);
////                    System.out.println(boxes);
//                    break;
//                }
//            }
//        }
//        int boxCount = 0;
//        for (Box box : boxes) {
//            boxCount += box.num;
//        }
//        for (Bag bag : bags) {
//            bag.num = boxCount;
//        }
//    }
//
//    public static void buildModel(String url, String suanli, String method) throws IloException, FileNotFoundException {
//        IloCplex cplex = new IloCplex();
//        ArrayList<Bag> allBags = new ArrayList<>();
//        ArrayList<Box> allBox = new ArrayList<>();
//        class boxComparator implements Comparator<Box> {
//            public int compare(Box o1, Box o2) {
//                if (o1.r < o2.r) return 1;
//                if (o1.r > o2.r) return -1;
//                return 0;
//            }
//        }
//        Comparator<Box> cmp = new boxComparator();
////        boxes.sort(cmp);
//        for (Bag bag : bags) {
//            System.out.println(bag.toString());
//            for (int i = 0; i < bag.num; i++) {
//                Bag tempBag = new Bag(bag.type, bag.X, bag.Y, bag.maxVolume, bag.cost, 1);
////                System.out.println(tempBag.cost);
//                allBags.add(tempBag);
//            }
//        }
//
//        for (Box box : boxes) {
//            for (int i = 0; i < box.num; i++) {
//                Box tempBox = new Box(box.type, box.p, box.q, box.r, 1);
//                allBox.add(tempBox);
//            }
//        }
//        int bagCount = allBags.size(), boxCount = allBox.size();
//        System.out.println(bagCount + "*********");
//        System.out.println(allBags);
//        System.out.println(allBox);
//        IloNumVar[] n = new IloNumVar[bagCount];
//        IloNumVar[] x = new IloNumVar[boxCount];
//        IloNumVar[] y = new IloNumVar[boxCount];
//        IloNumVar[] z = new IloNumVar[boxCount];
//        IloNumVar[][] l = new IloNumVar[boxCount][3];
//        IloNumVar[][] w = new IloNumVar[boxCount][3];
//        IloNumVar[][] h = new IloNumVar[boxCount][3];
//        IloNumVar[][] s = new IloNumVar[boxCount][bagCount];
//        IloNumVar[][] a = new IloNumVar[boxCount][boxCount];
//        IloNumVar[][] b = new IloNumVar[boxCount][boxCount];
//        IloNumVar[][] c = new IloNumVar[boxCount][boxCount];
//        IloNumVar[][] d = new IloNumVar[boxCount][boxCount];
//        IloNumVar[][] e = new IloNumVar[boxCount][boxCount];
//        IloNumVar[][] f = new IloNumVar[boxCount][boxCount];
//        IloNumVar[][] q = new IloNumVar[boxCount][bagCount];
//        IloNumVar[] L = new IloNumVar[bagCount];
//        IloNumVar[] W = new IloNumVar[bagCount];
//        IloNumVar[] H = new IloNumVar[bagCount];
//        double M1 = 0, M2 = 0, M3 = 0, M4 = 0, M5 = 0, M6 = 0, M9 = boxCount + 1, M10 = 0, M11 = 0, M12 = 0;
//        double maxDim = 0;
////        M1 = M2 = M3 = M4 = M5 = M6 = M9 = M10 = M11 = M12 = 10000;
//        for (Bag bag : bags) {
//            M1 = M2 = M10 = Math.max(M10, bag.X);
//            M3 = M4 = M11 = Math.max(M11, bag.Y);
//            M5 = M6 = M12 = Math.max(Math.max(M12, M10), M11);
//        }
//        System.out.println(M1 + " " + M2 + " " + M3 + " " + M4 + " " + M5 + " " + M6 + " " + M9 + " " + M10 + " " + M11 + " " + M12);
//
////        cplex.setParam(	IloCplex.Param.Preprocessing.Presolve,false);
//        for (int i = 0; i < bagCount; i++) {
//            n[i] = cplex.intVar(0, 1, "n_" + i);
//            double dim = Math.max(allBags.get(i).X, allBags.get(i).Y);
//            maxDim = Math.max(maxDim, dim);
//            L[i] = cplex.numVar(0, dim, "L_" + i);
//            W[i] = cplex.numVar(0, dim, "W_" + i);
//            H[i] = cplex.numVar(0, dim, "H_" + i);
//        }
//        for (int i = 0; i < boxCount; i++) {
//            x[i] = cplex.numVar(0, maxDim, "x_" + i);
//            y[i] = cplex.numVar(0, maxDim, "y_" + i);
//            z[i] = cplex.numVar(0, maxDim, "z_" + i);
//        }
//        for (int i = 0; i < boxCount; i++) {
//            l[i][0] = cplex.intVar(0, 1, "lx_" + i);
//            l[i][1] = cplex.intVar(0, 1, "ly_" + i);
//            l[i][2] = cplex.intVar(0, 1, "lz_" + i);
//            w[i][0] = cplex.intVar(0, 1, "wx_" + i);
//            w[i][1] = cplex.intVar(0, 1, "wy_" + i);
//            w[i][2] = cplex.intVar(0, 1, "wz_" + i);
//            h[i][0] = cplex.intVar(0, 1, "hx_" + i);
//            h[i][1] = cplex.intVar(0, 1, "hy_" + i);
//            h[i][2] = cplex.intVar(0, 1, "hz_" + i);
//        }
//        for (int i = 0; i < boxCount; i++) {
//            for (int j = 0; j < bagCount; j++) {
//                s[i][j] = cplex.intVar(0, 1, "s_" + i + "," + j);
//            }
//        }
//        for (int i = 0; i < boxCount; i++) {
//            for (int j = i + 1; j < boxCount; j++) {
//                a[i][j] = cplex.intVar(0, 1, "a_" + i + "," + j);
//                b[i][j] = cplex.intVar(0, 1, "b_" + i + "," + j);
//                c[i][j] = cplex.intVar(0, 1, "c_" + i + "," + j);
//                d[i][j] = cplex.intVar(0, 1, "d_" + i + "," + j);
//                e[i][j] = cplex.intVar(0, 1, "r_" + i + "," + j);
//                f[i][j] = cplex.intVar(0, 1, "f_" + i + "," + j);
//            }
//        }
//        //约束
//        for (int i = 0; i < boxCount; i++) {
//            for (int k = i + 1; k < boxCount; k++) {
//                IloNumExpr con1 = cplex.numExpr();
//                con1 = cplex.sum(con1, x[i], cplex.prod(allBox.get(i).p/M1, l[i][0]), cplex.prod(allBox.get(i).q/M1, w[i][0]), cplex.prod(allBox.get(i).r/M1, h[i][0]));
//                cplex.addLe(con1, cplex.sum(x[k], cplex.prod(cplex.diff(1, a[i][k]), 1)), "xi_" + i + "," + k);
//
//                IloNumExpr con2 = cplex.numExpr();
//                con2 = cplex.sum(con2, x[k], cplex.prod(allBox.get(k).p/M1, l[k][0]), cplex.prod(allBox.get(k).q/M1, w[k][0]), cplex.prod(allBox.get(k).r/M1, h[k][0]));
//                cplex.addLe(con2, cplex.sum(x[i], cplex.prod(cplex.diff(1, b[i][k]), 1)), "xk_" + i + "," + k);
//
//                IloNumExpr con3 = cplex.numExpr();
//                con3 = cplex.sum(con3, y[i], cplex.prod(allBox.get(i).p/M3, l[i][1]), cplex.prod(allBox.get(i).q/M3, w[i][1]), cplex.prod(allBox.get(i).r/M3, h[i][1]));
//                cplex.addLe(con3, cplex.sum(y[k], cplex.prod(cplex.diff(1, c[i][k]), 1)), "yi_" + i + "," + k);
//
//                IloNumExpr con4 = cplex.numExpr();
//                con4 = cplex.sum(con4, y[k], cplex.prod(allBox.get(k).p/M3, l[k][1]), cplex.prod(allBox.get(k).q/M3, w[k][1]), cplex.prod(allBox.get(k).r/M3, h[k][1]));
//                cplex.addLe(con4, cplex.sum(y[i], cplex.prod(cplex.diff(1, d[i][k]), 1)), "yk_" + i + "," + k);
//
//                IloNumExpr con5 = cplex.numExpr();
//                con5 = cplex.sum(con5, z[i], cplex.prod(allBox.get(i).p/M5, l[i][2]), cplex.prod(allBox.get(i).q/M5, w[i][2]), cplex.prod(allBox.get(i).r/M5, h[i][2]));
//                cplex.addLe(con5, cplex.sum(z[k], cplex.prod(cplex.diff(1, e[i][k]), 1)), "zi_" + i + "," + k);
//
//                IloNumExpr con6 = cplex.numExpr();
//                con6 = cplex.sum(con6, z[k], cplex.prod(allBox.get(k).p/M5, l[k][2]), cplex.prod(allBox.get(k).q/M5, w[k][2]), cplex.prod(allBox.get(k).r/M5, h[k][2]));
//                cplex.addLe(con6, cplex.sum(z[i], cplex.prod(cplex.diff(1, f[i][k]), 1)), "zk_" + i + "," + k);
//            }
//        }
//
//
//        for (int i = 0; i < boxCount; i++) {
//            for (int k = i + 1; k < boxCount; k++) {
//                for (int j = 0; j < bagCount; j++) {
//                    IloNumExpr con7 = cplex.numExpr();
//                    con7 = cplex.sum(con7, a[i][k], b[i][k], c[i][k], d[i][k], e[i][k], f[i][k]);
//                    con7 = cplex.sum(con7, 1);
//                    cplex.addGe(con7, cplex.sum(s[i][j], s[k][j]), "position_" + "," + i + "," + j + "," + k);
//                }
//            }
//        }
//        for (int i = 0; i < boxCount; i++) {
//            IloLinearNumExpr con8 = cplex.linearNumExpr();
//            for (int j = 0; j < bagCount; j++) {
//                con8.addTerm(1, s[i][j]);
//            }
//            cplex.addEq(con8, 1, "bag_and_box" + i);
//        }
//        for (int j = 0; j < bagCount; j++) {
//            IloLinearNumExpr con9 = cplex.linearNumExpr();
//            for (int i = 0; i < boxCount; i++) {
//                con9.addTerm(1, s[i][j]);
//            }
//            cplex.addLe(con9, cplex.prod(M9, n[j]), "bagUsed_" + j);
//        }
//        for (int i = 0; i < boxCount; i++) {
//            for (int j = 0; j < bagCount; j++) {
//                IloNumExpr con10 = cplex.numExpr();
//                con10 = cplex.sum(x[i], cplex.prod(allBox.get(i).p/M1, l[i][0]), cplex.prod(allBox.get(i).q/M1, w[i][0]), cplex.prod(allBox.get(i).r/M1, h[i][0]));
//                cplex.addLe(con10, cplex.sum(cplex.prod(L[j],1.0/M1), cplex.prod(cplex.diff(1, s[i][j]), 1)), "length_" + i + "," + j);
//
//                IloNumExpr con11 = cplex.numExpr();
//                con11 = cplex.sum(y[i], cplex.prod(allBox.get(i).p/M3, l[i][1]), cplex.prod(allBox.get(i).q/M3, w[i][1]), cplex.prod(allBox.get(i).r/M3, h[i][1]));
//                cplex.addLe(con11, cplex.sum(cplex.prod(W[j],1.0/M3), cplex.prod(cplex.diff(1, s[i][j]), 1)), "width_" + i + "," + j);
//                IloNumExpr con12 = cplex.numExpr();
//                con12 = cplex.sum(z[i], cplex.prod(allBox.get(i).p/M5, l[i][2]), cplex.prod(allBox.get(i).q/M5, w[i][2]), cplex.prod(allBox.get(i).r/M5, h[i][2]));
//                cplex.addLe(con12, cplex.sum(cplex.prod(H[j],1.0/M5), cplex.prod(cplex.diff(1, s[i][j]), 1)), "height_" + i + "," + j);
//            }
//        }
//        for (int i = 0; i < boxCount; i++) {
//            cplex.addEq(cplex.sum(l[i][0], l[i][1], l[i][2]), 1);
//            cplex.addEq(cplex.sum(w[i][0], w[i][1], w[i][2]), 1);
//            cplex.addEq(cplex.sum(h[i][0], h[i][1], h[i][2]), 1);
//            cplex.addEq(cplex.sum(l[i][0], w[i][0], h[i][0]), 1);
//            cplex.addEq(cplex.sum(l[i][1], w[i][1], h[i][1]), 1);
//            cplex.addEq(cplex.sum(l[i][2], w[i][2], h[i][2]), 1);
//        }
//        //袋子变形约束
//        for (int i = 0; i < bagCount; i++) {
//            cplex.addLe(cplex.sum(L[i], H[i]), allBags.get(i).X, "transformX_" + i);
//            cplex.addLe(cplex.sum(W[i], H[i]), allBags.get(i).Y, "transformY_" + i);
//        }
//        //袋子对称cut
//        for (int j = 0; j < bagCount - 1; j++) {
//            if (allBags.get(j).type == allBags.get(j + 1).type) {
//                cplex.addGe(n[j], n[j + 1], "bagCut" + j + "," + (j + 1));
//            }
//        }
//        IloLinearNumExpr obj = cplex.linearNumExpr();
//        for (int j = 0; j < bagCount; j++) {
//            obj.addTerm(allBags.get(j).cost, n[j]);
//        }
//        cplex.addMinimize(obj);
//        //Advanced cut
//        if (method.contains("a")) {
//            for (int i = 0; i < boxCount; i++) {
//                for (int k = i + 1; k < boxCount; k++) {
//                    if (allBox.get(i).type == allBox.get(k).type) {
//                        b[i][k].setUB(0);
//                        d[i][k].setUB(0);
//                        f[i][k].setUB(0);
//                        System.out.println("对称!!!!!!!!!!!!!!!" + i + " " + k);
//                    }
//                }
//            }
//        }
//        //袋子和同类物品cut
//        if (method.contains("b")) {
//            for (int i = 0; i < boxCount - 1; i++) {
//                if (allBox.get(i).type == allBox.get(i + 1).type) {
//                    for (int j = 0; j < bagCount; j++) {
//                        IloLinearNumExpr con13 = cplex.linearNumExpr();
//                        for (int r = 0; r <= j; r++) {
//                            con13.addTerm(1, s[i][r]);
//                        }
//                        cplex.addGe(con13, s[i + 1][j], "boxCut" + i + "," + j);
//                    }
//                }
//            }
//        }
//
//        if (method.contains("c")) {
//            int cur = 0;
//            for (int i = 0; i < boxes.size(); i++) {
//                double totalVolume = boxes.get(i).volume * boxes.get(i).num;
//                for (int j = 0; j < allBags.size(); j++) {
//                    if (totalVolume > allBags.get(j).maxVolume) {
//                        IloLinearNumExpr cutC = cplex.linearNumExpr();
//                        for (int k = cur; k < cur + boxes.get(i).num; k++) {
//                            cutC.addTerm(1, s[k][j]);
//                        }
//                        System.out.println(i + " " + j + " " + Math.floor(allBags.get(j).maxVolume / boxes.get(i).volume)+"CCCCCCCCCCCCCCCCC");
////                        System.out.println(Math.floor(allBags.get(j).maxVolume/boxes.get(i).volume));
//
////                        cplex.addLe(cutC,Math.floor(allBags.get(j).maxVolume/boxes.get(i).volume),"CutC_"+i+"_"+j);
//                        if (Math.floor(allBags.get(j).maxVolume / boxes.get(i).volume) == 0) {
//                            for (int k = cur; k < cur + boxes.get(i).num; k++) {
//                                s[k][j].setUB(0);
//                            }
//                        } else {
//                            cplex.addLe(cutC, cplex.prod(Math.floor(allBags.get(j).maxVolume / boxes.get(i).volume), n[j]), "CutC_" + i + "_" + j);
//                        }
//
//                    }
//                }
//                System.out.print(cur + ",");
//                cur += boxes.get(i).num;
//                System.out.println(cur);
//            }
//        }
//
//        if (method.contains("d")) {
//            for (int j = 0; j < bagCount; j++) {
//                IloLinearNumExpr cutD = cplex.linearNumExpr();
//                for (int i = 0; i < boxCount; i++) {
//                    cutD.addTerm(allBox.get(i).volume, s[i][j]);
//                }
//                cplex.addLe(cutD, cplex.prod(allBags.get(j).maxVolume, n[j]));
//            }
//        }
//        if (method.contains("f")) {
//            for (int j = 0; j < bagCount; j++) {
//                IloNumExpr con11 = cplex.numExpr();
//                con11 = cplex.sum(con11, x[1], cplex.prod(0.5 * allBox.get(1).p/M1, l[1][0]), cplex.prod(0.5 * allBox.get(1).q/M1, w[1][0]), cplex.prod(0.5 * allBox.get(1).r/M1, h[1][0]));
//                cplex.addLe(con11,cplex.sum(cplex.prod(L[j],1.0/M1),cplex.prod(1,cplex.diff(1,s[1][j]))));
//
//                IloNumExpr con12 = cplex.numExpr();
//                con12 = cplex.sum(con12, y[1], cplex.prod(0.5 * allBox.get(1).p/M3, l[1][0]), cplex.prod(0.5 * allBox.get(1).q/M3, w[1][0]), cplex.prod(0.5 * allBox.get(1).r/M3, h[1][0]));
//                cplex.addLe(con12,cplex.sum(cplex.prod(W[j],1.0/M3),cplex.prod(1,cplex.diff(1,s[1][j]))));
//
//                IloNumExpr con13 = cplex.numExpr();
//                con13 = cplex.sum(con13, z[1], cplex.prod(0.5 * allBox.get(1).p/M5, l[1][0]), cplex.prod(0.5 * allBox.get(1).q/M5, w[1][0]), cplex.prod(0.5 * allBox.get(1).r/M5, h[1][0]));
//                cplex.addLe(con13,cplex.sum(cplex.prod(H[j],1.0/M5),cplex.prod(1,cplex.diff(1,s[1][j]))));
//            }
//        }
//        if (method.contains("e")) {
//            cplex.exportModel("./lp/Bag2" + url + "_" + suanli + "_" + method + ".lp");
//        }
//
////        System.exit(0);
////        cplex.setParam(IloCplex.Param.MIP.Tolerances.MIPGap,0.38);
////        cplex.setParam(IloCplex.Param.Preprocessing.Presolve,false);
////        cplex.setParam(	IloCplex.Param.Threads,1);
////        cplex.setParam(IloCplex.Param.MIP.Display,5);
//
//        long start = System.currentTimeMillis();
//        boolean success = cplex.solve();
//        long end = System.currentTimeMillis();
//
//
//        System.out.println("cplex solve time:" + (end - start) + "ms");
//        System.out.println("cplex solve time:" + (end - start) * 1.0 / 1000 + "s");
//        if (success) {
//            System.out.println(cplex.getObjValue());
//            System.out.println("n:" + Arrays.toString(cplex.getValues(n)));
//            double[] xx, yy, zz, lx, ly, lz, LL, WW, HH, length, width, height;
//            int[] ss = new int[boxCount];
//            xx = cplex.getValues(x);
//            yy = cplex.getValues(y);
//            zz = cplex.getValues(z);
//            lx = new double[boxCount];
//            ly = new double[boxCount];
//            lz = new double[boxCount];
//            length = new double[boxCount];
//            width = new double[boxCount];
//            height = new double[boxCount];
//            LL = cplex.getValues(L);
//            WW = cplex.getValues(W);
//            HH = cplex.getValues(H);
//            int[] bagType = new int[boxCount];
//            for (int i = 0; i < boxCount; i++) {
//                for (int j = 0; j < bagCount; j++) {
//                    if (cplex.getValue(s[i][j]) > 0.9) {
//                        ss[i] = allBags.get(j).type;
//                        bagType[i] = j;
//                    }
//                }
//            }
//            for (int i = 0; i < boxCount; i++) {
//                length[i] = LL[bagType[i]];
//                width[i] = WW[bagType[i]];
//                height[i] = HH[bagType[i]];
//            }
//            for (int i = 0; i < boxCount; i++) {
//                double[] ll = cplex.getValues(l[i]);
//                double[] ww = cplex.getValues(w[i]);
//                double[] hh = cplex.getValues(h[i]);
//                System.out.print("l:" + Arrays.toString(ll));
//                System.out.print("w:" + Arrays.toString(ww));
//                System.out.print("h:" + Arrays.toString(hh));
//                System.out.println();
//                if (ll[0] > 0.9) {
//                    lx[i] = allBox.get(i).p;
//                } else {
//                    if (ww[0] > 0.9) {
//                        lx[i] = allBox.get(i).q;
//                    } else {
//                        lx[i] = allBox.get(i).r;
//                    }
//                }
//                if (ll[1] > 0.9) {
//                    ly[i] = allBox.get(i).p;
//                } else {
//                    if (ww[1] > 0.9) {
//                        ly[i] = allBox.get(i).q;
//                    } else {
//                        ly[i] = allBox.get(i).r;
//                    }
//                }
//                if (ll[2] > 0.9) {
//                    lz[i] = allBox.get(i).p;
//                } else {
//                    if (ww[2] > 0.9) {
//                        lz[i] = allBox.get(i).q;
//                    } else {
//                        lz[i] = allBox.get(i).r;
//                    }
//                }
//
//            }
//            System.out.println("No.:" + Arrays.toString(bagType));
//            System.out.println("type:" + Arrays.toString(ss));
//            System.out.println("L:" + Arrays.toString(length));
//            System.out.println("W:" + Arrays.toString(width));
//            System.out.println("H:" + Arrays.toString(height));
//            System.out.println("x:" + Arrays.toString(xx));
//            System.out.println("y:" + Arrays.toString(yy));
//            System.out.println("z:" + Arrays.toString(zz));
//            System.out.println("lx:" + Arrays.toString(lx));
//            System.out.println("ly:" + Arrays.toString(ly));
//            System.out.println("lz:" + Arrays.toString(lz));
//            ArrayList<Integer> ansBags = new ArrayList<>();
//            ArrayList<Bag> ansBagsInfo = new ArrayList<>();
//            ArrayList<ArrayList<Position>> allPs = new ArrayList<>();
//            Map<Integer, Integer> map = new HashMap<>();
//            for (int i = 0; i < boxCount; i++) {
//                boolean in = false;
//                for (Integer type : ansBags) {
//                    if (type == bagType[i]) {
//                        in = true;
//                        break;
//                    }
//                }
//                if (!in) {
//                    ansBags.add(bagType[i]);
//                    ArrayList<Position> ps = new ArrayList<>();
//                    allPs.add(ps);
//                    Bag tempBag = new Bag(ss[i], length[i], width[i], height[i], bags.get(ss[i]).cost);
//                    ansBagsInfo.add(tempBag);
//                    map.put(bagType[i], allPs.size() - 1);
//                }
//            }
//            System.out.println(map);
//            //TODO
//            for (int i = 0; i < boxCount; i++) {
//                Position position = new Position(allBox.get(i).type, xx[i]*M1, yy[i]*M3, zz[i]*M5, lx[i], ly[i], lz[i]);
//                allPs.get(map.get(bagType[i])).add(position);
//            }
//            System.out.println(ansBagsInfo);
//            for (ArrayList<Position> ps : allPs) {
//                System.out.println(ps);
//            }
//            assert (ansBagsInfo.size() == allPs.size()) : "袋子数量！=allPs数量";
//            for (ArrayList<Position> ps : allPs) {
//                for (Position p1 : ps) {
//                    for (Position p2 : ps) {
//                        if (p1 != p2) {
//                            assert (!check.isbox_collide(p1, p2)) : p1.toString() + "||" + p2.toString();
//                        }
//                    }
//                }
//            }
//            System.out.println("————————————————————————————————ssss");
//            for (int i = 0; i < boxCount; i++) {
//                for (int j = 0; j < bagCount; j++) {
//                    if (cplex.getValue(s[i][j]) > 0.9) {
//                        System.out.println("s_" + i + "," + j + "=1");
//                    }
//                }
//            }
////            PrintStream out = System.out;
////            PrintStream prs = new PrintStream("result\\result3_" + url + "_" + suanli + "_" + method + ".txt");
////            System.setOut(prs);
////            System.out.println(ansBagsInfo.size());
////            for (int i = 0; i < ansBagsInfo.size(); i++) {
////                System.out.println("use " + i + " bag");
////                System.out.println(ansBagsInfo.get(i).type + " " + ansBagsInfo.get(i).l + " " + ansBagsInfo.get(i).w + " " + ansBagsInfo.get(i).h + " " + ansBagsInfo.get(i).cost);
////                System.out.println(allPs.get(i).size());
////                for (Position position : allPs.get(i)) {
////                    System.out.println(position.type + " " + position.x + " " + position.y + " " + position.z + " " + position.lx + " " + position.ly + " " + position.lz);
////                }
////            }
////            prs.close();
////
////            System.setOut(out);
//        } else {
//            System.out.println(cplex.getStatus());
//        }
//        System.out.println("cplex solve time:" + (end - start) + "ms");
//        System.out.println("cplex solve time:" + (end - start) * 1.0 / 1000 + "s");
//        System.out.println("Node number:" + cplex.getNnodes());
//    }
//
//    public static void main(String[] args) throws IOException, IloException {
//        Initialze(args[0], args[1]);
////        System.exit(0);
//        String method;
//        if (args.length < 3) {
//            method = "0";
//        } else {
//            method = args[2];
//        }
//        System.out.println(bags.toString());
//        buildModel(args[0], args[1], method);
////        IloCplex cplex=new IloCplex();
////        cplex.importModel("./lp/Bagali_9_abcdetttt.lp");
////        cplex.solve();
////        System.out.println(cplex.getNnodes());
//    }
}
