package cn.shenyanchao.image.algorithm;

import org.springframework.stereotype.Component;

@Component
public class EuclideanDistance implements IDistance {
    public double distance(double[] a, double[] b) {

        return Math.sqrt(sqrdist(a, b));

    }

    private double sqrdist(double[] a, double[] b) {

        double dist = 0;

        for (int i = 0; i < a.length; ++i) {
            double diff = (a[i] - b[i]);
            dist += diff * diff;
        }

        return dist;
    }


}
