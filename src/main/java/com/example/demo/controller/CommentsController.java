package com.example.demo.controller;

import com.example.demo.dto.CommentsDto;
import com.example.demo.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto){
        //System.out.println("comenttttttttttttt11111111Controller");
        commentService.save(commentsDto);
        return new ResponseEntity<>(CREATED);

    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable String postId) {
       // System.out.println("comenttttttttttttt222222222Controller");
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName){
        //System.out.println("comenttttttttttttt3333333333Controller");
        //System.out.println(userName+"========userNameuserName1111");
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForUser(userName));
    }


}
