package ru.practicum.ewm.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.db.model.App;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {

    App getAppByName(String name);

}
