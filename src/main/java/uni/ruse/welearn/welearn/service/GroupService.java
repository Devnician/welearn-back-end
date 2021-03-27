package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.repository.GroupRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.List;
import java.util.Optional;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public Group findOne(String groupId) throws WeLearnException {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new WeLearnException("Group with id " + groupId + " not found.");
        }
        return group.get();
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group save(Group group) throws WeLearnException {
        if (group.getGroupId().isBlank() || group.getGroupId().isEmpty() || group.getGroupId() == null) {
            return groupRepository.save(group);
        }
        throw new WeLearnException("New Group object cannot have an id");
    }

    public Group edit(Group group) throws WeLearnException {
        Group existingGroup = findOne(group.getGroupId());
        existingGroup.setDescription(group.getDescription());
        existingGroup.setStartDate(group.getStartDate());
        existingGroup.setEndDate(group.getEndDate());
        existingGroup.setDisciplines(group.getDisciplines());
        existingGroup.setEvents(group.getEvents());
        existingGroup.setName(group.getName());
        existingGroup.setMaxResourcesMb(group.getMaxResourcesMb());
        existingGroup.setResources(group.getResources());
        existingGroup.setSchedules(group.getSchedules());
        existingGroup.setUsers(group.getUsers());
        return groupRepository.save(existingGroup);
    }

    public void delete(String id) throws WeLearnException {
        Group group = findOne(id);
        groupRepository.delete(group);
    }
}
