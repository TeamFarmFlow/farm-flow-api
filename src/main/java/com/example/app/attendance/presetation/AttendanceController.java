package com.example.app.attendance.presetation;

import com.example.app.attendance.application.AttendanceService;
import com.example.app.attendance.presetation.dto.request.AttendanceUpdateRequest;
import com.example.app.attendance.presetation.dto.response.AttendanceResponse;
import com.example.app.core.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "출퇴근")
@RestController
@RequiredArgsConstructor
@RequestMapping("{farmId}/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "출근")
    @PostMapping("/clock-in")
    public ResponseEntity<AttendanceResponse> clockIn(@PathVariable("farmId") Long farmId,
                                                      Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.clockIn(farmId, userId));
    }

    @Operation(summary = "퇴근")
    @PostMapping("/clock-out")
    public ResponseEntity<AttendanceResponse> clockOut(@PathVariable("farmId") Long farmId,
                                                       Authentication authentication){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.clockOut(farmId, userId));
    }

    @Operation(summary = "내 출퇴근 조회")
    @GetMapping("/me")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendances(@PathVariable("farmId") Long farmId,
                                                                  @RequestParam LocalDate from,
                                                                  @RequestParam LocalDate to,
                                                                  Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.getMyAttendances(farmId, userId, from, to));
    }

    @Operation(summary = "관리자용 출퇴근 조회")
    @GetMapping()
    public ResponseEntity<List<AttendanceResponse>> getAttendances(@PathVariable("farmId") Long farmId,
                                                                   @RequestParam LocalDate from,
                                                                   @RequestParam LocalDate to,
                                                                   Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.getAttendances(farmId, userId, from, to));
    }

    @Operation(summary = "출퇴근 수정")
    @PatchMapping("/{attendanceId}")
    public ResponseEntity<AttendanceResponse> updateAttendance(@PathVariable("farmId") Long farmId,
                                                               @PathVariable("attendanceId") Long id,
                                                               @Valid @RequestBody AttendanceUpdateRequest request,
                                                               Authentication authentication){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.updateAttendance(id, farmId, userId, request.toCommand()));
    }
}
