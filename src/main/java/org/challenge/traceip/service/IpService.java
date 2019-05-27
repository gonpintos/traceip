package org.challenge.traceip.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.challenge.traceip.dto.IpTraceResult;
import org.challenge.traceip.dto.Statistics;
import org.challenge.traceip.model.DistanceToBsAsAverage;
import org.challenge.traceip.model.RequestTracker;
import org.challenge.traceip.provider.fixer.ExchangeRate;
import org.challenge.traceip.provider.fixer.FixerConnector;
import org.challenge.traceip.provider.ip2country.Ip2CountryConnector;
import org.challenge.traceip.provider.ip2country.Ip2CountryResponse;
import org.challenge.traceip.provider.restcountries.CountryInformation;
import org.challenge.traceip.provider.restcountries.Currency;
import org.challenge.traceip.provider.restcountries.Language;
import org.challenge.traceip.provider.restcountries.RestCountriesConnector;
import org.challenge.traceip.repository.RequestTrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IpService {

    private static final Logger logger = LoggerFactory.getLogger(IpService.class);
    
    private static final double buenosAiresLatitude = -34;
    private static final double buenosAiresLongitude = -58;
    private static final String usaCurrencySymbol = "USD";
    
    @Autowired
    Ip2CountryConnector ip2CountryConnector;

    @Autowired
    RestCountriesConnector restCountriesConnector;
    
    @Autowired
    FixerConnector fixerConnector;
    
    @Autowired
    RequestTrackerRepository requestTrackerRepository;
    
    public IpTraceResult trace(String ip) throws Exception {
        
        logger.debug("Validating Inet IP");
        ip = ip.trim();
        if (Strings.isBlank(ip) || !isValidInetIp(ip))
            throw new IllegalArgumentException("Invalid Inet IP");
        
        logger.info("Tracing Inet IP: {}", ip);
        
        // obtener país origen de la ip
        IpTraceResult ipTraceResult = new IpTraceResult();
        
        ipTraceResult.setIp(ip);
        ipTraceResult.setCurrentDateTime(ZonedDateTime.now().toLocalDateTime());
        
        Ip2CountryResponse ip2CountryResponse = ip2CountryConnector
                .getCountryInformation(ip)
                .orElseThrow(() -> new Exception("Country code couldn't be retrieved"));

        
        CountryInformation countryInformation = restCountriesConnector
                .getCountryInformation(ip2CountryResponse.getCountryCode())
                .orElseThrow(() -> new Exception("Country information couldn't be retrieved"));
        

        ipTraceResult.setCountryName(
                String.format("%s (%s)", 
                        countryInformation.getTranslations().getEs(), 
                        countryInformation.getName().toLowerCase()));
        ipTraceResult.setCountryISOCode(countryInformation.getAlpha2Code());
        
        List<String> languages = new ArrayList<String>();
        for(Language language : countryInformation.getLanguages())
            languages.add(String.format("%s (%s)", language.getName(), language.getIso639_1()));
        ipTraceResult.setLanguages(languages);
        
        List<OffsetTime> offsetTimes = new ArrayList<OffsetTime>();
        for(String timezone : countryInformation.getTimezones())
            offsetTimes.add(OffsetTime.now(ZoneOffset.of(timezone.replace("UTC", ""))));
        ipTraceResult.setOffsetTimes(offsetTimes);        

        ipTraceResult.setApproximateDistance(BigDecimal.valueOf(
                calculateDistance(
                    countryInformation.getLatlng().get(0).doubleValue(), 
                    countryInformation.getLatlng().get(1).doubleValue(), 
                    buenosAiresLatitude, 
                    buenosAiresLongitude)
                ).setScale(2, RoundingMode.HALF_UP));
        
        
        if (!countryInformation.getCurrencies().isEmpty()) {
            // Se asume una moneda
            String currencyText = "";            
            Currency currency = countryInformation.getCurrencies().get(0);
            ExchangeRate exchangeRate = fixerConnector
                    .getExchangeRate(currency.getCode(), usaCurrencySymbol)
                    .orElseThrow(() -> new Exception("Exchange rate couldn't be retrieved"));
            
            if (exchangeRate.isSuccess()) {
                BigDecimal usdExchangeRate = exchangeRate.getRates().get(usaCurrencySymbol);
                BigDecimal relativeCountryExchangeRate = exchangeRate.getRates().get(currency.getCode());
                BigDecimal finalCountryExchangeRate = usdExchangeRate.divide(relativeCountryExchangeRate, MathContext.DECIMAL32).setScale(5, RoundingMode.HALF_UP);
                currencyText = String.format("%s (1 %s = %s %s)", currency.getCode(), currency.getCode(), finalCountryExchangeRate.toPlainString(), usaCurrencySymbol);
            } else {
                logger.warn("Currency Exchange not available for {}", currency.getCode());
                currencyText = "Not available";
            }
            ipTraceResult.setCurrency(currencyText);
        }
        
        RequestTracker requestTracker = new RequestTracker();
        requestTracker.setIp(ipTraceResult.getIp());
        requestTracker.setCreationDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        requestTracker.setDistanceToBsAs(ipTraceResult.getApproximateDistance());
        requestTrackerRepository.save(requestTracker);
        
        return ipTraceResult;
    }
    
    public Statistics getStatistics() {
        Statistics statistics = new Statistics();
        
        statistics.setMaxDistanceToBsAs(
                requestTrackerRepository.findTop1OptionalByOrderByDistanceToBsAsDesc()
                .map(RequestTracker::getDistanceToBsAs)
                .orElseGet(() -> BigDecimal.ZERO));
                
                
        statistics.setMinDistanceToBsAs(
                requestTrackerRepository.findTop1OptionalByOrderByDistanceToBsAsAsc()
                .map(RequestTracker::getDistanceToBsAs)
                .orElseGet(() -> BigDecimal.ZERO));
        
        DistanceToBsAsAverage averageDistanceToBsAs = requestTrackerRepository.findAverageDistanceToBsAs();
        statistics.setAverageDistanceToBsAs(
                averageDistanceToBsAs != null? 
                averageDistanceToBsAs.getValue(): 
                BigDecimal.ZERO);
        
        return statistics; 
    }

    
    private boolean isValidInetIp(String ip) {
        boolean validInetIp = true;
        try {
            validInetIp = Inet4Address.getByName(ip).getHostAddress().equals(ip);
        } catch (UnknownHostException unknownHostException) {
            validInetIp = false;
        }
        return validInetIp;
    }
    
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final double earthRadio = 6371;  
        double dLat = Math.toRadians(lat2 - lat1);  
        double dLng = Math.toRadians(lng2 - lng1);  
        double sindLat = Math.sin(dLat / 2);  
        double sindLng = Math.sin(dLng / 2);  
        double value1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));  
        double value2 = 2 * Math.atan2(Math.sqrt(value1), Math.sqrt(1 - value1));  
        return earthRadio * value2;  
    }  
}
