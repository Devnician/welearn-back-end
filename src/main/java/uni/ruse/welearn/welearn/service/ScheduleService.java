package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.Schedule;
import uni.ruse.welearn.welearn.repository.ScheduleRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.Optional;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    public Schedule findById(String id) throws WeLearnException {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isEmpty()) {
            throw new WeLearnException("Schedule with id " + id + " is not found");
        }
        return optionalSchedule.get();
    }
}
