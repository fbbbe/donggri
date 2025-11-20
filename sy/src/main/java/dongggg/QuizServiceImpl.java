// 시험 로직 구현

package dongggg;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import dongggg.QuizService.QuizMode;

public class QuizServiceImpl implements QuizService {

    private final ConceptPairRepository pairRepo = new ConceptPairRepository();

    @Override
    public List<ConceptPair> generateQuiz(List<Integer> noteIds, QuizMode mode, int limit) {
        List<ConceptPair> list = new ArrayList<>();
        if (noteIds != null) {
            switch (mode) {
                case WORST -> list = ConceptPairRepository.findWorstByNoteIds(noteIds, limit);
                case ALL -> {
                    for (Integer id : noteIds) {
                        if (id == null) continue;
                        list.addAll(ConceptPairRepository.findByNoteId(id));
                    }
                    if (limit > 0 && list.size() > limit) {
                        Collections.shuffle(list);
                        list = list.subList(0, limit);
                    }
                }
            }
        }

        Collections.shuffle(list); // 랜덤 출제
        return list;
    }

    @Override
    public void updateResult(ConceptPair pair, boolean isCorrect) {
        if (pair == null || pair.getId() == 0) {
            return;
        }

        ConceptPairRepository.updateResult(pair.getId(), isCorrect);

        System.out.println("[시험 기록][저장] " + pair.getTerm() + " / " + (isCorrect ? "정답" : "오답"));
    }
}
