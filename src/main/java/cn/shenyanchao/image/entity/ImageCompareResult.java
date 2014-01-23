package cn.shenyanchao.image.entity;

/**
 * @date 14-1-16.
 * @author  shenyanchao
 */
public class ImageCompareResult {

    private boolean match;

    private String diffImageId;

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public String getDiffImageId() {
        return diffImageId;
    }

    public void setDiffImageId(String diffImageId) {
        this.diffImageId = diffImageId;
    }

    @Override
    public String toString() {
        return "ImageCompareResult{" +
                "match=" + match +
                ", diffImageId='" + diffImageId + '\'' +
                '}';
    }
}
