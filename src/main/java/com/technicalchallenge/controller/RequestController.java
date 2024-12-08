package com.technicalchallenge.controller;

import com.technicalchallenge.model.ApiResponse;
import com.technicalchallenge.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verve")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/accept")
    public ResponseEntity<ApiResponse> acceptRequest(
            @RequestParam("id") int id,
            @RequestParam(value = "endpoint", required = false) String endpoint) {
        try {
            requestService.processRequest(id, endpoint);
            return ResponseEntity.ok(new ApiResponse("ok"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("failed"));
        }
    }
}

