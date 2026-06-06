package io.github.scitia.app.agile.domain.backlogitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BacklogItemRepository extends JpaRepository<BacklogItem, UUID> {
}


