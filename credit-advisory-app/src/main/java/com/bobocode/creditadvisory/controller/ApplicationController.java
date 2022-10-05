package com.bobocode.creditadvisory.controller;

import com.bobocode.creditadvisory.dto.AssignmentRequest;
import com.bobocode.creditadvisory.dto.AssignmentResult;
import com.bobocode.creditadvisory.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(final ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/assignments")
    public ResponseEntity<AssignmentResult> assignApplication(@RequestBody final AssignmentRequest request) {
        final AssignmentResult assignmentResult = this.applicationService.assignApplication(request.getAdvisorId());
        return ResponseEntity.ok().body(assignmentResult);
    }
}
