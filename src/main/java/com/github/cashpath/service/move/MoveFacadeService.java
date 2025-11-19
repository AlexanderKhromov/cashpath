package com.github.cashpath.service.move;

import com.github.cashpath.model.dto.BuyRequestDTO;
import com.github.cashpath.model.dto.MoveResponseDTO;
import org.springframework.stereotype.Service;

/**
 * Responsible for creating <code> MoveResponseDTO </> as a response to UI
 */
@Service
public interface MoveFacadeService {

    MoveResponseDTO buy(Long gameId, BuyRequestDTO request);

    MoveResponseDTO endTurn(Long gameId);

}