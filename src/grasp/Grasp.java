package grasp;

import baseObject.*;
import benders.Instance;
import com.sun.xml.internal.ws.util.Pool;
import comparator.GraspItemComparator;
import ilog.cplex.IloCplex;
import scorer.EnvelopeUtilization;
import scorer.PositionScorer;
import singleBag.ExtremePoint;
import singleBag.ExtremePointSet;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @Author: Feng Jixuan
 * @Date: 2023-03-2023-03-02
 * @Description: BPP_Model
 * @version=1.0
 */
public class Grasp {
    public static Position bestFitPosition(Item toPackItem, ExtremePointSet epSet, ArrayList<Position> packedPositions, PositionScorer scorer) {
        //在6个转向和所有极端点中找best fit
        double minScore = Double.MAX_VALUE;//score是反向指标
        Position bestPosition = null;
        for (int j = 1; j <= 6; j++) {
            //尝试六个转向
            Item tryItem = toPackItem.copyOfOrientation(j);
            for (ExtremePoint extremePoint : epSet.getExtremePoints()) {
                //尝试所有极端点
                Position packPosition = new Position(toPackItem.getType(), toPackItem.getId(), extremePoint.getX(), extremePoint.getY(), extremePoint.getZ(),
                        tryItem.getP(), tryItem.getQ(), tryItem.getR());
                if (!check.isPositionCollideWithPacked(packPosition, packedPositions)
                        && !check.isPositionOutOfBag(epSet, packPosition)) {
                    if (scorer.calculateScore(epSet, packPosition) < minScore) {
                        bestPosition = packPosition;
                        minScore = scorer.calculateScore(epSet, packPosition);
                    }
                }
            }
        }
        return bestPosition;
    }

    public static boolean improveSmallerBag(Instance instance, Solution solution, int i) {
        if (solution.getUsedBags().get(i).getType() == 0) {
            //最小的袋子不能再优化
            return false;
        }
        int count = 0;
        boolean canImprove = true;
        PositionScorer scorer = new EnvelopeUtilization();
        double totalVolume=0;
        for (Item item : solution.getPackedItemsList().get(i)) {
            totalVolume += item.getVolume();
        }
        while (canImprove) {
            canImprove = false;
            if (solution.getUsedBags().get(i).getType() == 0) {
                break;
            }
            //依次尝试小一号的袋子
            Bag chooseBag = instance.getBags().get(solution.getUsedBags().get(i).getType() - 1);
            if (totalVolume>chooseBag.getMaxVolume()) break;
            ArrayList<Item> itemList = new ArrayList<>();
            itemList.addAll(solution.getPackedItemsList().get(i));
            itemList.sort(new GraspItemComparator(instance));

            ExtremePointSet epSet = new ExtremePointSet(chooseBag);
            ArrayList<Position> packedPositions = new ArrayList<>();
            epSet.getExtremePoints().add(new ExtremePoint(0, 0, 0));
            ArrayList<Item> packedItems = new ArrayList<>();
            boolean canPack = false;
            while (!itemList.isEmpty()) {
                canPack = false;
                Item toPackItem = null;
                //找到第一个能装的物品
                for (Item tryPackItem : itemList) {
                    if (epSet.canPackItemOfOrientation(tryPackItem, packedPositions)) {
                        toPackItem = tryPackItem;
                        canPack = true;
                        break;
                    }
                }
                //没有能装的就break
                if (canPack) {
                    itemList.remove(toPackItem);
                } else {
                    break;
                }
                Position bestPosition = bestFitPosition(toPackItem, epSet, packedPositions, scorer);
                //更新
                epSet.updateEPSetAfterPack(packedPositions, bestPosition);
                packedPositions.add(bestPosition);
            }
            if (!canPack && !itemList.isEmpty()) {
                canImprove = false;
            } else {
//                System.out.println("improve small");
                canImprove = true;
                count++;
                solution.getPackedPositionsList().set(i, packedPositions);
                solution.getUsedBags().set(i, chooseBag);
            }
        }
        if (count == 0) return false;
        return true;

    }

