/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import model.Play;
import model.Seat;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

/**
 *
 * @author Mihai
 */
@Service("utils")
public class UtilsImpl implements Utils {

    @Override
    public String convertPlaysToHtml(List<Play> plays) {
        String body = "";
        Optional<String> opt = plays.stream().map(p
                -> "                    <tr id=\"" + p.getIdPlay() + "\"" + " onclick=\"fill(" + p.getIdPlay() + ")\">\n"
                + "                       <td style =\"display:none\">" + p.getIdPlay() + "</td>"
                + "                       <td>" + p.getPlayName() + "</td>\n"
                + "                       <td>" + p.getStartDate() + "</td>\n"
                + "                       <td>" + p.getStartTime() + "</td>\n"
                + "                       <td>" + p.getEndTime() + "</td>\n"
                + "                       <td>" + p.getTicketPrice() + "</td>\n"
                + "                    </tr>\n"
        ).collect(Collectors.reducing((i, j) -> i + j));
        if (opt.get() != null) {
            body = "            <table id=\"playTable\" class=\"table\" border=1>\n"
                    + "                <thead>\n"
                    + "                    <tr>\n"
                    + "                       <th>Name</th>\n"
                    + "			<th>Start Date</th>\n"
                    + "			<th>Start Time</th>\n"
                    + "			<th>End Time</th>\n"
                    + "			<th>Price</th>\n"
                    + "                    </tr>\n"
                    + "                </thead>\n"
                    + "                <tbody>\n";
            body = body + opt.get();
            body = body + "                </tbody>\n            </table>";
        }
        return body;
    }

    @Override
    public String convertSeatsToHtml(List<Seat> places) {
        String body = "";
        Optional<String> opt = places.stream().map(p
                -> "                    <tr id=\"" + p.getIdSeat() + "\">\n"
                + "                       <td style =\"display:none\">" + p.getIdSeat() + "</td>"
                + "                       <td>" + p.getSeatNumber() + "</td>\n"
                + "                       <td>" + p.getAvailability() + "</td>\n"
                + "                       <td>" + p.getName() + "</td>\n"
                + "                       <td><input type=\"button\" class=\"btn-primary\" onclick=\"reserve(" + p.getIdSeat() + "," + "'" + p.getAvailability() + "'" + ")\" value=\"reserve\" /></td>"
                + "                       <td><input type=\"button\" class=\"btn-primary\" onclick=\"cancel(" + p.getIdSeat() + "," + "'" + p.getAvailability() + "'" + ")\" value=\"cancel\" /></td>"
                + "                    </tr>\n"
        ).collect(Collectors.reducing((i, j) -> i + j));
        if (opt.get() != null) {
            body = "            <table class=\"table\" border=1>\n"
                    + "                <thead>\n"
                    + "                    <tr>\n"
                    + "                       <th>Seat Number</th>\n"
                    + "			<th>Availability</th>\n"
                    + "			<th>Name</th>\n"
                    + "			<th>Reserve Seat</th>\n"
                    + "			<th>Cancel</th>\n"
                    + "                    </tr>\n"
                    + "                </thead>\n"
                    + "                <tbody>\n";
            body = body + opt.get();
            body = body + "                </tbody>\n            </table>";
        }
        return body;
    }

    @Override
    public String convertErrorsToHtml(List<ObjectError> errors) {
        String errorString = errors.stream().map(p -> "<p style=\"color:red\">" + p.getDefaultMessage() + "!</p>").reduce((i, j) -> i + "\n" + j).orElse("");
        return errorString;
    }
}
