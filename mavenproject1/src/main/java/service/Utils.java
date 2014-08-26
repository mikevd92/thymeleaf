/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import java.util.List;
import model.Play;
import model.Seat;
import org.springframework.validation.ObjectError;

/**
 *
 * @author Mihai
 */
public interface Utils {
    public String convertPlaysToHtml(List<Play> plays);
    public String convertSeatsToHtml(List<Seat> places);
    public String convertErrorsToHtml(List<ObjectError> errors);
}