    public static boolean mergeTwoBagsIntoOne(Instance instance, Solution solution, int i, int j) {
        TwoTuple tp = new TwoTuple(solution.getUsedBags().get(i), solution.getUsedBags().get(j));
        if (!instance.getMergeBagMap().containsKey(tp)) {
            return false;
        }
        Bag mergeBag = instance.getMergeBagMap().get(tp);
        double totalVolume = 0;
        for (Item item : solution.getPackedItemsList().get(i)) {
            totalVolume += item.getVolume();
        }
        for (Item item : solution.getPackedItemsList().get(j)) {
            totalVolume += item.getVolume();
        }
        if (totalVolume > mergeBag.getMaxVolume()) {
            return false;
        }
        boolean canMerge = true;
        PositionScorer scorer = new EnvelopeUtilization();
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.addAll(solution.getPackedItemsList().get(i));
        itemList.addAll(solution.getPackedItemsList().get(j));
        itemList.sort(new GraspItemComparator(instance));

        ExtremePointSet epSet = new ExtremePointSet(mergeBag);
        ArrayList<Position> packedPositions = new ArrayList<>();
        epSet.getExtremePoints().add(new ExtremePoint(0, 0, 0));
        ArrayList<Item> packedItems = new ArrayList<>();
        boolean canPack = false;
        while (!itemList.isEmpty()) {
            canPack = false;
            Item toPackItem = null;
            //找到第一个能装的物品
            for (Item tryPackItem : itemList) {
                if (epSet.canPackItemOfOrientation(tryPackItem, packedPositions)) {
                    toPackItem = tryPackItem;
                    canPack = true;
                    break;
                }
            }
            //没有能装的就break
            if (canPack) {
                itemList.remove(toPackItem);
            } else {
                break;
            }
            Position bestPosition = bestFitPosition(toPackItem, epSet, packedPositions, scorer);
            //更新
            packedItems.add(toPackItem);
            epSet.updateEPSetAfterPack(packedPositions, bestPosition);
            packedPositions.add(bestPosition);
        }
        if (!canPack) {
            return false;
        } else {
            //将两个pattern的信息合成一个
            if (i > j) {
                int t = i; i = j; j = t;
            }
            solution.removePattern(j);
            solution.removePattern(i);
            solution.addApattern(mergeBag,packedItems,packedPositions);
            return true;
        }

    }

    public static Bag randomModifyBag(ArrayList<Item> itemList, Instance instance, double bagCostSum) {
        Bag chooseBag = null;
        Random rd = new Random();
        //轮盘赌选择bag
        double r = rd.nextDouble();
        double cumulateBagCost = 0;
        for (Bag bag : instance.getBags()) {
            cumulateBagCost += bag.getCost();
            if (r < cumulateBagCost / bagCostSum) {
                chooseBag = bag;
                break;
            }
        }
        System.out.println(chooseBag);
        //如果当前bag无法装下第一个物品，那就选择第一个能装下该物品的bag，确保有物品能放下
        if (itemList.get(0).getLengthOf(0) + itemList.get(0).getLengthOf(2) > chooseBag.getX()
                || itemList.get(0).getLengthOf(1) + itemList.get(0).getLengthOf(2) > chooseBag.getY()) {
            for (Bag bag : instance.getBags()) {
                if (itemList.get(0).getLengthOf(0) + itemList.get(0).getLengthOf(2) <= bag.getX()
                        && itemList.get(0).getLengthOf(1) + itemList.get(0).getLengthOf(2) <= bag.getY()) {
                    chooseBag = bag;
                    break;
                }
            }
        }
        return chooseBag;
    }

