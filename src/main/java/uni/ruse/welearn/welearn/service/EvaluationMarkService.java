package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.EvaluationMark;
import uni.ruse.welearn.welearn.repository.EvaluationMarkRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.List;
import java.util.Optional;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class EvaluationMarkService {
    @Autowired
    private EvaluationMarkRepository evaluationMarkRepository;

    public EvaluationMark findOne(String id) throws WeLearnException {
        Optional<EvaluationMark> evaluationMark = evaluationMarkRepository.findById(id);
        if (evaluationMark.isEmpty()) {
            throw new WeLearnException("Evaluation mark not found");
        }
        return evaluationMark.get();
    }

    public EvaluationMark findById(String id) throws WeLearnException {
        return findOne(id);
    }

    public List<EvaluationMark> findAll() {
        return evaluationMarkRepository.findAll();
    }

    public EvaluationMark save(EvaluationMark evaluationMark) {
        return evaluationMarkRepository.save(evaluationMark);
    }

    public EvaluationMark edit(EvaluationMark evaluationMark) throws WeLearnException {
        EvaluationMark existingEvaluationMark = findOne(evaluationMark.getId());
        existingEvaluationMark.setMarkValue(evaluationMark.getMarkValue());
//        existingEvaluationMark.setDiscipline(evaluationMark.getDiscipline());
//        existingEvaluationMark.setGroup(evaluationMark.getGroup());
//        existingEvaluationMark.setUser(evaluationMark.getUser());
        return evaluationMarkRepository.save(existingEvaluationMark);
    }

    public void delete(String id) throws WeLearnException {
        EvaluationMark evaluationMark = findOne(id);
        evaluationMarkRepository.delete(evaluationMark);
    }
}
