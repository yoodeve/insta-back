package com.toyproject.instagram.controller;

import com.toyproject.instagram.dto.UploadFeedReqDto;
import com.toyproject.instagram.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;
    @PostMapping("/feed")
    // 폼데이터이기 때문에 reqBody 생략
    public ResponseEntity<?> uploadFeed(UploadFeedReqDto uploadFeedReqDto) {
        feedService.upload(uploadFeedReqDto);
        return ResponseEntity.ok().body(null);
    }
}
