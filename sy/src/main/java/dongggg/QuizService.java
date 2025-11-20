// 시험 기능 메서드 정의

package dongggg;

import java.util.List;

public interface QuizService {

    enum QuizMode {
        ALL,
        WORST
    }

    List<ConceptPair> generateQuiz(List<Integer> noteIds, QuizMode mode, int limit); // 문제 출제
    void updateResult(ConceptPair pair, boolean isCorrect); // 정답/오답 DB 반영
}
