package com.github.cashpath.repository;

import com.github.cashpath.model.entity.OpportunityCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpportunityCardRepository extends JpaRepository<OpportunityCard, Long> {

    @Query("""
            select c from OpportunityCard c
            where c.game.id = :gameId
                and c.isAvailable = true
            order by function('random')
            """)
    List<OpportunityCard> findRandomAvailableCard(@Param("gameId") Long gameId);

    @Modifying
    @Query("""
            update OpportunityCard c
            set c.isAvailable = false
            where c.id = :cardId
                and c.game.id = :gameId
                and c.isAvailable = true
            """)
    int markAsBoughtIfAvailable(
            @Param("gameId") Long gameId,
            @Param("cardId") Long cardId
    );

    Optional<OpportunityCard> findByIdAndGameId(Long id, Long gameId);

}
