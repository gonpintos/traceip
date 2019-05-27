package org.challenge.traceip.provider.fixer;

import java.util.Arrays;
import java.util.Optional;

import javax.xml.ws.http.HTTPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FixerConnector {
    
    private static final Logger logger = LoggerFactory.getLogger(FixerConnector.class);
    
    // TODO params
    private final static String api_access_key = "b59f4240083e7fcc1e8ff70f8553df64";
    private final static String url = "http://data.fixer.io/api";
    
    @Autowired
    ObjectMapper mapper;
    
    public Optional<ExchangeRate> getExchangeRate(String ...symbols) {
        Optional<ExchangeRate> optExchangeRate = Optional.empty();
        RestTemplate restClient = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        StringBuilder sb = new StringBuilder();
        for (String s : symbols) sb = sb.append(s + ",");
        String finalUrl = url + "/latest?access_key=" + api_access_key + "&symbols=" + sb.toString();
        logger.info("Requesting URL: {}", finalUrl);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finalUrl);
        ResponseEntity<ExchangeRate> responseEntity = restClient.exchange(builder.toUriString(), HttpMethod.GET, entity, ExchangeRate.class);       
        if (HttpStatus.OK.equals(responseEntity.getStatusCode()))
            optExchangeRate = Optional.of(mapper.convertValue(responseEntity.getBody(), ExchangeRate.class));
        else
            throw new HTTPException(responseEntity.getStatusCodeValue());
        return optExchangeRate;
    }

}
