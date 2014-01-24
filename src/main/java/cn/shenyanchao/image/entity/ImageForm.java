package cn.shenyanchao.image.entity;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author shenyanchao
 * @date 2014-01-23
 */
public class ImageForm {

    private MultipartFile sourceFile;
    private MultipartFile candidateFile;
    private int col = 20;
    private int row = 20;
    private double similarityThreshold = 0.8;

    public MultipartFile getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(MultipartFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    public MultipartFile getCandidateFile() {
        return candidateFile;
    }

    public void setCandidateFile(MultipartFile candidateFile) {
        this.candidateFile = candidateFile;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public double getSimilarityThreshold() {
        return similarityThreshold;
    }

    public void setSimilarityThreshold(double similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }
}
