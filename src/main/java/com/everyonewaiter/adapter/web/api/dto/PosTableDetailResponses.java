package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.pos.PosView;
import java.util.List;

public record PosTableDetailResponses(List<PosView.PosTableDetail> tables) {

  public static PosTableDetailResponses from(List<PosView.PosTableDetail> posTables) {
    return new PosTableDetailResponses(posTables);
  }

}
