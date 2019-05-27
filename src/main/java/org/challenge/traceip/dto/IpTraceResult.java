package org.challenge.traceip.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.util.List;

public class IpTraceResult {

    private String ip;
    private LocalDateTime currentDateTime;
    private String countryName;
    private String countryISOCode;
    private List<String>languages;
    private String currency;
    private List<OffsetTime> offsetTimes;
    private BigDecimal approximateDistance;
    
    public IpTraceResult() {
        super();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryISOCode() {
        return countryISOCode;
    }

    public void setCountryISOCode(String countryISOCode) {
        this.countryISOCode = countryISOCode;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<OffsetTime> getOffsetTimes() {
        return offsetTimes;
    }

    public void setOffsetTimes(List<OffsetTime> offsetTimes) {
        this.offsetTimes = offsetTimes;
    }
    
    public BigDecimal getApproximateDistance() {
        return approximateDistance;
    }

    public void setApproximateDistance(BigDecimal approximateDistance) {
        this.approximateDistance = approximateDistance;
    }

}
