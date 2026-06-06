package io.github.scitia.app.agile.domain.sprintplanning;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintPlanning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID sprintId;
    private List<String> committedItems;
    private int plannedStoryPoints;
    private int capacity;
    private boolean overflow;
    private String status;
}

