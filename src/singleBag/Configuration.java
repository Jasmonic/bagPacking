package singleBag;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-12-2022-12-15
 * @Description: BPP_Model
 * @version=1.0
 */
public class Configuration {
    public static int beamWidth=2;
    public static int w=500;
    public static int itemWidth=1;


    //0: Envelope
    //1: BagFace
    //2: EnvelopeUtilization
    //3: EnvelopeUtilizationBagFace
    public static int positionScorer=2;

    //0: VolumeItem
    //1: BagFaceItem
    public static int sortItem=0;
}
