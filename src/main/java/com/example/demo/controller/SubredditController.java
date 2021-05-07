package com.example.demo.controller;

import com.example.demo.dto.SubredditDto;
import com.example.demo.model.Subreddit;
import com.example.demo.service.SubredditService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j

public class SubredditController {

    @Autowired
    private SubredditService ss;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        System.out.println("sunreduttt111111111111111Controller");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ss.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<List<Subreddit>> getAllSubreddits() {
        System.out.println("sunreduttt22222222222222Controller");
       // System.out.println(ss.getAll()+"============subgetalllllllllllcontroller");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ss.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
        System.out.println("sunreduttt3333333333333333Controller");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ss.getSubreddit(id));
    }
}