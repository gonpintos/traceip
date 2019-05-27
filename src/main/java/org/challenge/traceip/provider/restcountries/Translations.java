package org.challenge.traceip.provider.restcountries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Translations {
    
    private String es;
    
    public Translations() {
        super();
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

}
