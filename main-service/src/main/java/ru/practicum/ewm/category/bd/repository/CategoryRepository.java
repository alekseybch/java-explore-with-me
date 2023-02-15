package ru.practicum.ewm.category.bd.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.category.bd.model.Category;
import ru.practicum.ewm.global.mapper.EntityMapper;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c " +
            "from Category c")
    List<Category> getAllCategories(PageRequest pageable);

    @EntityMapper
    Category getCategoryById(Long id);

}
