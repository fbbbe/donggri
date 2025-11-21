package dongggg;

public class DonggriStatus {

    private int cumulativeScore;
    private int cumulativeCorrect;
    private int examCount;   // ⭐ 추가

    public DonggriStatus(int cumulativeScore, int cumulativeCorrect, int examCount) {
        this.cumulativeScore = cumulativeScore;
        this.cumulativeCorrect = cumulativeCorrect;
        this.examCount = examCount;
    }

    public int getCumulativeScore() {
        return cumulativeScore;
    }

    public void setCumulativeScore(int cumulativeScore) {
        this.cumulativeScore = cumulativeScore;
    }

    public int getCumulativeCorrect() {
        return cumulativeCorrect;
    }

    public void setCumulativeCorrect(int cumulativeCorrect) {
        this.cumulativeCorrect = cumulativeCorrect;
    }

    public int getExamCount() {
        return examCount;
    }

    public void setExamCount(int examCount) {
        this.examCount = examCount;
    }
}
