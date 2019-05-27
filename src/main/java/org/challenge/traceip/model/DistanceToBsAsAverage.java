package org.challenge.traceip.model;

import java.math.BigDecimal;

public class DistanceToBsAsAverage {
    
    private BigDecimal value;
    
    public DistanceToBsAsAverage() {
        super();
    }
    
    public DistanceToBsAsAverage(BigDecimal value) {
        super();
        this.value = value;
    }
    
    public DistanceToBsAsAverage(double value) {
        super();
        this.value = BigDecimal.valueOf(value);
    }


    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
}
