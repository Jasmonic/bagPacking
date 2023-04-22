package baseObject;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-08-2022-08-23
 * @Description: BPP_Model
 * @version=1.0
 */
public class Item {
    int type;
    int p,q,r;
    int num;
    int volume;
    int id;
    public Item(int type, int p, int q, int r, int num) {
        this.type = type;
        this.p = p;
        this.q = q;
        this.r = r;
        this.num = num;
        this.volume=p*q*r;
    }

    public Item(int type, int p, int q, int r, int num, int volume, int id) {
        this.type = type;
        this.p = p;
        this.q = q;
        this.r = r;
        this.num = num;
        this.volume = volume;
        this.id = id;
    }

    public Item copyOfOrientation(int t){
        int[] num={getP(),getQ(),getR()};
        Arrays.sort(num);
        switch (t){
            case 6: return new Item(type,num[0],num[1],num[2],1,volume,id);
            case 5: return new Item(type,num[0],num[2],num[1],1,volume,id);
            case 4: return new Item(type,num[1],num[0],num[2],1,volume,id);
            case 3: return new Item(type,num[1],num[2],num[0],1,volume,id);
            case 2: return new Item(type,num[2],num[0],num[1],1,volume,id);
            case 1: return new Item(type,num[2],num[1],num[0],1,volume,id);
            default:
                System.out.println("param of copyOfOrientation must be in range [1..6],now is "+t);
                return null;
        }
    }
    public int getLengthOf(int i){
        int[] num={getP(),getQ(),getR()};
        Arrays.sort(num);
        switch (i){
            case 0: return num[2];
            case 1: return num[1];
            case 2: return num[0];
            default:
                System.out.println("param of getLengthOf must be in range [0..2],now is "+i);
                return -1;
        }
    }

    @Override
    public String toString() {
        return "Item{" +
                "type=" + type +
                ", id=" + id +
                ", p=" + p +
                ", q=" + q +
                ", r=" + r +
                ", num=" + num +
                ", volume=" + volume +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
