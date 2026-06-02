package com.example.Backend.Repository;




import com.example.Backend.Domain.Role;
import com.example.Backend.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByRoleAndDepartment(Role role, String department);

    @Query("select distinct u.department from User u where u.department is not null and u.department <> '' order by u.department")
    List<String> findDistinctDepartments();

    boolean existsByEmail(String email);

    List<User> findByIdIn(List<Long> id);

    List<User> id(Long id);
}
