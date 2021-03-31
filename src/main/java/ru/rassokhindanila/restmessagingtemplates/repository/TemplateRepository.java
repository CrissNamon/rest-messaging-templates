package ru.rassokhindanila.restmessagingtemplates.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rassokhindanila.restmessagingtemplates.model.Template;

@Repository
public interface TemplateRepository extends JpaRepository<Template, String> {

}
