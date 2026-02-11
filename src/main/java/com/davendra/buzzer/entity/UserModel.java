package com.davendra.buzzer.entity;


//@Entity(name="something") -> so the name given in this is used to write queries in our java app
// @Table(name="some2") -> this name is used to make a table in db, if not specified (it'll take it from @Entity

import com.davendra.buzzer.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private String password;

    private String profileImg;

    @ElementCollection
    private List<Long> followers;

    @ElementCollection
    private List<Long> followings;

    private GenderEnum gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PostModel> posts; // One user can have many posts

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StoryModel> stories; // One user can have many stories

}
