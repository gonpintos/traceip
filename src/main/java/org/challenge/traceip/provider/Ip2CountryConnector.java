package org.challenge.traceip.provider;

import java.util.Arrays;

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

    @Autowired
    private ObjectMapper mapper;
    
    public void lalala() {
        RestTemplate restClient = new RestTemplate();
        String url = "";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        // revisar headers

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        ResponseEntity<?> response = null;
        response = restClient.exchange(builder.toUriString(), HttpMethod.GET, entity, Object.class);       
        
        Object resultObject;
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            resultObject = mapper.convertValue(response.getBody(), Object.class);
        }
            // ver q responde
        
    }
}
