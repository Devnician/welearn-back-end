package uni.ruse.welearn.welearn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uni.ruse.welearn.welearn.model.Group;
import uni.ruse.welearn.welearn.model.Resource;
import uni.ruse.welearn.welearn.model.dto.ResourceDto;
import uni.ruse.welearn.welearn.service.DisciplineService;
import uni.ruse.welearn.welearn.service.GroupService;
import uni.ruse.welearn.welearn.service.ResourceService;
import uni.ruse.welearn.welearn.service.ScheduleService;
import uni.ruse.welearn.welearn.service.UserService;
import uni.ruse.welearn.welearn.util.JwtTokenUtil;
import uni.ruse.welearn.welearn.util.WeLearnException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/resource")
public class ResourceController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDto> getById(
            @PathVariable String id
    ) throws WeLearnException {
        return new ResponseEntity<>(new ResourceDto(resourceService.findById(id)), HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadResource(
            @PathVariable String id,
            HttpServletRequest req
    ) throws WeLearnException {
        Resource resource = resourceService.findById(id);
        org.springframework.core.io.Resource file = resourceService.download(resource, req);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(resource.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getName() + "\"")
                .body(file);
    }

    @GetMapping()
    public ResponseEntity<List<ResourceDto>> findAll() {
        return new ResponseEntity<>(resourceService.findAll().stream().map(ResourceDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResourceDto> save(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "disciplineId", required = false, defaultValue = "missing") String disciplineId,
            @RequestParam(value = "eventId", required = false, defaultValue = "missing") String eventId,
            @RequestParam(value = "accessibleAll", required = false, defaultValue = "true") Boolean accessibleAll,
            HttpServletRequest req
    ) throws WeLearnException {
        checkOnlyOneOptionalParam(disciplineId, eventId);
        return new ResponseEntity<>(new ResourceDto(resourceService.save(file, getGroupFromRequest(req), disciplineId, eventId, accessibleAll)
        ), HttpStatus.OK);
    }

    private void checkOnlyOneOptionalParam(String disciplineId, String scheduleId) throws WeLearnException {
        try {
            Map<String, String> optionalParams = Map.of("disciplineId", disciplineId, "scheduleId", scheduleId);
            List<String> params = optionalParams.values().stream().filter(it -> !it.equals("missing")).collect(Collectors.toList());
            if (params.size() > 1) {
                throw new WeLearnException("Only one optional parameter may be parsed");
            }
        } catch (NullPointerException ignored) {
        }
    }

    @PutMapping("/upload")
    public ResponseEntity<ResourceDto> edit(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "disciplineId", required = false, defaultValue = "missing") String disciplineId,
            @RequestParam(value = "eventId", required = false, defaultValue = "missing") String eventId,
            @RequestParam(value = "accessibleAll", required = false, defaultValue = "true") Boolean accessibleAll,
            @RequestParam(value = "resourceId") String resourceId,
            HttpServletRequest req
    ) throws WeLearnException {
        checkOnlyOneOptionalParam(disciplineId, eventId);
        return new ResponseEntity<>(new ResourceDto(resourceService.edit(file, getGroupFromRequest(req), disciplineId, eventId, resourceId, accessibleAll)
        ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(
            @PathVariable String id,
            HttpServletRequest req
    ) throws WeLearnException {
        resourceService.delete(id, getGroupFromRequest(req));
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    private Group getGroupFromRequest(HttpServletRequest req) {
        return userService.findOne(jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getToken(req))).getGroup();
    }
}
