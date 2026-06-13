package io.github.scitia.app.sprint.infrastructure;

import io.github.scitia.app.sprint.domain.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
}

