/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import exceptions.AppException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import model.Play;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping(value = "/play")
public class PlayController implements Serializable {

    @Autowired
    private UserService userService;
    @Autowired
    private Utils utils;

    @Autowired
    SimpMessagingTemplate template;

    private Stack<Play> changes;
    private Play currentPlay;

    public PlayController() {
        changes = new Stack<>();
        if (currentPlay == null) {
            currentPlay = new Play();
        }
        //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");+
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView show(@Valid @ModelAttribute Play play, HttpServletRequest request) throws AppException {
        ModelAndView model = null;
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("name");
        if (name != null) {
            model = new ModelAndView("manager", "play", new Play());
            model.addObject("plays", userService.loadPlays());
        } else {
            model = new ModelAndView("securityError", "play", new Play());
        }
        return model;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    JSONResponse addPlay(@Valid @RequestBody Play play, Errors errors) {
        String response;
        if (!errors.hasErrors()) {
            try {
                userService.addPlay(play);
                response = "{\"notify\":\"succes\"}";
                template.convertAndSend("/channel/plays", response);
                response = "succes";
                return new JSONResponse("succes", response);
            } catch (AppException | MessagingException e) {
                response = "Exception:" + e.getMessage();
                return new JSONResponse("error", response);
            }
        } else {
            return new JSONResponse("error", utils.convertErrorsToHtml(errors.getAllErrors()));
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    JSONResponse editPlay(@Valid @RequestBody Play play, Errors errors) throws AppException {
        String response;
        if (!errors.hasErrors()) {
            ResponseEntity entity = new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
            try {
                userService.updatePlay(play);
                int id_play = play.getIdPlay();
                response = "{\"notify\":\"succes\",\"id\":\"" + id_play + "\"}";
                template.convertAndSend("/channel/plays", response);
                response = "succes";
                return new JSONResponse("succes", response);
            } catch (AppException | MessagingException e) {
                response = "Exception:" + e.getMessage();
                return new JSONResponse("error", response);
            }
        } else {
            return new JSONResponse("error", utils.convertErrorsToHtml(errors.getAllErrors()));
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    JSONResponse removePlay(@Valid @RequestBody Play play, Errors errors) {
        String response;
        if (!errors.hasErrors()) {
            try {
                userService.removePlay(play.getIdPlay());
                if (Objects.equals(currentPlay.getIdPlay(), play.getIdPlay())) {
                    currentPlay = changes.pop();
                }
                int id_play = play.getIdPlay();
                response = "{\"notify\":\"delete\",\"id\":\"" + id_play + "\"}";
                template.convertAndSend("/channel/plays", response);
                response = "succes";
                return new JSONResponse("succes", response);
            } catch (AppException | MessagingException e) {
                response = "Exception:" + e.getMessage();
                return new JSONResponse("error", response);
            }
        } else {
            return new JSONResponse("error", utils.convertErrorsToHtml(errors.getAllErrors()));
        }
    }

    @RequestMapping(value = "/list/refresh", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody
    String refresh() {
        List<Play> plays = null;
        if (currentPlay.getPlayName() != null && currentPlay.getStartTime() != null && currentPlay.getStartTime() != null && currentPlay.getEndTime() != null && currentPlay.getStartDate() != null && currentPlay.getTicketPrice() != 0) {
            Play play = userService.getPlay(currentPlay.getIdPlay());
            currentPlay.setPlayName(play.getPlayName());
            currentPlay.setStartTime(play.getStartTime());
            currentPlay.setEndTime(play.getEndTime());
            currentPlay.setStartDate(play.getStartDate());
            currentPlay.setTicketPrice(play.getTicketPrice());
        }
        String response;
        try {
            plays = filterPlays(currentPlay, userService.loadPlays());
            if (plays != null) {
                response = utils.convertPlaysToHtml(plays);
            } else {
                response = utils.convertPlaysToHtml(userService.loadPlays());
            }
        } catch (AppException e) {
            response = "Exception:" + e.getMessage();
        }
        return response;
    }

    @RequestMapping(value = "/list/undo", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody
    String undo() {
        List<Play> plays;
        String response;
        try {
            if (!changes.isEmpty()) {
                currentPlay = changes.pop();
                plays = filterPlays(currentPlay, userService.loadPlays());
            } else {
                plays = userService.loadPlays();
            }
            if (plays != null) {
                response = utils.convertPlaysToHtml(plays);
            } else {
                response = utils.convertPlaysToHtml(userService.loadPlays());
            }
        } catch (AppException e) {
            response = "Exception:" + e.getMessage();
        }
        return response;
    }

    @RequestMapping(value = "list/filter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    JSONResponse filter(@Valid @RequestBody Play play, Errors errors) {
        String response;
        if (!errors.hasErrors()) {
            try {
                changes.add(currentPlay);
                currentPlay = play;
                List<Play> plays = filterPlays(currentPlay, userService.loadPlays());
                if (plays != null) {
                    response = utils.convertPlaysToHtml(plays);
                    return new JSONResponse("success", response);
                } else {
                    currentPlay = changes.pop();
                    response = "No such elements!";
                    return new JSONResponse("error", response);
                }
            } catch (AppException e) {
                response = "Exception:" + e.getMessage();
                return new JSONResponse("error", response);
            }
        } else {
            return new JSONResponse("error", utils.convertErrorsToHtml(errors.getAllErrors()));
        }
    }

    public List<Play> filterPlays(Play play, List<Play> plays) {
        Predicate<Play> selector = p -> p != null;
        String playName = play.getPlayName();
        Integer ticketPrice = play.getTicketPrice();
        if (playName != null && !playName.equals("")) {
            selector = selector.and(p -> p.getPlayName().equals(playName));
        }
        if (play.getStartTime() != null) {
            Time startTime = Time.valueOf(play.getStartTime().toString());
            selector = selector.and(p -> (p.getStartTime().after(startTime) || p.getStartTime().equals(startTime)));
        }
        if (play.getEndTime() != null) {
            Time endTime = Time.valueOf(play.getEndTime().toString());
            selector = selector.and(p -> (p.getEndTime().before(endTime) || p.getEndTime().equals(endTime)));
        }
        if (play.getStartDate() != null) {
            Date startDate = Date.valueOf(play.getStartDate().toString());
            selector = selector.and(p -> p.getStartDate().equals(startDate));
        }
        if (ticketPrice != 0) {
            selector = selector.and(p -> p.getTicketPrice() == ticketPrice);
        }
        return lambdaFilter(selector, plays);
    }

    public List<Play> lambdaFilter(Predicate<Play> selector, List<Play> plays) {
        List<Play> list = plays.stream().filter(p -> selector.test(p)).collect(Collectors.toList());
        if (!list.isEmpty()) {
            return list;
        } else {
            return null;
        }
    }

    @ExceptionHandler(HttpSessionRequiredException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "The session has expired")
    public String handleSessionExpired() {
        return "sessionExpired";
    }
}