    public static void pack(Instance instance, String suanli) throws IOException {
        long time1 = System.currentTimeMillis();
        int maxIter = 1000, iter = 0;
        double proRCL = 0.4;
        Random random = new Random();
        ArrayList<Item> items = new ArrayList<>(); //itemList每次的重置
        ArrayList<Item> itemList = new ArrayList<>();
        items.addAll(instance.getAllItems());
        items.sort(new GraspItemComparator(instance));
        PositionScorer scorer = new EnvelopeUtilization();
        ArrayList<Solution> solutions = new ArrayList<>();
        Solution bestSolution = null;
        double minSolutionCost = Double.MAX_VALUE;
        double bagCostSum = 0;
        for (Bag bag : instance.getBags()) bagCostSum += bag.getCost();
        System.out.println("item sort");
        for (Item item : items) {
            System.out.println(item);
        }

//        assert false;
        while (iter < maxIter) {
            //每轮生成一个solution
            iter++;
            itemList.clear();
            itemList.addAll(items);
            Solution currentSolution = new Solution();
            //itemList 排序
            //开始装
            while (!itemList.isEmpty()) {
                Bag chooseBag = randomModifyBag(itemList, instance, bagCostSum);
                System.out.println(chooseBag);

                ExtremePointSet epSet = new ExtremePointSet(chooseBag);
                ArrayList<Position> packedPositions = new ArrayList<>();
                epSet.getExtremePoints().add(new ExtremePoint(0, 0, 0));
                ArrayList<Item> packedItems = new ArrayList<>();
                boolean canPack = true;
                while (!itemList.isEmpty()) {
//                    System.out.println(itemList.size());
                    ArrayList<Item> itemRCL = new ArrayList<>();
                    //TODO 构建RCL
                    for (Item item : itemList) {
                        if (itemRCL.size() >= proRCL * itemList.size()) break;
                        //如果本身放不下就continue
                        if (itemList.get(0).getLengthOf(0) + itemList.get(0).getLengthOf(2) > chooseBag.getX()
                                && itemList.get(0).getLengthOf(1) + itemList.get(0).getLengthOf(2) > chooseBag.getY())
                            continue;
                        if (epSet.canPackItemOfOrientation(item, packedPositions)) {
                            itemRCL.add(item);
                        }

                    }
                    System.out.println("RCL : " + itemRCL);
                    if (itemRCL.isEmpty()) {
                        System.out.println("RCL is empty: unable to pack into current bag");
                        break;
                    }
                    // 从RCL中随机选择item, bestFit
                    int rI = random.nextInt(itemRCL.size());
                    Item toPackItem = itemRCL.get(rI);
                    System.out.println("packedItem id : " + toPackItem.getId());
                    boolean successToRemove = itemList.remove(toPackItem);
                    assert successToRemove : "remove itemList wrong";
                    System.out.println("itemList size : " + itemList.size());

                    Position bestPosition = bestFitPosition(toPackItem, epSet, packedPositions, scorer);
                    //更新
                    packedItems.add(toPackItem);
                    epSet.updateEPSetAfterPack(packedPositions, bestPosition);
                    packedPositions.add(bestPosition);
                }
                //一个bag装完了
                currentSolution.addApattern(chooseBag, packedItems, packedPositions);
            }
            currentSolution.calCost();
            System.out.print("！！！！！！！！！before improveSmaller: " + currentSolution.getCost());
            // 缩袋
            for (int i = 0; i < currentSolution.usedBags.size(); i++) {
                improveSmallerBag(instance, currentSolution, i);
            }
            currentSolution.calCost();
            System.out.println("  after improveSmaller: " + currentSolution.getCost());
            int maxMergeIter = currentSolution.usedBags.size();
            int mergeIter = 0;
            TwoTuple tp = new TwoTuple();
            while (mergeIter < maxMergeIter) {
                mergeIter++;
                boolean canMerge = false;
                for (int i = 0; i < currentSolution.usedBags.size()-1; i++) {
                    for (int j = i + 1; j < currentSolution.usedBags.size(); j++) {
                        canMerge = mergeTwoBagsIntoOne(instance, currentSolution, i, j);
                        if (canMerge) break;
                        ;
                    }
                    if (canMerge) break;
                }
                if (!canMerge) break;
            }
            currentSolution.calCost();
            System.out.println("  after merge: " + currentSolution.getCost()+"!!!!!!!!!!!!!!!!!");
            // 二次缩袋
            for (int i = 0; i < currentSolution.usedBags.size(); i++) {
                improveSmallerBag(instance, currentSolution, i);
            }
            currentSolution.calCost();
            System.out.println("  after improveSmaller2: " + currentSolution.getCost());



            //TODO improvement  拆箱
            //TODO improvement 合箱子 0.4+0.4>0.7  0.4+0.7>1
//            while (true){
//
//            }

//            System.out.println(currentSolution.usedBags.size());
//            System.out.println(currentSolution.packedItemsList.size());
//            System.out.println(currentSolution.packedPositionsList.size());
            //TODO local search

            if (currentSolution.getCost() < minSolutionCost) {
                minSolutionCost = currentSolution.getCost();
                bestSolution = currentSolution;
            }
            System.out.println("best cost = " + minSolutionCost + " ; current cost = " + currentSolution.getCost());
            for (int i = 0; i < currentSolution.usedBags.size(); i++) {
                System.out.print(currentSolution.getUsedBags().get(i).getType() + "-->  ");
                for (Item item : currentSolution.getPackedItemsList().get(i)) {
                    System.out.print(item.getId() + ",");
                }
                System.out.println();
            }
        }
        long time2 = System.currentTimeMillis();
        System.out.println("=====best solution=========");
        System.out.println("time cost : " + (time2 - time1) + " ms");
        System.out.println("best cost = " + minSolutionCost);
        for (int i = 0; i < bestSolution.usedBags.size(); i++) {
            System.out.print(bestSolution.getUsedBags().get(i).getType() + "-->  ");
            for (Item item : bestSolution.getPackedItemsList().get(i)) {
                System.out.print(item.getId() + ",");
            }
            System.out.println();
        }
//        FileWriter fw = new FileWriter("D:\\java\\BPP_Model\\0323_GRASP2.txt",true);
//        fw.write(suanli+" "+minSolutionCost);
//        fw.write(" "+(time2 - time1) * 1.0 / 1000 + "s");
//        fw.write("\n");//Windows平台下用\r\n，Linux/Unix平台下用\n
//        fw.close();
    }

    public static void main(String[] args) throws IOException {
        Instance instance = new Instance();
        instance.initialze(args[0], args[1]);
        instance.preprocessMergeBagMap();
//        assert false;
        //feasible
        boolean isFeasible = check.isInstanceFeasible(instance);
        if (isFeasible) {
            Grasp.pack(instance, args[1]);
        } else {
            FileWriter fw = new FileWriter("D:\\java\\BPP_Model\\0323_GRASP2.txt",true);
            fw.write(args[1]+" infeasible");
            fw.write("\n");//Windows平台下用\r\n，Linux/Unix平台下用\n
            fw.close();
        }
    }
}
