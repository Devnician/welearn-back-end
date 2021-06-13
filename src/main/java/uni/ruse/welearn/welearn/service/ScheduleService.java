package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.components.EmailService;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.Schedule;
import uni.ruse.welearn.welearn.repository.EventRepository;
import uni.ruse.welearn.welearn.repository.ScheduleRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    GroupService groupService;
    @Autowired
    EventService eventService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EmailService emailService;

    public Schedule findById(String id) throws WeLearnException {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isEmpty()) {
            throw new WeLearnException("Schedule with id " + id + " is not found");
        }
        return optionalSchedule.get();
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Schedule save(Schedule schedule) throws WeLearnException {
        if (schedule.getId() != null) {
            throw new WeLearnException("New schedule entity cannot have id.");
        }
        return scheduleRepository.save(schedule);
    }

    public Schedule edit(Schedule schedule) throws WeLearnException {
        Schedule foundSchedule = findById(schedule.getId());
        foundSchedule.setDayOfWeek(schedule.getDayOfWeek());
        foundSchedule.setStartDate(schedule.getStartDate());
        foundSchedule.setEndDate(schedule.getEndDate());
        foundSchedule.setStartHour(schedule.getStartHour());
        foundSchedule.setEndHour(schedule.getEndHour());
        return scheduleRepository.save(foundSchedule);
    }

    public void delete(String id) throws WeLearnException {
        Schedule foundSchedule = findById(id);
        scheduleRepository.delete(foundSchedule);
    }

    public void generateEvents(String id) throws WeLearnException {
        Schedule foundSchedule = findById(id);
        Group foundGroup = groupService.findOne(foundSchedule.getGroup().getGroupId());
        for (Event events : foundGroup.getEvents()){
            try {
                eventService.deleteWithoutEmail(events.getEventId());
            } catch (Exception exception){
                exception.printStackTrace();
            }
        }
        for (
                LocalDate date = foundSchedule.getStartDate().toLocalDateTime().toLocalDate();
                date.isBefore(foundSchedule.getEndDate().toLocalDateTime().toLocalDate());
                date = date.plusDays(1)
        ) {
            for (Schedule schedule : foundGroup.getSchedules()) {
                if (getDayStringNew(date, Locale.ENGLISH).equalsIgnoreCase(schedule.getDayOfWeek())) {
                    Event newEvent = new Event(
                            "",
                            schedule.getDiscipline().getName(),
                            Timestamp.valueOf(date.atTime(schedule.getStartHour().toLocalTime())),
                            Timestamp.valueOf(date.atTime(schedule.getEndHour().toLocalTime())),
                            "Class",
                            "Description",
                            foundGroup,
                            schedule.getDiscipline(),
                            null
                    );
                    eventService.saveWithoutEmail(newEvent);
                }
            }
        }
        AtomicReference<String> emailText = new AtomicReference<>("");
        foundGroup.getSchedules().forEach(it -> emailText.set(emailText + "\n" + it.toString()));
        foundGroup.getUsers().forEach(it -> emailService.sendSimpleMessage(
                it.getEmail(),
                "A set of events has been generated - Welearn",
                emailText.get()
        ));
    }

    public static String getDayStringNew(LocalDate date, Locale locale) {
        DayOfWeek day = date.getDayOfWeek();
        return day.getDisplayName(TextStyle.FULL, locale);
    }
}
