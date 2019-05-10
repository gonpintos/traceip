package org.challenge.traceip.controller;

import javax.validation.Valid;

import org.challenge.traceip.domain.Ip;
import org.challenge.traceip.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IpController {

    @Autowired
    IpService ipService;
    
    @GetMapping("/hello2")
//    public ModelAndView showForm() {
//        return< new ModelAndView("hello");
//    }
    public String showForm() {
        return "WEB-INF/hello.jsp";
    }

    @PostMapping(path = "/trace")
    public String submit(@Valid @ModelAttribute("ip") Ip ip, 
      BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }
        
        String trace = ipService.trace(ip.getIp());
        System.out.println(trace);
        
        model.addAttribute("ip", ip.getIp());
        return "ipView";
    }
    
}
