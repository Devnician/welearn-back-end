package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.ruse.welearn.welearn.model.Resource;
import uni.ruse.welearn.welearn.repository.ResourceRepository;
import uni.ruse.welearn.welearn.util.WeLearnException;

import java.util.Optional;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class ResourceService {

    @Autowired
    ResourceRepository resourceRepository;

    public Resource findById(String id) throws WeLearnException {
        Optional<Resource> optionalResource = resourceRepository.findById(id);
        if (optionalResource.isEmpty()) {
            throw new WeLearnException("Resource with id " + id + " not found.");
        }
        return optionalResource.get();
    }
}
