package com.experience.Xperience.services;

import com.experience.Xperience.models.Post;
import com.experience.Xperience.repos.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepo postRepo;
    public Post save(Post post) {
        return postRepo.save(post);
    }
    public List<Post> getAllPost(){
        return postRepo.findAll();
    }
    public Optional<Post> getPostById(int id) {
        return postRepo.findById(id);
    }

    public List<Post> getPostByTitle(String title) {
        return postRepo.findByTitleIgnoreCase(title);
    }

    public List<Post> getPostByCompany(String company) {
        return postRepo.findByCompanyIgnoreCase(company);
    }

    public List<Post> getPostByRole(String role) {
        return postRepo.findByRoleIgnoreCase(role);
    }

    public List<Post> getPostByExperience(int experience) {
        return postRepo.findByExperience(experience);
    }

    public List<Post> getPostsBySalaryRange(int min, int max) {
        return postRepo.findBySalaryBetween(min,max);
    }

    public List<Post> getPostsByKeyword(String keyword) {
        return postRepo.findByKeyword(keyword);
    }

    public void deletePostById(Post post) {
        postRepo.delete(post);
    }
}
