package com.cognive.projects.casernkb.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CounterPartyFinal {

    String inn;

    Long id;

    BigDecimal sumDt;

    BigDecimal sumKt;

}
