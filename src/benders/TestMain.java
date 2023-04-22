package benders;

import ilog.concert.IloException;

import java.io.IOException;

/**
 * @Author: Feng Jixuan
 * @Date: 2023-01-2023-01-18
 * @Description: BPP_Model
 * @version=1.0
 */
public class TestMain {
    public static void main(String[] args) throws IOException, IloException {
        for (int i = 1; i <= 60; i++) {
            Main.main(new String[]{"ali2", ""+i, "abcd"});
        }
    }
}
