package com.experience.Xperience.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String company;
    private String role; // Example field for role, such as software engineer or developer
    private int rounds;
    private int clearRounds;
    private double salary;
    private int experience;
    @Lob
    private byte[] imageFile;
    private String imageType;
    private String description;
    private List<String> skills;
    private boolean isAnonymous; // If the user wants to post anonymously

    @Column(updatable = false)//updated only once
    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "username") // Foreign key column referring to the user
    private User user; // Many posts can belong to one user
}
