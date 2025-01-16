package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.NotificationService;
import fitness_app_be.fitness_app.domain.Notification;
import fitness_app_be.fitness_app.domain.WorkoutStatus;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutStatusServiceImplTest {

    @Mock
    private WorkoutStatusRepository workoutStatusRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private WorkoutStatusServiceImpl workoutStatusService;

    private WorkoutStatus workoutStatus;
    private Notification notification;

    @BeforeEach
    void setUp() {
        workoutStatus = new WorkoutStatus(1L, null, null, false, 12);
        notification = new Notification(1L, "Nice job!", false, 1L, LocalDate.now().atStartOfDay());
    }

    @Test
    void findByWorkoutPlanIdAndWorkoutId_ShouldReturnWorkoutStatus_WhenFound() {
        when(workoutStatusRepository.findByWorkoutPlanIdAndWorkoutId(1L, 1L)).thenReturn(Optional.of(workoutStatus));

        WorkoutStatus result = workoutStatusService.findByWorkoutPlanIdAndWorkoutId(1L, 1L);

        assertNotNull(result);
        assertEquals(workoutStatus, result);
    }

    @Test
    void findByWorkoutPlanIdAndWorkoutId_ShouldThrowException_WhenNotFound() {
        when(workoutStatusRepository.findByWorkoutPlanIdAndWorkoutId(1L, 1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> workoutStatusService.findByWorkoutPlanIdAndWorkoutId(1L, 1L));
        assertEquals("Workout status not found for plan ID 1 and workout ID 1", exception.getMessage());
    }

    @Test
    void markWorkoutAsDone_ShouldUpdateStatusAndCreateNotification() {
        when(workoutStatusRepository.findByWorkoutPlanIdAndWorkoutId(1L, 1L)).thenReturn(Optional.of(workoutStatus));
        when(notificationService.createNotification(eq(1L), anyString())).thenReturn(notification);

        Notification result = workoutStatusService.markWorkoutAsDone(1L, 1L, 1L);

        assertNotNull(result);
        assertEquals(notification, result);
        assertTrue(workoutStatus.getIsDone());
        verify(workoutStatusRepository, times(1)).update(workoutStatus);
        verify(notificationService, times(1)).createNotification(eq(1L), anyString());
    }

    @Test
    void getWorkoutStatusesForPlan_ShouldReturnWorkoutStatuses() {
        when(workoutStatusRepository.findByWorkoutPlanId(1L)).thenReturn(List.of(workoutStatus));

        List<WorkoutStatus> results = workoutStatusService.getWorkoutStatusesForPlan(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void resetWeeklyWorkouts_ShouldResetAllWorkouts() {
        when(workoutStatusRepository.findAll()).thenReturn(List.of(workoutStatus));

        workoutStatusService.resetWeeklyWorkouts(13);

        assertFalse(workoutStatus.getIsDone());
        assertEquals(13, workoutStatus.getWeekNumber());
        verify(workoutStatusRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getCompletedWorkouts_ShouldReturnCount() {
        when(workoutStatusRepository.countCompletedWorkoutsByWeekRange(eq(1L), anyInt(), anyInt())).thenReturn(5L);

        Long completedWorkouts = workoutStatusService.getCompletedWorkouts(1L, "quarter");

        assertNotNull(completedWorkouts);
        assertEquals(5, completedWorkouts);
    }
}