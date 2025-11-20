package dongggg;

public class DonggriLevelInfo {

    private final int currentLevel;
    private final Integer nextLevel;
    private final int requiredScoreForNext;
    private final int requiredCorrectForNext;
    private final int remainingScore;
    private final int remainingCorrect;
    private final double progressRatio;
    private final int cumulativeScore;
    private final int cumulativeCorrect;

    public DonggriLevelInfo(int currentLevel,
            Integer nextLevel,
            int requiredScoreForNext,
            int requiredCorrectForNext,
            int remainingScore,
            int remainingCorrect,
            double progressRatio,
            int cumulativeScore,
            int cumulativeCorrect) {
        this.currentLevel = currentLevel;
        this.nextLevel = nextLevel;
        this.requiredScoreForNext = requiredScoreForNext;
        this.requiredCorrectForNext = requiredCorrectForNext;
        this.remainingScore = remainingScore;
        this.remainingCorrect = remainingCorrect;
        this.progressRatio = progressRatio;
        this.cumulativeScore = cumulativeScore;
        this.cumulativeCorrect = cumulativeCorrect;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Integer getNextLevel() {
        return nextLevel;
    }

    public int getRequiredScoreForNext() {
        return requiredScoreForNext;
    }

    public int getRequiredCorrectForNext() {
        return requiredCorrectForNext;
    }

    public int getRemainingScore() {
        return remainingScore;
    }

    public int getRemainingCorrect() {
        return remainingCorrect;
    }

    public double getProgressRatio() {
        return progressRatio;
    }

    public int getCumulativeScore() {
        return cumulativeScore;
    }

    public int getCumulativeCorrect() {
        return cumulativeCorrect;
    }

    public boolean isMaxLevel() {
        return nextLevel == null;
    }
}
