package com.example.demo.model;

import com.example.demo.exceptions.SpringRedditException;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;


@Document(collection = "voteType" )
public enum VoteType {
        UPVOTE(1), DOWNVOTE(-1),
        ;


        private int direction;

        VoteType(int direction) {
        }

        public static VoteType lookup(Integer direction) {
                return Arrays.stream(VoteType.values())
                        .filter(value -> value.getDirection().equals(direction))
                        .findAny()
                        .orElseThrow(() -> new SpringRedditException("Vote not found"));
        }

        public Integer getDirection() {
                return direction;
        }

}
