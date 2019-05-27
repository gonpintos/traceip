package org.challenge.traceip.domain;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class Ip {
    
    private String ip;
    
    public Ip() {
        super();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }
    
    @Override
    public String toString() {
        return reflectionToString(this);
    }

}
