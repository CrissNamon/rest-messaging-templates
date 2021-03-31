package ru.rassokhindanila.restmessagingtemplates.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "template")
@Data
public class Template {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String templateId;

    @Column(name = "template")
    private String template;

    @ElementCollection
    private Set<String> recipients;

}
