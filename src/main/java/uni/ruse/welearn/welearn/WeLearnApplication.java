package uni.ruse.welearn.welearn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uni.ruse.welearn.welearn.enums.Role;
import uni.ruse.welearn.welearn.model.Day;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.EvaluationMark;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.Resource;
import uni.ruse.welearn.welearn.model.Schedule;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.repository.DayRepository;
import uni.ruse.welearn.welearn.repository.DisciplineRepository;
import uni.ruse.welearn.welearn.repository.EvaluationMarkRepository;
import uni.ruse.welearn.welearn.repository.EventRepository;
import uni.ruse.welearn.welearn.repository.GroupRepository;
import uni.ruse.welearn.welearn.repository.ResourceRepository;
import uni.ruse.welearn.welearn.repository.ScheduleRepository;
import uni.ruse.welearn.welearn.repository.UserRepository;

@SpringBootApplication
public class WeLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeLearnApplication.class, args);
    }

    /**
     * Fill database with mock data to demonstrate the ability of the prototype to effectively use the database design
     * @param groupRepository
     * @param userRepository
     * @param eventRepository
     * @param scheduleRepository
     * @param resourceRepository
     * @param disciplineRepository
     * @param dayRepository
     * @param evaluationMarkRepository
     * @return
     * @throws IOException
     */
    @Bean
    public List<Group> fillUpDatabaseIfNotEmpty(
            GroupRepository groupRepository,
            UserRepository userRepository,
            EventRepository eventRepository,
            ScheduleRepository scheduleRepository,
            ResourceRepository resourceRepository,
            DisciplineRepository disciplineRepository,
            DayRepository dayRepository,
            EvaluationMarkRepository evaluationMarkRepository
    ) throws IOException {
        if (groupRepository.findAll().isEmpty()) {
            Group group = new Group();
            group.setName("Group");
            group.setDescription("Group description");
            group.setMaxResourcesMb(200);
            group.setStartDate(Timestamp.valueOf("2021-2-1 00:00:00"));
            group.setEndDate(Timestamp.valueOf("2021-3-1 00:00:00"));
            groupRepository.save(group);
        }

        if (userRepository.findAll().isEmpty()) {
            User user1 = new User();
            user1.setEmail("user1@mail.com");
            user1.setFirstName("First user");
            user1.setLastName("One");
            user1.setPassword("pwd");
            user1.setUsername("user1");
            user1.setRole(Role.ADMINISTRATOR);
            user1.setGroup(groupRepository.findAll().stream().findFirst().get());

            User user2 = new User();
            user2.setEmail("user2@mail.com");
            user2.setFirstName("Second name");
            user2.setLastName("Two");
            user2.setPassword("pwd");
            user2.setUsername("user2");
            user2.setRole(Role.TAUGHT);
            user2.setGroup(groupRepository.findAll().stream().findFirst().get());

            User user3 = new User();
            user3.setEmail("user3@mail.com");
            user3.setFirstName("Third user");
            user3.setLastName("Three");
            user3.setPassword("pwd");
            user3.setUsername("user3");
            user3.setRole(Role.TEACHING);
            user3.setGroup(groupRepository.findAll().stream().findFirst().get());

            userRepository.saveAll(Set.of(user1, user2, user3));
        }

        if (dayRepository.findAll().size() != 7) {
            Day monday = new Day("", "Monday");
            Day tuesday = new Day("", "Tuesday");
            Day wednesday = new Day("", "Wednesday");
            Day thursday = new Day("", "Thursday");
            Day friday = new Day("", "Friday");
            Day saturday = new Day("", "Saturday");
            Day sunday = new Day("", "Sunday");
            dayRepository.saveAll(Set.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday));
        }

        if (disciplineRepository.findAll().size() != 3) {
            Discipline math = new Discipline("", "Math");
            Discipline english = new Discipline("", "English");
            Discipline oop = new Discipline("", "OOP");
            disciplineRepository.saveAll(Set.of(math, english, oop));
        }

        if (resourceRepository.findAll().size() != 2) {
            Resource src1 = new Resource("", "Resource 1", ".pdf", "/grp1", true, groupRepository.findAll().stream().findFirst().get());
            Resource src2 = new Resource("", "Resource 2", ".docx", "/grp1", true, groupRepository.findAll().stream().findFirst().get());
            resourceRepository.saveAll(Set.of(src1, src2));
        }

        if (scheduleRepository.findAll().size() != 14) {
            Schedule fridayMath = new Schedule(
                    "",
                    new Time(8, 0, 0),
                    new Time(8, 40, 0),
                    groupRepository.findAll().stream().findFirst().get(),
                    dayRepository.findByName("Friday").get(),
                    disciplineRepository.findByName("Math").get()
            );
            Schedule saturdayEnglish = new Schedule(
                    "",
                    new Time(9, 0, 0),
                    new Time(9, 40, 0),
                    groupRepository.findAll().stream().findFirst().get(),
                    dayRepository.findByName("Saturday").get(),
                    disciplineRepository.findByName("English").get()
            );
            Schedule sundayOop = new Schedule(
                    "",
                    new Time(10, 0, 0),
                    new Time(10, 40, 0),
                    groupRepository.findAll().stream().findFirst().get(),
                    dayRepository.findByName("Sunday").get(),
                    disciplineRepository.findByName("OOP").get()
            );
            scheduleRepository.saveAll(Set.of(fridayMath, saturdayEnglish, sundayOop));
        }

        if (eventRepository.findAll().isEmpty()) {
            Group foundGroup = groupRepository.findAll().get(0);
            for (LocalDate date = foundGroup.getStartDate().toLocalDateTime().toLocalDate(); date.isBefore(foundGroup.getEndDate().toLocalDateTime().toLocalDate()); date = date.plusDays(1)) {
                LocalDate finalDate = date;
                foundGroup.getSchedules().forEach(schedule -> {
                    if (getDayStringNew(finalDate, Locale.ENGLISH).equalsIgnoreCase(schedule.getDay().getName())) {
                        Event newEvent = new Event(
                                "",
                                schedule.getDiscipline().getName(),
                                Timestamp.valueOf(finalDate.atTime(schedule.getStartTime().toLocalTime())),
                                Timestamp.valueOf(finalDate.atTime(schedule.getEndTime().toLocalTime())),
                                "Class",
                                foundGroup,
                                null
                        );
                        eventRepository.save(newEvent);
                    }
                });
            }
            Event eventBlacklistUser = eventRepository.findAll().stream().findFirst().get();
            eventBlacklistUser.setBlacklist(Set.of(userRepository.findByEmail("user2@mail.com").get()));
            eventRepository.save(eventBlacklistUser);
        }

        if (evaluationMarkRepository.findAll().isEmpty()) {
            Group group = groupRepository.findAll().get(0);
            User user = userRepository.findByEmail("user2@mail.com").get();
            EvaluationMark mark1 = new EvaluationMark(
                    "",
                    BigDecimal.valueOf(2),
                    group,
                    disciplineRepository.findByName("Math").get(),
                    user
            );
            EvaluationMark mark2 = new EvaluationMark(
                    "",
                    BigDecimal.valueOf(5),
                    group,
                    disciplineRepository.findByName("OOP").get(),
                    user
            );
            EvaluationMark mark3 = new EvaluationMark(
                    "",
                    BigDecimal.valueOf(6),
                    group,
                    disciplineRepository.findByName("English").get(),
                    user
            );
            evaluationMarkRepository.saveAll(Set.of(mark1, mark2, mark3));
        }

        File file = new File("data/databaseExport.txt");
        FileWriter myWriter = new FileWriter("data/databaseExport.txt");
        groupRepository.findAll().forEach(
                group -> {
                    try {
                        System.out.println("Writing to file");
                        myWriter.write(group.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        myWriter.close();
        return groupRepository.findAll();
    }

    public static String getDayStringNew(LocalDate date, Locale locale) {
        DayOfWeek day = date.getDayOfWeek();
        return day.getDisplayName(TextStyle.FULL, locale);
    }
}
