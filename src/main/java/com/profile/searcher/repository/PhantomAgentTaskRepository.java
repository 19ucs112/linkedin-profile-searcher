package com.profile.searcher.repository;

import com.profile.searcher.entity.PhantomAgentTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhantomAgentTaskRepository extends JpaRepository<PhantomAgentTaskEntity, UUID> {
}
