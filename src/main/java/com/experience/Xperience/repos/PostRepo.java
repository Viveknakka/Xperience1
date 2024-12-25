package com.experience.Xperience.repos;

import com.experience.Xperience.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post,Integer> {

    List<Post> findByTitleIgnoreCase(String title);

    List<Post> findByCompanyIgnoreCase(String company);

    List<Post> findByRoleIgnoreCase(String role);

    List<Post> findByExperience(int experience);
    List<Post> findBySalaryBetween(int minSalary, int maxSalary);

    @Query(value = "SELECT * FROM posts p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "+
            "LOWER(p.role) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "+
            "LOWER(p.company) LIKE LOWER(CONCAT('%',:keyword,'%')) ",
            nativeQuery = true)
    List<Post> findByKeyword(String keyword);
}
