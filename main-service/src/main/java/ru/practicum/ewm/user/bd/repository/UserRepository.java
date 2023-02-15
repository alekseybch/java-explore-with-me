package ru.practicum.ewm.user.bd.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.user.bd.model.User;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u " +
            "from User u")
    List<User> getAllUsers(PageRequest pageable);

    @Query("select u " +
            "from User u " +
            "where u.id in (:ids)")
    List<User> getUsersById(Set<Long> ids, PageRequest pageable);

}
