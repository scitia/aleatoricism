package io.github.scitia.app.agile.domain.backlogitem;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BacklogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID itemId;
    private String title;
    private String description;
    private String type;
    private int storyPoints;
    private int priority;
    private String reporter;
    private String status;
}

