package com.github.cashpath.repository;

import com.github.cashpath.model.entity.OpportunityCard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityCardRepository extends JpaRepository<OpportunityCard, Long> {

    @EntityGraph(attributePaths = {"asset", "asset.owner"})
    List<OpportunityCard> findByIsAvailableTrue();

    @EntityGraph(attributePaths = {"asset", "asset.owner"})
    List<OpportunityCard> findByType(OpportunityCard.OpportunityType type);

    @EntityGraph(attributePaths = {"asset", "asset.owner"})
    List<OpportunityCard> findByTypeAndIsAvailableTrue(OpportunityCard.OpportunityType type);

    @Query("SELECT c FROM OpportunityCard c LEFT JOIN FETCH c.asset a LEFT JOIN FETCH a.owner WHERE c.isAvailable = true ORDER BY function('random') LIMIT 1")
    OpportunityCard findRandomAvailableCard();

}
