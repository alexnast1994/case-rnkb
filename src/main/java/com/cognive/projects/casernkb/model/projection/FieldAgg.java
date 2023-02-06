package com.cognive.projects.casernkb.model.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FieldAgg {

    Long getAggId();

    Long getLtype();

    String getString();

    BigDecimal getLsum();

    Long getLcount();

}
