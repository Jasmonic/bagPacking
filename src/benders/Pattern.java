package benders;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-10-2022-10-26
 * @Description: BPP_Model
 * @version=1.0
 */
public class Pattern {
    final int[] pattern;
    public Pattern(Instance instance, ArrayList<Integer> items,int bag){
        pattern=new int[instance.getItemTypeCount()+1];
        for (int i = 0; i < items.size(); i++) {
            pattern[instance.getAllItems().get(items.get(i)).getType()]+=1;
        }
        pattern[instance.getItemTypeCount()]= instance.getAllBags().get(bag).getType();
        System.out.println("patten : "+Arrays.toString(pattern));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern1 = (Pattern) o;
        return Arrays.equals(pattern, pattern1.pattern);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pattern);
    }
}
