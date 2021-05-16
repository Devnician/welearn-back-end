package uni.ruse.welearn.welearn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uni.ruse.welearn.welearn.model.Discipline;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.Resource;
import uni.ruse.welearn.welearn.model.Schedule;
import uni.ruse.welearn.welearn.model.User;
import uni.ruse.welearn.welearn.repository.ResourceRepository;
import uni.ruse.welearn.welearn.util.JwtTokenUtil;
import uni.ruse.welearn.welearn.util.WeLearnException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.Files.deleteIfExists;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class ResourceService {

    public static final String MISSING = "missing";
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserService userService;
    @Autowired
    DisciplineService disciplineService;
    @Autowired
    ScheduleService scheduleService;

    public Resource findById(String id) throws WeLearnException {
        Optional<Resource> optionalResource = resourceRepository.findById(id);
        if (optionalResource.isEmpty()) {
            throw new WeLearnException("Resource with id " + id + " not found.");
        }
        return optionalResource.get();
    }

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    public Resource save(MultipartFile file, HttpServletRequest req, String disciplineId, String scheduleId, Boolean accessibleAll) throws WeLearnException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Group group = userService.findOne(jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getToken(req))).getGroup();
        Discipline discipline = !disciplineId.equals(MISSING) ? disciplineService.getDisciplineById(disciplineId) : null;
        Schedule schedule = !scheduleId.equals(MISSING) ? scheduleService.findById(scheduleId) : null;
        Path path = Paths.get("/data/resources/");
        resourceRepository.findByDirPath(path.toString()).forEach(it -> {
            log.info("Deleting hanging resource with id " + it.getResourceId());
            resourceRepository.delete(it);
        });
        Resource resource = resourceRepository.save(
                Resource.builder()
                        .name(fileName)
                        .accessibleAll(accessibleAll)
                        .group(group)
                        .type(file.getContentType())
                        .discipline(discipline)
                        .schedule(schedule)
                        .dirPath(path.toString())
                        .build()
        );
        try {
            saveFile(file, resource, path);
        } catch (WeLearnException ex) {
            resourceRepository.delete(resource);
            throw ex;
        }
        return resource;
    }

    private void saveFile(MultipartFile file, Resource resource, Path path) throws WeLearnException {
        try {
            List<String> splitName = Arrays.asList(resource.getName().split("[.]"));
            Path newPath = extractPathFromSplitName(resource, path, splitName);
            resource.setDirPath(newPath.toString());
            resourceRepository.save(resource);
            Files.copy(file.getInputStream(), newPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WeLearnException("File was not saved successfully");
        }
    }

    private Path extractPathFromSplitName(Resource resource, Path path, List<String> splitName) {
        Path newPath;
        try {
            newPath = Paths.get(path.toString(), resource.getResourceId() + "." + splitName.get(splitName.size() - 1));
        } catch (IndexOutOfBoundsException ex) {
            newPath = Paths.get(resource.getResourceId());
        }
        return newPath;
    }

    public Resource edit(MultipartFile file, HttpServletRequest req, String disciplineId, String scheduleId, String resourceId,Boolean accessibleAll) throws WeLearnException {
        Discipline discipline = !disciplineId.equals(MISSING) ? disciplineService.getDisciplineById(disciplineId) : null;
        Schedule schedule = !scheduleId.equals(MISSING) ? scheduleService.findById(scheduleId) : null;
        Resource existingResource = findById(resourceId);
        validateGroup(req, existingResource);
        existingResource.setDiscipline(discipline);
        existingResource.setSchedule(schedule);
        existingResource.setName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
        existingResource.setAccessibleAll(accessibleAll);
        editFile(file, existingResource);
        return existingResource;
    }

    private void editFile(MultipartFile file, Resource resource) throws WeLearnException {
        try {
            List<String> splitName = Arrays.asList(resource.getName().split("[.]"));
            Path newPath = extractPathFromSplitName(resource, Paths.get("/data/resources/"), splitName);
            deleteFileFromStorage(resource);
            Files.copy(file.getInputStream(), newPath);
            resource.setDirPath(newPath.toString());
            resourceRepository.save(resource);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WeLearnException("File was not edited successfully");
        }
    }

    private void validateGroup(HttpServletRequest req, Resource existingResource) throws WeLearnException {
        Group group = userService.findOne(jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getToken(req))).getGroup();
        if (!existingResource.getGroup().getGroupId().equals(group.getGroupId())) {
            throw new WeLearnException("Group of sending user and resource must be matching");
        }
    }

    public void delete(String id, HttpServletRequest req) throws WeLearnException {
        Resource existingResource = findById(id);
        validateGroup(req, existingResource);
        deleteFileFromStorage(existingResource);
        resourceRepository.delete(existingResource);
    }

    private void deleteFileFromStorage(Resource existingResource) throws WeLearnException {
        File existingFile = new File(existingResource.getDirPath());
        try {
            deleteIfExists(existingFile.toPath());
        } catch (IOException ex) {
            throw new WeLearnException("The file was not deleted successfully");
        }
    }

    public org.springframework.core.io.Resource download(Resource resource, HttpServletRequest req) throws WeLearnException {
        User user = userService.findOne(jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getToken(req)));
        return getResourceByDisciplineOrThrowException(resource, user);
    }

    private org.springframework.core.io.Resource getResourceByDisciplineOrThrowException(Resource resource, User user) throws WeLearnException {
        var ref = new Object() {
            org.springframework.core.io.Resource foundResource = null;
        };
        user.getAssistedDiscipline().forEach(it -> {
            if (it.getId().equals(resource.getDiscipline().getId())) {
                try {
                    ref.foundResource = getUrlResource(resource);
                } catch (WeLearnException ignored) {
                }
            }
        });
        user.getTaughtDiscipline().forEach(it -> {
            if (it.getId().equals(resource.getDiscipline().getId()) && ref.foundResource == null) {
                try {
                    ref.foundResource = getUrlResource(resource);
                } catch (WeLearnException ignored) {
                }
            }
        });
        user.getGroup().getDisciplines().forEach(it -> {
            if (it.getId().equals(resource.getDiscipline().getId()) && ref.foundResource == null) {
                try {
                    ref.foundResource = getUrlResource(resource);
                } catch (WeLearnException ignored) {

                }
            }
        });
        if (user.getGroup().getGroupId().equals(resource.getGroup().getGroupId())) {
            ref.foundResource = getUrlResource(resource);
        }
        if (ref.foundResource != null) {
            return getUrlResource(resource);
        }
        throw new WeLearnException("User does not have rights to the resource");
    }

    private UrlResource getUrlResource(Resource resource) throws WeLearnException {
        try {
            return new UrlResource(Paths.get(resource.getDirPath()).toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new WeLearnException("Cannot download the file");
        }
    }
}
