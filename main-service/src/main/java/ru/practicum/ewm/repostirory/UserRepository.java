package ru.practicum.ewm.repostirory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByIdIn(List<Long> idList, Pageable pageable);

    Boolean existsByName(String name);

    Boolean existsByIdIn(List<Long> userId);

}
