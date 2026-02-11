package com.davendra.buzzer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
// if in this post model again occur inside user's savedPost, it'll only provide its reference
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;
    private List<String> mediaFiles;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

//    @OneToMany
//    @JoinTable(
//            name = "post_comments",
//            joinColumns = @JoinColumn(name = "post_id"),
//            inverseJoinColumns = @JoinColumn(name = "comment_id"))
//    private List<CommentModel> comments;

    private List<String> tags;
    private String location;

    @ManyToMany
    @JoinTable(
            name = "post_users_tagged",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserModel> usersTagged;

    @ManyToMany
    @JoinTable(
            name = "post_liked_by_user",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserModel> likedBy;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CommentModel> comments;

//
//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<CommentModel> postComments;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_saved_posts",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserModel> savedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
