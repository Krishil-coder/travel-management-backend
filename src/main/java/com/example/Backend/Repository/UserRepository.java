package com.example.Backend.Repository;




import com.example.Backend.Domain.Role;
import com.example.Backend.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //    Optional is used to handle the null pointer exception; if there is no email available then no errors occurs;
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    boolean existsByEmail(String email);

    //List<User> findAll();

    List<User> findByIdIn(List<Long> id);

    List<User> id(Long id);
}
