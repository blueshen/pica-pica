package cn.shenyanchao.image.algorithm;

import org.springframework.stereotype.Component;

/**
 * 汉明距离
 */
@Component
public class HammingDistance implements IDistance {

    public double distance(double[] a, double[] b) {

        double dist = 0;

        for (int i = 0; i < a.length; ++i) {
            double diff = (a[i] - b[i]);
            dist += Math.abs(diff);
        }
        return dist;
    }
}
