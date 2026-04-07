package com.example.app.attendance.presetation;

import com.example.app.attendance.application.AttendanceService;
import com.example.app.attendance.presetation.dto.response.AttendanceResponse;
import com.example.app.core.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @PutMapping("/clock-in")
    public ResponseEntity<AttendanceResponse> clockIn(@PathVariable("farmId") Long farmId,
                                                      Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.clockIn(farmId, userId));
    }

    @Operation(summary = "퇴근")
    @PutMapping("/clock-out")
    public ResponseEntity<AttendanceResponse> clockOut(@PathVariable("farmId") Long farmId,
                                                       Authentication authentication){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.clockOut(farmId, userId));
    }

    // 내 출퇴근 조회
    @Operation(summary = "내 출퇴근 조회")
    @GetMapping("/me")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendances(@PathVariable("farmId") Long farmId,
                                                                  @RequestParam LocalDate from,
                                                                  @RequestParam LocalDate to,
                                                                  Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.getMyAttendances(farmId, userId, from, to));
    }

    // 관리자 - 출퇴근 조회


    // 관리자 - 출퇴근 수정
}
