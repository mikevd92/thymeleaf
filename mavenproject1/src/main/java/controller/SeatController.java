/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import exceptions.AppException;
import java.io.Serializable;
import java.util.List;
import java.util.Stack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import model.Seat;
import model.Play;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import service.UserService;
import service.Utils;

/**
 *
 * @author Mihai
 */
@Controller
@RequestMapping(value = "/seat")
public class SeatController implements Serializable {

    @Autowired
    private UserService userService;
    
    @Autowired
    private Utils utils;
    
    @Autowired
    SimpMessagingTemplate template;

    private List<Seat> seats;
    private Stack<Seat> changes;
    private int id_play;
    private Seat currentPlace;

    public SeatController() {
        changes = new Stack<>();
        currentPlace = new Seat();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView show(@Valid @ModelAttribute Play play, HttpServletRequest request) throws AppException {
        HttpSession session = request.getSession();
        ModelAndView model;
        String name = (String) session.getAttribute("name");
        if (name != null) {
            model = new ModelAndView("user", "play", new Play());
            model.addObject("plays", userService.loadPlays());
            model.addObject("name", session.getAttribute("name"));
        } else {
            model = new ModelAndView("securityError", "play", new Play());
        }
        return model;
    }

    @RequestMapping(value = "/list/id/{id}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody
    String loadSeats(@PathVariable Integer id) {
        String response = "";
        try {
            id_play = id;
            seats = userService.findByShowID(id_play);
            response = utils.convertSeatsToHtml(seats);
        } catch (AppException e) {
            response = "Exception:" + e.getMessage();
        }
        return response;
    }

    @RequestMapping(value = "/list/id/{id}/availability/{availability}/name/{name}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody
    String reserve(@PathVariable Integer id, @PathVariable String name, @PathVariable String availability) {
        String response;
        try {
            if (userService.getPlaceName(id).equals("")) {
                userService.changeAvailability(id, availability, name);
                response = "{\"notify\":\"succes\",\"id\":\"" + id_play + "\"}";
                template.convertAndSend("/channel/seats", response);
                response = "succes";
            } else {
                response = "Seat already reserved!";
            }
        } catch (AppException | MessagingException e) {
            response = "Exception:" + e.getMessage();
        }
        return response;
    }

    @RequestMapping(value = "/list/cancel/id/{id}/availability/{availability}/name/{name}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody
    String cancel(@PathVariable Integer id, @PathVariable String availability, @PathVariable String name) {
        String response="";
        String storedName=userService.getPlaceName(id);
        try {
            if (storedName.equals(name)) {
                userService.cancelAvailability(id, availability);
                response = "{\"notify\":\"succes\",\"id\":\"" + id_play + "\"}";
                template.convertAndSend("/channel/seats", response);
                response = "succes";
            } else if(storedName.equals("")){
                response = "Seat not reserved!";
            } else if(!storedName.equals(name)){
                response = "Not your seat!";
            }
        } catch (AppException | MessagingException e) {
            response = "Exception:" + e.getMessage();
        }
        return response;
    }

    @RequestMapping(value = "/list/hide", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody
    String hide() {
        return "";
    }
    
    @ExceptionHandler(HttpSessionRequiredException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="The session has expired")	
    public String handleSessionExpired(){		
        return "sessionExpired";
    }
}
