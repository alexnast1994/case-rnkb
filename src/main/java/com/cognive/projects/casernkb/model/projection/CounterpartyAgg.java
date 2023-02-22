package com.cognive.projects.casernkb.model.projection;

import java.math.BigDecimal;

public interface CounterpartyAgg {

    String getInn();

    BigDecimal getSumDt();

    BigDecimal getSumKt();

}
