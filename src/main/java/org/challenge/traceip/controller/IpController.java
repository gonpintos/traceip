package org.challenge.traceip.controller;

import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;

import org.challenge.traceip.domain.Ip;
import org.challenge.traceip.dto.IpTraceResult;
import org.challenge.traceip.dto.Statistics;
import org.challenge.traceip.service.IpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ip")
public class IpController {
    
    private static final Logger logger = LoggerFactory.getLogger(IpController.class);
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Autowired
    IpService ipService;
    
    @GetMapping
    public ModelAndView showForm() {
        return new ModelAndView("ip", "ip", new Ip());
    }

    @PostMapping(path = "/trace")
    public String submit(@Valid @ModelAttribute("ip") Ip ip, 
      BindingResult result, ModelMap model) {
        
        if (result.hasErrors())
            return "error";
        
        logger.info("IP object: {}", ip);
        
        try {
            IpTraceResult ipTraceResult = ipService.trace(ip.getIp());
            model.addAttribute("ip", ipTraceResult.getIp());
            model.addAttribute("currentDateTime", dateTimeFormatter.format(ipTraceResult.getCurrentDateTime()));
            model.addAttribute("countryName", ipTraceResult.getCountryName());
            model.addAttribute("countryISOCode", ipTraceResult.getCountryISOCode());
        
            StringBuilder sbLanguages = new StringBuilder();
            for (int i = 0; i < ipTraceResult.getLanguages().size(); i++)
                sbLanguages = sbLanguages.append(String.format("%s %s", 
                        ipTraceResult.getLanguages().get(i), 
                        (i < ipTraceResult.getLanguages().size() - 1)? " o " :""));               
            model.addAttribute("languages", sbLanguages.toString());
            
            model.addAttribute("currency", ipTraceResult.getCurrency());            
            
            
            StringBuilder sbCurrentTimes = new StringBuilder();
            for(int i = 0; i < ipTraceResult.getOffsetTimes().size(); i++) 
                sbCurrentTimes = sbCurrentTimes.append(String.format("%s (UTC%s) %s", 
                        dateTimeFormatter.format(ipTraceResult.getOffsetTimes().get(i)), 
                        ipTraceResult.getOffsetTimes().get(i).getOffset().getId(),
                        (i < ipTraceResult.getOffsetTimes().size() - 1)? " o ": ""));
            model.addAttribute("currentTimes", sbCurrentTimes.toString());
            
            model.addAttribute("approximateDistance", ipTraceResult.getApproximateDistance());
        } catch (Exception exception) {
            logger.error("Exception", exception);
            return "error";
        }
        return "ipresult";
    }
    
    @GetMapping("/statistics")
    public ModelAndView getStatistics() {
        Statistics statistics = ipService.getStatistics();
        ModelAndView modelAndView = new ModelAndView("statistics");
        modelAndView.addObject("maxDistanceToBsAs", statistics.getMaxDistanceToBsAs().setScale(2, RoundingMode.HALF_UP).toPlainString());
        modelAndView.addObject("minDistanceToBsAs", statistics.getMinDistanceToBsAs().setScale(2, RoundingMode.HALF_UP).toPlainString());
        modelAndView.addObject("averageDistanceToBsAs", statistics.getAverageDistanceToBsAs().setScale(2, RoundingMode.HALF_UP).toPlainString());
        return modelAndView;        
    }
    
}
