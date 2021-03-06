package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.repository.DisciplineRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.List;
import java.util.Optional;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class DisciplineService {

    @Autowired
    private DisciplineRepository disciplineRepository;

    public List<Discipline> getDisciplines() {
        return disciplineRepository.findAll();
    }

    public Discipline getDisciplineById(String disciplineId) throws WeLearnException {
        return findOne(disciplineId);
    }

    public Discipline findOne(String disciplineId) throws WeLearnException {
        Optional<Discipline> discipline = disciplineRepository.findById(disciplineId);
        if (discipline.isEmpty()) {
            throw new WeLearnException("Discipline with id " + disciplineId + " not found.");
        }
        return discipline.get();
    }

    public Discipline createDiscipline(Discipline discipline) throws WeLearnException {
        if (disciplineRepository.findById(discipline.getId()).isEmpty()) {
            return disciplineRepository.save(discipline);
        }
        throw new WeLearnException("Discipline with id " + discipline.getId() + " already exists");
    }

    public Discipline editDiscipline(Discipline discipline) throws WeLearnException {
        findOne(discipline.getId());
        return disciplineRepository.save(discipline);
    }

    public void removeDiscipline(String disciplineId) throws WeLearnException {
        disciplineRepository.delete(findOne(disciplineId));
    }
}
