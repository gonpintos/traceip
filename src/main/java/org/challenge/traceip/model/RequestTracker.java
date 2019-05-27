package org.challenge.traceip.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RequestTracker {
    
    @Id
    @GeneratedValue
    private Long id;
    private String ip;
    private Date creationDate;
    private BigDecimal distanceToBsAs;
    
    public RequestTracker() {
        super();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getDistanceToBsAs() {
        return distanceToBsAs;
    }

    public void setDistanceToBsAs(BigDecimal distanceToBsAs) {
        this.distanceToBsAs = distanceToBsAs;
    }

}