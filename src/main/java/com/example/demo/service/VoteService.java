package com.example.demo.service;

import com.example.demo.dto.VoteDto;
import com.example.demo.exceptions.PostNotFoundException;
import com.example.demo.exceptions.SpringRedditException;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.Vote;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.demo.model.VoteType.UPVOTE;

//import static com.programming.techie.springredditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        List<Comment> comment = commentRepository.findByPost(post);
        System.out.println(comment+"commentcommentcommentcomment");
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        System.out.println(post+"posttttttttttttttmathnnaaaaaaa");
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post));

        postRepository.save(post);
        if(!comment.isEmpty() || comment != null) {
            System.out.println("cameeeeeeeeeeeeee insideeeeeeeeeee");
            comment.stream().forEach(cmt->{
                cmt.setPost(post);
                System.out.println(cmt+"cmmmmmmmmmmmmmmmmmmmtttttttttttttttttt");
                commentRepository.save(cmt);

            });
        }

        System.out.println(post+"===============postpostpost222222222222marhannnn");

    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}