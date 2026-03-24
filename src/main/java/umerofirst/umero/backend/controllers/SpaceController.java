package umerofirst.umero.backend.controllers;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import umerofirst.umero.backend.models.Space;
import umerofirst.umero.backend.repositories.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spaces")
@CrossOrigin(origins = "*")
public class SpaceController {

    @Autowired
    private SpaceRepository spaceRepository;

    // ✅ GET all spaces with filters + caching
    @GetMapping
    @Cacheable(value = "spaces",
            key = "(#activity != null ? #activity : 'any') + '_' + (#location != null ? #location : 'all')")
    public List<Space> getSpaces(
            @RequestParam(required = false) String activity,
            @RequestParam(required = false) String location
    ) {
        try {
            List<Space> spaces = spaceRepository.findByIsActiveTrue();

            // 🔥 FIXED ACTIVITY FILTER
            if (activity != null && !activity.equalsIgnoreCase("any")) {
                spaces = spaces.stream()
                        .filter(s -> s.getActivities() != null &&
                                s.getActivities().stream()
                                        .anyMatch(a -> a.equalsIgnoreCase(activity)))
                        .collect(Collectors.toList());
            }

            // 🔥 FIXED LOCATION FILTER
            if (location != null && !location.isEmpty()) {
                spaces = spaces.stream()
                        .filter(s -> s.getCity() != null &&
                                s.getCity().equalsIgnoreCase(location))
                        .collect(Collectors.toList());
            }

            return spaces;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // ✅ NEVER break frontend
        }
    }

    // ✅ GET single space by id
    @GetMapping("/{id}")
    @Cacheable(value = "space", key = "#id")
    public ResponseEntity<Space> getSpace(@PathVariable String id) {
        return spaceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST create space
    @PostMapping
    @CacheEvict(value = {"spaces", "space"}, allEntries = true)
    public Space createSpace(@RequestBody Space space) {
        return spaceRepository.save(space);
    }
}