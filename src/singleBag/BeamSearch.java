package singleBag;

import baseObject.*;
import benders.Instance;
import comparator.BagFaceItem;
import comparator.NodeScoreComparator;
import comparator.VolumeHeightItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-12-2022-12-12
 * @Description: BPP_Model
 * @version=1.0
 */
public class BeamSearch {
    public  boolean successToPack = false;
    public Node lastNode;
    public BeamSearch() {
    }

    public  void pack(Instance instance, Bag bag, ArrayList<Integer> remainingItemsId) {
        //对Item排序
//        System.out.println("排序前" + remainingItemsId);
        ArrayList<Item> remainingItems = new ArrayList<>();
        for (int Id : remainingItemsId) {
            remainingItems.add(instance.getAllItems().get(Id));
        }
        switch (Configuration.sortItem){
            case 0:remainingItems.sort(new VolumeHeightItem()); break;
            case 1:remainingItems.sort(new BagFaceItem()); break;
            default: Collections.shuffle(remainingItems); break;
        }
        remainingItemsId.clear();
        for (Item item : remainingItems) {
            remainingItemsId.add(item.getId());
//            System.out.println(item);
        }
        System.out.println("排序后" + remainingItemsId);
        ArrayList<Node> nodeList = new ArrayList<>();
        Node node0 = new Node(bag, remainingItemsId, instance);
        nodeList.add(node0);
        int count = 0;
        boolean isTerminated = false;
        Node successNode = null;
        lastNode=node0;
        int w = Configuration.w;
        int itemWidth=Configuration.itemWidth;
        while (!isTerminated) {
//            System.out.println("——————————————————————————————————————————第" + count + "层——————————————————————————————————————");
            ArrayList<Node> newNodeList = new ArrayList<>();
            for (Node node : nodeList) {
//                printNode(node);
                //检查有多少EP是废的
                int c=0;
//                for (ExtremePoint extremePoint : node.getExtremePointSet().getExtremePoints()) {
//                    boolean useless = false;
//                    for (Position packedPosition : node.getPackedPosition()) {
//                        if ((extremePoint.getY() == packedPosition.getY() //XZ平面
//                                && extremePoint.getX() > packedPosition.getX() && extremePoint.getX() < packedPosition.getX() + packedPosition.getLx()
//                                && extremePoint.getZ() > packedPosition.getZ() && extremePoint.getZ() < packedPosition.getZ() + packedPosition.getLz())
//                                || (extremePoint.getX() == packedPosition.getX() //YZ平面
//                                && extremePoint.getY() > packedPosition.getY() && extremePoint.getY() < packedPosition.getY() + packedPosition.getLy()
//                                && extremePoint.getZ() > packedPosition.getZ() && extremePoint.getZ() < packedPosition.getZ() + packedPosition.getLz())
//                                || (extremePoint.getZ() == packedPosition.getZ() //XZ平面
//                                && extremePoint.getY() > packedPosition.getY() && extremePoint.getY() < packedPosition.getY() + packedPosition.getLy()
//                                && extremePoint.getX() > packedPosition.getX() && extremePoint.getX() < packedPosition.getX() + packedPosition.getLx())
//                                || //在箱子里
//                                (extremePoint.getZ() > packedPosition.getZ() && extremePoint.getZ() < packedPosition.getZ() + packedPosition.getLz()
//                                && extremePoint.getY() > packedPosition.getY() && extremePoint.getY() < packedPosition.getY() + packedPosition.getLy()
//                                && extremePoint.getX() > packedPosition.getX() && extremePoint.getX() < packedPosition.getX() + packedPosition.getLx())
//                        ){
//                            c++;break;
//                        }
//                    }
//                    System.out.println(c);
//                }


//                ArrayList<Node> expandedNodeList = node.expand(w, instance.getScorer(), node.getRemainingItemsId());
                ArrayList<Node> expandedNodeList = node.expand2(w, itemWidth,instance.getScorer(), node.getRemainingItemsId());
                if (expandedNodeList != null) {
                    newNodeList.addAll(expandedNodeList);

                }

            }
//            System.out.println(nodeList.size());
            if (newNodeList.size() > 0) {

                nodeList.clear();
                //TODO 1.0 addAll ;  2.0: 前w个
//                nodeList.addAll(newNodeList);
                newNodeList.sort(new NodeScoreComparator());
                for (int i = 0; i < Math.min(newNodeList.size(), w); i++) {
                    nodeList.add(newNodeList.get(i));
                }
                lastNode= newNodeList.get(0);
                if (remainingItems.size() == newNodeList.get(0).getPackedPosition().size()) {
                    this.successToPack = true;
                    successNode = newNodeList.get(0);
                    isTerminated = true;
                    System.out.println("haha");
                    break;
                }
            } else {
                this.successToPack = false;
                isTerminated = true;
                break;
            }
//            System.out.println("haha"+count+newNodeList.get(0).getPackedPosition().size());
            count++;

        }

        if (this.successToPack) {

            for (Position position : successNode.getPackedPosition()) {
                System.out.println(position);
            }
//            System.out.println(successNode);
        } else {
            System.out.println("装不完，装了" + count);
//            System.out.println("");
        }
//        System.out.println(lastNode);
//        printNode(lastNode);


    }

    public static void printNode(Node node) {
        System.out.println("==========START=============");
        for (Position position : node.getPackedPosition()) {
            System.out.println(position);
        }
        for (ExtremePoint extremePoint : node.getExtremePointSet().getExtremePoints()) {
            System.out.println(extremePoint);
        }
        System.out.println("装载物品"+node.getPackedItemsId());
        System.out.println("剩余物品" + node.getRemainingItemsId());
        System.out.println("==========END==============");
    }

    public static void main(String[] args) throws IOException {
        System.out.println("执行main程序");
        Instance instance = new Instance();
        instance.initialze(args[0], args[1]);
//        Bag bag=instance.getAllBags().get(38);//Bags().get(1);
//        bag=instance.getBags().get(1);
         Bag bag = instance.getBags().get(1);
        System.out.println(bag);
//        Integer[]items2={0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 18};
//        ArrayList<Integer> remainingItemsId = new ArrayList<>(Arrays.asList(items2));
//        [0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 18]
        ArrayList<Integer> remainingItemsId = new ArrayList<>();
        for (int i = 0; i < instance.getAllItems().size(); i++) {
            remainingItemsId.add(i);
        }

//        assert false;
        BeamSearch beamSearch =new BeamSearch();
        beamSearch.pack(instance, bag, remainingItemsId);
    }
}

