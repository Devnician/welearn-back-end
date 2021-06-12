package uni.ruse.welearn.welearn;

import org.h2.util.IOUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.EvaluationMark;
import uni.ruse.welearn.welearn.model.Event;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.Resource;
import uni.ruse.welearn.welearn.model.Role;
import uni.ruse.welearn.welearn.model.Schedule;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.model.dto.DisciplineDto;
import uni.ruse.welearn.welearn.model.dto.EvaluationMarkDto;
import uni.ruse.welearn.welearn.model.dto.EventDto;
import uni.ruse.welearn.welearn.model.dto.GroupDto;
import uni.ruse.welearn.welearn.model.dto.ResourceDto;
import uni.ruse.welearn.welearn.model.dto.RoleDto;
import uni.ruse.welearn.welearn.model.dto.ScheduleDto;
import uni.ruse.welearn.welearn.model.dto.UserDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EvaluationMarkService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.service.RoleService;
import uni.ruse.welearn.welearn.service.ScheduleService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.JwtTokenUtil;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WeLearnApplicationTests {

    @Autowired
    private RoleService roleService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private EvaluationMarkService evaluationMarkService;
    @Autowired
    private EventService eventService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @Order(1)
    void createRole() {
        RoleDto roleDto = RoleDto.builder()
                .role("Admin")
                .id(1)
                .description("admin")
                .build();

        roleService.saveRole(new Role(roleDto, userService));
        assertEquals(roleService.findRoleById(1).getRole(), roleDto.getRole());
    }

    @Test
    @Order(2)
    void createUser() throws WeLearnException {
        UserDto newUser = UserDto.builder()
                .email("example@email.com")
                .firstName("Ivan")
                .middleName("Marinov")
                .lastName("Petrov")
                .username("username123")
                .password("Test123+")
                .address("address")
                .birthdate(Timestamp.from(Instant.now()))
                .phoneNumber("0896664546")
                .loggedIn(0)
                .deleted(0)
                .role(RoleDto.builder()
                        .id(1)
                        .role("Admin")
                        .build())
                .build();
        userService.addUser(new User(newUser, groupService, disciplineService, userService, eventService));
        assertEquals(userService.findOne("username123").getUsername(), newUser.getUsername());
    }

    @Test
    @Order(3)
    void updateRole() {
        RoleDto roleDto = RoleDto.builder()
                .role("SecondAdmin")
                .id(10)
                .description("admin")
                .build();
        Role roleSecond = roleService.saveRole(new Role(roleDto, userService));
        roleSecond.setDescription("NewDescription");
        roleService.updateRole(roleSecond);
        assertEquals("NewDescription", roleService.findRoleByName("SecondAdmin").getDescription());
    }

    @Test
    @Order(4)
    void getRole() {
        assertNotNull(roleService.findRoleByName("SecondAdmin"));
    }

    @Test
    @Order(5)
    void deleteRole() {
        long id = roleService.findRoleByName("SecondAdmin").getId();
        roleService.deleteRoleById(id);
        assertNull(roleService.findRoleById(id));
    }

    @Test
    @Order(6)
    void getUser() throws WeLearnException {
        assertNotNull(userService.findUserById(userService.findOne("username123").getUserId()));
    }

    @Test
    @Order(7)
    void getUserByUsername() {
        assertNotNull(userService.findOne("username123"));
    }

    @Test
    @Order(8)
    void getUserNegative() {
        assertNull(userService.findOne("invalidId"));
    }

    @Test
    @Order(9)
    void getUserByIdNegative() {
        assertThrows(WeLearnException.class, () -> {
            userService.findUserById("invalidId");
        });
    }

    @Test
    @Order(10)
    void getRoleNegative() {
        assertNull(roleService.findRoleById(10000));
    }

    @Test
    @Order(11)
    void getRoleByNameNegative() {
        assertNull(roleService.findRoleByName("invalidName"));
    }

    @Test
    @Order(12)
    void getAllRoles() {
        assertEquals(1, roleService.findAllRoles().size());
    }

    @Test
    @Order(13)
    void getAllUsers() {
        assertEquals(1, userService.findAllUsers().size());
    }

    @Test
    @Order(14)
    void createGroup() throws WeLearnException {
        GroupDto groupDto = GroupDto.builder()
                .description("Desc")
                .name("group")
                .maxResourcesMb(100)
                .startDate(Timestamp.from(Instant.now()))
                .endDate(Timestamp.from(Instant.now()))
                .build();
        Group group = groupService.save(new Group(groupDto, scheduleService, disciplineService, groupService, resourceService, userService, eventService));
        assertEquals(group.getName(), groupService.findOne(group.getGroupId()).getName());
    }

    @Test
    @Order(15)
    void getGroup() {
        assertFalse(groupService.findAll().isEmpty());
    }

    @Test
    @Order(16)
    void login() {
        User user = userService.findOne("username123");
        userService.login(user);
        assertEquals(1, userService.findOne("username123").getLoggedIn());
    }

    @Test
    @Order(17)
    void updateGroup() throws WeLearnException {
        Group group = getGroupFromRepo();
        group.setDescription("newDesc");
        group.setStartDate(Timestamp.from(Instant.now()));
        group.setEndDate(Timestamp.from(Instant.now()));
        group.setName("newName");
        group.setMaxResourcesMb(200);
        groupService.edit(group);
        assertEquals("newName", groupService.findOne(group.getGroupId()).getName());
    }

    @Test
    @Order(18)
    void updateUser() throws WeLearnException {
        Group group = getGroupFromRepo();
        User user = userService.findOne("username123");
        user.setPassword("NewPassword123+");
        user.setEmail("newEmail@email.com");
        user.setBirthdate(Timestamp.from(Instant.now()));
        user.setPhoneNumber("0896664547");
        user.setFirstName("New");
        user.setMiddleName("New");
        user.setLastName("New");
        user.setGroup(group);
        userService.updateUser(user);
        assertEquals("newEmail@email.com", userService.findOne("username123").getEmail());
    }

    @Test
    @Order(19)
    void createDiscipline() throws WeLearnException {
        Group group = getGroupFromRepo();
        DisciplineDto disciplineDto = DisciplineDto.builder()
                .id("")
                .name("discipline")
                .build();
        Discipline discipline = disciplineService.createDiscipline(new Discipline(disciplineDto, groupService, disciplineService, resourceService, userService, eventService));
        group.setDisciplines(Set.of(discipline));
        groupService.edit(group);
        assertEquals(disciplineDto.getName(), disciplineService.findOne(discipline.getId()).getName());
    }

    @Test
    @Order(20)
    void editDiscipline() throws WeLearnException {
        Discipline discipline = getDisciplineFromRepo();
        discipline.setName("test");
        disciplineService.editDiscipline(discipline);
        assertEquals("test", getDisciplineFromRepo().getName());
    }

    @Test
    @Order(21)
    void createEvaluationMark() throws WeLearnException {
        EvaluationMarkDto evaluationMarkDto = EvaluationMarkDto.builder()
                .id("")
                .disciplineId(getDisciplineFromRepo().getId())
                .userId(userService.findOne("username123").getUserId())
                .markValue(BigDecimal.valueOf(6d))
                .groupId(getGroupFromRepo().getGroupId())
                .build();
        EvaluationMark evaluationMark = evaluationMarkService.save(new EvaluationMark(
                evaluationMarkDto, groupService, disciplineService, userService
        ));
        assertEquals(userService.findOne("username123").getEvaluationMarks().stream().iterator().next().getMarkValue().setScale(2), evaluationMark.getMarkValue().setScale(2));
    }

    @Test
    @Order(22)
    void editEvaluationMark() throws WeLearnException {
        EvaluationMark evaluationMark = userService.findOne("username123").getEvaluationMarks().stream().iterator().next();
        evaluationMark.setMarkValue(BigDecimal.valueOf(5d));
        EvaluationMark evaluationMark1 = evaluationMarkService.edit(evaluationMark);
        assertEquals(evaluationMark1.getMarkValue(), BigDecimal.valueOf(5d));
    }

    @Test
    @Order(23)
    void createSchedule() throws WeLearnException {
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .startHour(Time.valueOf(LocalTime.MIN))
                .endHour(Time.valueOf(LocalTime.NOON))
                .disciplineId(getDisciplineFromRepo().getId())
                .groupId(getGroupFromRepo().getGroupId())
                .dayOfWeek("Monday")
                .startDate(Timestamp.from(Instant.now().minusSeconds(31556926)))
                .endDate(Timestamp.from(Instant.now()))
                .build();
        Schedule schedule = scheduleService.save(new Schedule(
                scheduleDto, groupService, disciplineService, resourceService
        ));
        assertEquals(schedule.getId(), getGroupFromRepo().getSchedules().iterator().next().getId());
    }

    @Test
    @Order(24)
    void editSchedule() throws WeLearnException {
        Schedule schedule = getGroupFromRepo().getSchedules().stream().iterator().next();
        schedule.setDayOfWeek("Tuesday");
        Schedule editedSchedule = scheduleService.edit(schedule);
        assertEquals("Tuesday", editedSchedule.getDayOfWeek());
    }

    @Test
    @Order(25)
    void generateEvents() throws WeLearnException {
        Schedule schedule = getGroupFromRepo().getSchedules().iterator().next();
        scheduleService.generateEvents(schedule.getId());
        assertFalse(eventService.findAll().isEmpty());
    }

    @Test
    @Order(26)
    void createEvent() throws WeLearnException {
        eventService.findAll().forEach(it -> {
            try {
                eventService.delete(it.getEventId());
            } catch (WeLearnException e) {
                e.printStackTrace();
            }
        });

        Discipline discipline = getDisciplineFromRepo();
        EventDto eventDto = EventDto.builder()
                .name("test")
                .startDate(Timestamp.from(Instant.now()))
                .endDate(Timestamp.from(Instant.now()))
                .type("Class")
                .description("desc")
                .groupId(getGroupFromRepo().getGroupId())
                .discipline(new DisciplineDto(discipline))
                .build();
        Event event = eventService.save(new Event(
                eventDto, groupService, disciplineService, userService, eventService, resourceService
        ));
        assertEquals("test", event.getName());
    }

    @Test
    @Order(27)
    void editEvent() throws WeLearnException {
        Event event = getGroupFromRepo().getEvents().iterator().next();
        event.setName("event");
        eventService.edit(event);
        assertEquals("event", getGroupFromRepo().getEvents().iterator().next().getName());
    }

    @Test
    @Order(28)
    void createResource() throws WeLearnException {
        Resource resource = resourceService.save(
                new MultipartFile() {
                    @Override
                    public String getName() {
                        return "file";
                    }

                    @Override
                    public String getOriginalFilename() {
                        return "file";
                    }

                    @Override
                    public String getContentType() {
                        return "application/pdf";
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public long getSize() {
                        return 0;
                    }

                    @Override
                    public byte[] getBytes() throws IOException {
                        return new byte[0];
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return IOUtils.getInputStreamFromString("some text");
                    }

                    @Override
                    public void transferTo(File file) throws IOException, IllegalStateException {

                    }
                },
                getGroupFromRepo(),
                getDisciplineFromRepo().getId(),
                "missing",
                true
        );
        assertEquals("file", resource.getName());
    }

    @Test
    @Order(29)
    void testToken() {
        User user = userService.findOne("username123");
        String token = jwtTokenUtil.generateToken(user, user.getRole());
        assertEquals(user.getUsername(), jwtTokenUtil.getUsernameFromToken(token));
    }

    @Test
    @Order(29)
    void testDto() throws WeLearnException {
        try {
            Group group = getGroupFromRepo();
            GroupDto groupDto = new GroupDto(group);
            new Group(
                    groupDto, scheduleService, disciplineService, groupService, resourceService, userService, eventService
            );
            User user = userService.findOne("username123");
            UserDto userDto = new UserDto(user);
            new User(
                    userDto, groupService, disciplineService, userService, eventService
            );
            Discipline discipline = getDisciplineFromRepo();
            DisciplineDto disciplineDto = new DisciplineDto(discipline);
            new Discipline(
                    disciplineDto, groupService, disciplineService, resourceService, userService, eventService
            );
            EvaluationMark evaluationMark = user.getEvaluationMarks().iterator().next();
            EvaluationMarkDto evaluationMarkDto = new EvaluationMarkDto(evaluationMark);
            new EvaluationMark(
                    evaluationMarkDto, groupService, disciplineService, userService
            );
            Event event = group.getEvents().iterator().next();
            EventDto eventDto = new EventDto(event);
            new Event(
                    eventDto, groupService, disciplineService, userService, eventService, resourceService
            );
            Resource resource = discipline.getResources().iterator().next();
            ResourceDto resourceDto = new ResourceDto(resource);
            new Resource(
                    resourceDto, scheduleService, disciplineService, groupService
            );
            Role role = user.getRole();
            RoleDto roleDto = new RoleDto(role);
            new Role(
                    roleDto, userService
            );
            Schedule schedule = group.getSchedules().iterator().next();
            ScheduleDto scheduleDto = new ScheduleDto(schedule);
            new Schedule(
                    scheduleDto, groupService, disciplineService, resourceService
            );
            assertEquals("username123", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            assertNotNull(null);
        }
    }

    @Test
    @Order(30)
    void editResource() throws WeLearnException {
        Resource resource = getResourceFromRepo();
        resourceService.edit(
                new MultipartFile() {
                                 @Override
                                 public String getName() {
                                     return "editedFile";
                                 }

                                 @Override
                                 public String getOriginalFilename() {
                                     return "editedFile";
                                 }

                                 @Override
                                 public String getContentType() {
                                     return "application/pdf";
                                 }

                                 @Override
                                 public boolean isEmpty() {
                                     return false;
                                 }

                                 @Override
                                 public long getSize() {
                                     return 0;
                                 }

                                 @Override
                                 public byte[] getBytes() throws IOException {
                                     return new byte[0];
                                 }

                                 @Override
                                 public InputStream getInputStream() throws IOException {
                                     return IOUtils.getInputStreamFromString("some text");
                                 }

                                 @Override
                                 public void transferTo(File file) throws IOException, IllegalStateException {

                                 }
                             },
                getGroupFromRepo(),
                getDisciplineFromRepo().getId(),
                "missing",
                resource.getResourceId(),
                true);
        assertEquals("newName", getGroupFromRepo().getName());
    }

    @Test
    @Order(31)
    void deleteAll() throws WeLearnException {
        Resource resource = getResourceFromRepo();
        Group group = getGroupFromRepo();
        Discipline discipline = getDisciplineFromRepo();
        resourceService.delete(resource.getResourceId(), group);
        eventService.delete(group.getEvents().iterator().next().getEventId());
        scheduleService.delete(group.getSchedules().iterator().next().getId());
        evaluationMarkService.delete(group.getUsers().iterator().next().getEvaluationMarks().iterator().next().getId());
        Discipline halvedDiscipline = getDisciplineFromRepo();
        halvedDiscipline.setGroup(null);
        disciplineService.editDiscipline(halvedDiscipline);
        Group halvedGroup = getGroupFromRepo();
        halvedGroup.setDisciplines(null);
        groupService.edit(halvedGroup);
        disciplineService.removeDiscipine(discipline.getId());
        userService.findAllUsers().forEach(user -> {
            try {
                user.setGroup(null);
                userService.updateUser(user);
                userService.deleteUser(user.getUserId());
            } catch (WeLearnException e) {
                e.printStackTrace();
            }
        });
        roleService.deleteRoleById(group.getUsers().iterator().next().getRole().getId());
        groupService.delete(halvedGroup.getGroupId());
        assertThrows(WeLearnException.class, () -> {
            userService.findUserById("username123");
        });
    }


    private Resource getResourceFromRepo() {
        Optional<Resource> optionalResource = resourceService.findAll().stream().findFirst();
        Resource resource = null;
        if (optionalResource.isPresent()) {
            resource = optionalResource.get();
        }
        assert resource != null;
        return resource;
    }

    private Discipline getDisciplineFromRepo() {
        Optional<Discipline> optionalDiscipline = disciplineService.getDisciplines().stream().findFirst();
        Discipline discipline = null;
        if (optionalDiscipline.isPresent()) {
            discipline = optionalDiscipline.get();
        }
        assert discipline != null;
        return discipline;
    }

    private Group getGroupFromRepo() {
        Optional<Group> optionalGroup = groupService.findAll().stream().findFirst();
        Group group = null;
        if (optionalGroup.isPresent()) {
            group = optionalGroup.get();
        }
        assert group != null;
        return group;
    }
}
