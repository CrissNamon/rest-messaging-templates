package ru.rassokhindanila.restmessagingtemplates.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rassokhindanila.restmessagingtemplates.model.SavedTemplate;

@Repository
public interface SavedTemplateRepository extends JpaRepository<SavedTemplate, Long> {
}
