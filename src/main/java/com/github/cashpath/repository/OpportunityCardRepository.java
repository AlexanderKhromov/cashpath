package com.github.cashpath.repository;

import com.github.cashpath.model.entity.OpportunityCard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = """
                SELECT c.*
                FROM opportunity_cards c
                WHERE c.is_available = true
                ORDER BY random()
                LIMIT 1
            """, nativeQuery = true)
    OpportunityCard findRandomAvailableCard();

    @Modifying
    @Query("""
               UPDATE OpportunityCard c\s
               SET c.isAvailable = false\s
               WHERE c.id = :id AND c.isAvailable = true
           \s""")
    int markAsBoughtIfAvailable(@Param("id") Long id);

}
