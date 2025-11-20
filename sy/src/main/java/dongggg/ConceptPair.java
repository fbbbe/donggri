package dongggg;

public class ConceptPair {

    private int id;
    private int noteId;
    private String term;
    private String explanation;
    private int sortOrder;
    private int totalAttempts;
    private int correctCount;
    private double wrongRate;

    public ConceptPair(int id, int noteId, String term, String explanation, int sortOrder,
            int totalAttempts, int correctCount, double wrongRate) {
        this.id = id;
        this.noteId = noteId;
        this.term = term;
        this.explanation = explanation;
        this.sortOrder = sortOrder;
        this.totalAttempts = totalAttempts;
        this.correctCount = correctCount;
        this.wrongRate = wrongRate;
    }

    public ConceptPair(String term, String explanation) {
        this(0, 0, term, explanation, 0);
    }

    public ConceptPair(int id, int noteId, String term, String explanation, int sortOrder) {
        this(id, noteId, term, explanation, sortOrder, 0, 0, 0.0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(int totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public double getWrongRate() {
        return wrongRate;
    }

    public void setWrongRate(double wrongRate) {
        this.wrongRate = wrongRate;
    }

    @Override
    public String toString() {
        if (term == null || term.isBlank()) {
            return "(개념 없음)";
        }
        return term;
    }
}
