package uni.ruse.welearn.welearn.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.BeanUtils;
import uni.ruse.welearn.welearn.model.dto.GroupDto;
import uni.ruse.welearn.welearn.model.dto.GroupIdDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.EventService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.service.ScheduleService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.AuditedClass;
import uni.ruse.welearn.welearn.util.WeLearnException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ivelin Dimitrov
 */
@Entity(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class Group extends AuditedClass {
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<User> users;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Event> events;

    @ManyToMany
    @JoinTable(
            name = "discipline_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id"))
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Discipline> disciplines;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private Set<Resource> resources;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "uuid2"
    )
    private String groupId;
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;
    @NotNull(message = "Max resources is mandatory")
    private Integer maxResourcesMb;
    @NotNull(message = "start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp startDate;
    @NotNull(message = "start date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp endDate;

    public Group(
            GroupDto groupDto,
            ScheduleService scheduleService,
            DisciplineService disciplineService,
            GroupService groupService,
            ResourceService resourceService,
            UserService userService,
            EventService eventService
    ) {
        if (groupDto != null) {
            BeanUtils.copyProperties(groupDto, this);
            if (groupDto.getResources() != null) {
                resources = groupDto.getResources().stream().map(it -> {
                    try {
                        return new Resource(it, eventService, disciplineService, groupService);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
            if (groupDto.getDisciplines() != null) {
                disciplines = groupDto.getDisciplines().stream().map(it -> {
                    try {
                        return new Discipline(it, groupService, disciplineService, resourceService, userService, eventService);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
            if (groupDto.getEvents() != null) {
                events = groupDto.getEvents().stream().map(it -> {
                    try {
                        return new Event(it, groupService, disciplineService, userService, eventService, resourceService);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
            if (groupDto.getSchedules() != null) {
                schedules = groupDto.getSchedules().stream().map(it -> {
                    try {
                        return new Schedule(it, groupService, disciplineService, resourceService);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
            if (groupDto.getUsers() != null) {
                users = groupDto.getUsers().stream().map(it -> {
                    try {
                        return new User(it, groupService, disciplineService, userService, eventService);
                    } catch (WeLearnException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet());
            }
        }
    }

    public Group(
            GroupIdDto groupIdDto,
            ScheduleService scheduleService,
            DisciplineService disciplineService,
            GroupService groupService,
            ResourceService resourceService,
            UserService userService,
            EventService eventService
    ) throws WeLearnException {
        User student = userService.findUserById(groupIdDto.getStudentId());
        Group group = groupService.findOne(groupIdDto.getGroupId());
        BeanUtils.copyProperties(group, this);
        if (groupIdDto.isRemove()) {
            student.setGroup(null);
            users.remove(student);
        } else {
            student.setGroup(group);
            users.add(student);
        }
        userService.updateUserWithoutPassword(student);
    }
}
