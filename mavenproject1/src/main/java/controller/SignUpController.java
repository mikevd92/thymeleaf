/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import exceptions.AppException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import service.UserService;

/**
 *
 * @author Mihai
 */
@Controller
public class SignUpController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showForm() {
        ModelAndView model = new ModelAndView("signUp", "user", new User());
        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult result) throws AppException {
        result.getAllErrors().stream().map(p -> p.getDefaultMessage()).reduce((i,j)-> i+"\n"+j);
        if (result.hasErrors()) {
            return "register";
        } else {
            userService.addUser(user);
            return "redirect:/login";
        }
    }

    @ExceptionHandler(AppException.class)
    public ModelAndView handleError(HttpServletRequest req, AppException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}
