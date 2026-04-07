package com.megachollos.shared.infrastructure.rest.dtos.stats.statresult;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import lombok.experimental.SuperBuilder;

@Data
@With
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GroupCountStatResult extends StatResult {

  private List<GroupCountElementStatResult> values;
}
