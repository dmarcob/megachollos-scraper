package com.megachollos.shared.infrastructure.elastic.statoperators;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.megachollos.shared.infrastructure.rest.dtos.stats.Stat;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.StatResult;

public abstract class BaseStatOperator {

  public abstract Aggregation apply(Stat stat);

  public abstract StatResult toStatResult(String field, Aggregate aggregate);
}