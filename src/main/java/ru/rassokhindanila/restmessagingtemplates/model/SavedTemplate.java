package ru.rassokhindanila.restmessagingtemplates.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "saved_template")
public class SavedTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "template_id")
    private String templateId;

    @ElementCollection
    private Map<String, String> data;

    @Column(name = "minutes", nullable = false, columnDefinition = "integer default 0")
    private int minutes;
}
