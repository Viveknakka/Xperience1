package com.experience.Xperience.controllers;

import com.experience.Xperience.models.Post;
import com.experience.Xperience.models.User;
import com.experience.Xperience.models.UserPrincipal;
import com.experience.Xperience.services.PostService;
import com.experience.Xperience.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private  UserService userService;

    @PostMapping(value = "", consumes = "multipart/form-data")

    public ResponseEntity<?> addPost(
            @RequestPart("post") String postString,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal UserPrincipal userDetails) {
        // Check if the user is authenticated
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to create a post.");
        }

        try {
            // Deserialize the postString into a Post object
            ObjectMapper objectMapper = new ObjectMapper();
            Post post = objectMapper.readValue(postString, Post.class);

            // Set the current logged-in user as the author of the post
            post.setUser(userDetails.getUser());  // Assuming CustomUserDetails has a getUser() method

            // Handle image file (if provided)
            if (imageFile != null && !imageFile.isEmpty()) {
                post.setImageType(imageFile.getContentType());
                post.setImageFile(imageFile.getBytes());
            }

            // Save the post object
            postService.save(post);

            // Retrieve the saved post by its ID
            Post savedPost = postService.getPostById(post.getId()).orElse(null);

            // Return the saved post in the response
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create post. Error: " + e.getMessage());
        }
    }
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable int postId,
            @RequestPart("post") String postString,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal UserPrincipal userDetails) {
        // Check if the user is authenticated
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update a post.");
        }

        try {
            // Fetch the post by ID
            Post post = postService.getPostById(postId).orElse(null);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
            }
//            System.out.println(post.getUser().getUsername()+" post");
//            System.out.println(userDetails.getUser().getUsername()+" logoin");
//            System.out.println("role:"+userDetails.getUser().getRole());
            // Check if the logged-in user is the author of the post or an admin
            if (!post.getUser().getUsername().equals(userDetails.getUser().getUsername()) && !userDetails.getUser().getRole().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to update this post.");
            }

            // Deserialize the postString into a Post object and update the post
            ObjectMapper objectMapper = new ObjectMapper();
            Post updatedPost = objectMapper.readValue(postString, Post.class);

            // Handle the image file if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                updatedPost.setImageType(imageFile.getContentType());
                updatedPost.setImageFile(imageFile.getBytes());
            }

            // Update the post fields
            post.setTitle(updatedPost.getTitle()); // Update title, content, etc.
            post.setDescription(updatedPost.getDescription());
            post.setSkills(updatedPost.getSkills());
            post.setAnonymous(updatedPost.isAnonymous());
            post.setRole(updatedPost.getRole());
            post.setCompany(updatedPost.getCompany());
            post.setClearRounds(updatedPost.getClearRounds());
            post.setExperience(updatedPost.getExperience());
            post.setImageType(updatedPost.getImageType());
            post.setImageFile(updatedPost.getImageFile());
            post.setTimestamp(updatedPost.getTimestamp());
            // Save the updated post
            postService.save(post);

            return ResponseEntity.status(HttpStatus.OK).body("Post updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update post. Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable int postId,
            @AuthenticationPrincipal UserPrincipal userDetails) {
        // Check if the user is authenticated
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to delete a post.");
        }

        try {
            // Fetch the post by ID
            Post post = postService.getPostById(postId).orElse(null);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
            }

            // Check if the logged-in user is the author of the post or an admin
            if (!post.getUser().getUsername().equals(userDetails.getUser().getUsername()) && !userDetails.getUser().getRole().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this post.");
            }

            // Delete the post
            postService.deletePostById(post);

            return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete post. Error: " + e.getMessage());
        }
    }

    @GetMapping("")
    ResponseEntity<List<Post>> getAllPosts(){
       List<Post>posts = postService.getAllPost();
       return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getPostById(@PathVariable int id){
       Post post=postService.getPostById(id).orElse(null);
       if(post==null){
           return ResponseEntity.status(HttpStatus.OK).body("No post Found with given id..");
       }
       return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @GetMapping("/title")
    public ResponseEntity<?>getPostsByTitle(@RequestParam String title){
        List<Post>posts = postService.getPostByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body((posts!=null)?posts:"No Posts Found...");

    }

    @GetMapping("/company")
    public ResponseEntity<?>getPostsByCompany(@RequestParam String company){
        List<Post>posts = postService.getPostByCompany(company);
        return ResponseEntity.status(HttpStatus.OK).body((posts!=null)?posts:"No Posts Found...");

    }

    @GetMapping("/role")
    public ResponseEntity<?>getPostsByRole(@RequestParam String role){
        List<Post>posts = postService.getPostByRole(role);
        return ResponseEntity.status(HttpStatus.OK).body((posts!=null)?posts:"No Posts Found...");

    }

    @GetMapping("/experience")
    public ResponseEntity<?> getPostsByExperience(@RequestParam String experience) {
        try {
            int experienceInt = Integer.parseInt(experience);
            List<Post>posts=postService.getPostByExperience(experienceInt);
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid experience value. Must be an integer.");
        }
    }

    @GetMapping("/salary")
    public ResponseEntity<?> getPostsBySalaryRange(@RequestParam int min ,@RequestParam int max){
        List<Post>posts = postService.getPostsBySalaryRange(min,max);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/keyword")
    public ResponseEntity<?>search(@RequestParam String keyword){
        List<Post>posts=postService.getPostsByKeyword(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

}
