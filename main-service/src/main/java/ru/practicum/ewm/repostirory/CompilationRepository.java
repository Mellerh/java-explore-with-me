package ru.practicum.ewm.repostirory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Page<Compilation> findAllByPinnedOrderByIdDesc(Boolean pinned, Pageable pageable);

    Page<Compilation> findAll(Pageable pageable);

}
