package com.example.app.attendance.presetation;

import com.example.app.attendance.application.AttendanceService;
import com.example.app.attendance.presetation.dto.response.AttendanceResponse;
import com.example.app.core.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "출퇴근")
@RestController
@RequiredArgsConstructor
@RequestMapping("{farmId}/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "출근")
    @PutMapping("clock-in")
    public ResponseEntity<AttendanceResponse> clockIn(@PathVariable("farmId") Long farmId,
                                                      Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.clockIn(farmId, userId));
    }

    @Operation(summary = "퇴근")
    @PutMapping("clock-out")
    public ResponseEntity<AttendanceResponse> clockOut(@PathVariable("farmId") Long farmId,
                                                       Authentication authentication){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(attendanceService.clockOut(farmId, userId));
    }


    // 내 출퇴근 조회


    // 관리자 - 출퇴근 조회


    // 관리자 - 출퇴근 수정
}
