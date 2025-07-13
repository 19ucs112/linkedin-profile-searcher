package com.profile.searcher.repository;

import com.profile.searcher.entity.UniversityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UniversityRepository extends JpaRepository<UniversityEntity, UUID> {
}
