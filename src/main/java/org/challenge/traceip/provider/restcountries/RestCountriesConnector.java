package org.challenge.traceip.provider.restcountries;

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
public class RestCountriesConnector {
    
    private static final Logger logger = LoggerFactory.getLogger(RestCountriesConnector.class);
    private final static String url = "https://restcountries.eu/rest/v2/alpha";
    
    @Autowired
    private ObjectMapper mapper;
    
    public Optional<CountryInformation> getCountryInformation(String countryCode) {
        Optional<CountryInformation> optCountryInformation = Optional.empty();
        RestTemplate restClient = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String finalUrl = url + "/" + countryCode;
        logger.info("Requesting URL: {}", finalUrl);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finalUrl);
        ResponseEntity<CountryInformation> responseEntity = restClient.exchange(builder.toUriString(), HttpMethod.GET, entity, CountryInformation.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode()))
            optCountryInformation = Optional.of(mapper.convertValue(responseEntity.getBody(), CountryInformation.class));
        else
            throw new HTTPException(responseEntity.getStatusCodeValue());
        return optCountryInformation;
    }

}
