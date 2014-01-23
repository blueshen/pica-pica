package cn.shenyanchao.image.algorithm;

import org.springframework.stereotype.Component;

/**
 * Bhattacharyya Coefficient
 *
 * @author shenyanchao
 * @date 14-1-23.
 */
@Component
public class BhattacharyyaDistance implements IDistance {

    @Override
    public double distance(double[] a, double[] b) {
        double dist = 0.0;
        double[] mixedData = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            mixedData[i] = Math.sqrt(a[i] * b[i]);
        }
        for (int i = 0; i < mixedData.length; i++) {
            dist += mixedData[i];
        }
        return dist;
    }
}
