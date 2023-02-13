package ru.practicum.ewm.compilation.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.compilation.bd.model.Compilation;

@Repository
public interface CompilationRepository  extends JpaRepository<Compilation, Long> {
}
