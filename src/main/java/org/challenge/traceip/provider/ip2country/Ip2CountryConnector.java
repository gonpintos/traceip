package org.challenge.traceip.provider.ip2country;

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
public class Ip2CountryConnector {
    
    private static final Logger logger = LoggerFactory.getLogger(Ip2CountryConnector.class);
    private static final String url = "https://api.ip2country.info/ip";
    
    @Autowired
    private ObjectMapper mapper;
    
    public Optional<Ip2CountryResponse> getCountryInformation(String ip) {
        Optional<Ip2CountryResponse> optIp2CountryResponse = Optional.empty();
        RestTemplate restClient = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String finalUrl = url + "?" + ip;
        logger.info("Requesting URL: {}", finalUrl);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finalUrl);
        ResponseEntity<Ip2CountryResponse> responseEntity = restClient.exchange(builder.toUriString(), HttpMethod.GET, entity, Ip2CountryResponse.class);       
        if (HttpStatus.OK.equals(responseEntity.getStatusCode()))
            optIp2CountryResponse = Optional.of(mapper.convertValue(responseEntity.getBody(), Ip2CountryResponse.class));
        else
            throw new HTTPException(responseEntity.getStatusCodeValue());
        return optIp2CountryResponse;
    }
}
