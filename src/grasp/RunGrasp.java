package grasp;

import benders.Main;

import java.io.IOException;

/**
 * @Author: Feng Jixuan
 * @Date: 2023-03-2023-03-03
 * @Description: BPP_Model
 * @version=1.0
 */
public class RunGrasp {
    public static void main(String[] args) throws IOException  {
        for (int i = 1; i <= 60; i++) {
            Grasp.main(new String[]{"ali2", ""+i});
        }
    }
}
