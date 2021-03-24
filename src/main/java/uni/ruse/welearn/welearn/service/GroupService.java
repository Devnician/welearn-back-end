package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.repository.GroupRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

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
        if(group.isEmpty()){
            throw new WeLearnException("Group with id " + groupId + " not found.");
        }
        return group.get();
    }
}
