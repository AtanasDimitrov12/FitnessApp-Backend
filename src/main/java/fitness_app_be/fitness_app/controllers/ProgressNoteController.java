package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.ProgressNoteService;
import fitness_app_be.fitness_app.controllers.dto.ProgressNoteDTO;
import fitness_app_be.fitness_app.controllers.mapper.ProgressNoteMapper;
import fitness_app_be.fitness_app.domain.ProgressNote;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress-notes")
@RequiredArgsConstructor
public class ProgressNoteController {

    private final ProgressNoteService progressNoteService;
    private final ProgressNoteMapper progressNoteMapper;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<ProgressNoteDTO> getAllProgressNotes() {
        return progressNoteService.getAllProgressNotes().stream()
                .map(progressNoteMapper::domainToDto)
                .toList();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ProgressNoteDTO getProgressNoteById(@PathVariable Long id) {
        ProgressNote progressNote = progressNoteService.getProgressNoteById(id);
        return progressNoteMapper.domainToDto(progressNote);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<ProgressNoteDTO> getProgressNotesByUserId(@PathVariable Long userId) {
        return progressNoteService.getProgressNotesByUserId(userId).stream()
                .map(progressNoteMapper::domainToDto)
                .toList();
    }



    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ProgressNoteDTO createProgressNote(@RequestBody ProgressNoteDTO progressNoteDTO) {
        ProgressNote progressNote = progressNoteMapper.toDomain(progressNoteDTO);
        ProgressNote createdNote = progressNoteService.createProgressNote(progressNote);
        return progressNoteMapper.domainToDto(createdNote);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping
    public ProgressNoteDTO updateProgressNote(@RequestBody ProgressNoteDTO progressNoteDTO) {
        ProgressNote progressNote = progressNoteMapper.toDomain(progressNoteDTO);
        ProgressNote updatedNote = progressNoteService.updateProgressNote(progressNote);
        return progressNoteMapper.domainToDto(updatedNote);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgressNote(@PathVariable Long id) {
        progressNoteService.deleteProgressNote(id);
        return ResponseEntity.noContent().build();
    }
}
