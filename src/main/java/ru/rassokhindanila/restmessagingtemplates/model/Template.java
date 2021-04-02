package ru.rassokhindanila.restmessagingtemplates.model;

import lombok.Data;
import ru.rassokhindanila.restmessagingtemplates.dto.Receiver;

import javax.persistence.*;
import java.util.Set;

/**
 * Template entity
 */
@Entity
@Table(name = "template")
@Data
public class Template {

    /**
     * Template id
     */
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String templateId;

    /**
     * Template string with placeholders
     */
    @Column(name = "template")
    private String template;

    /**
     * Template endpoints
     */
    @ElementCollection
    private Set<Receiver> recipients;

}
