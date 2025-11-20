package dongggg;

public class DonggriStatus {

    private int cumulativeScore;
    private int cumulativeCorrect;

    public DonggriStatus(int cumulativeScore, int cumulativeCorrect) {
        this.cumulativeScore = cumulativeScore;
        this.cumulativeCorrect = cumulativeCorrect;
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
}
