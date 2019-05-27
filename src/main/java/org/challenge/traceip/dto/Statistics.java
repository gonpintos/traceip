package org.challenge.traceip.dto;

import java.math.BigDecimal;

public class Statistics {
    
    private BigDecimal maxDistanceToBsAs;
    private BigDecimal minDistanceToBsAs;
    private BigDecimal averageDistanceToBsAs;
    
    public Statistics() {
        super();
    }

    public BigDecimal getMaxDistanceToBsAs() {
        return maxDistanceToBsAs;
    }

    public void setMaxDistanceToBsAs(BigDecimal maxDistanceToBsAs) {
        this.maxDistanceToBsAs = maxDistanceToBsAs;
    }

    public BigDecimal getMinDistanceToBsAs() {
        return minDistanceToBsAs;
    }

    public void setMinDistanceToBsAs(BigDecimal minDistanceToBsAs) {
        this.minDistanceToBsAs = minDistanceToBsAs;
    }

    public BigDecimal getAverageDistanceToBsAs() {
        return averageDistanceToBsAs;
    }

    public void setAverageDistanceToBsAs(BigDecimal averageDistanceToBsAs) {
        this.averageDistanceToBsAs = averageDistanceToBsAs;
    }
    
    

}
