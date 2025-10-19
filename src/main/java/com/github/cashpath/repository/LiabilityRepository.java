package com.github.cashpath.repository;

import com.github.cashpath.model.entity.Liability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiabilityRepository extends JpaRepository<Liability, Long> {
}
