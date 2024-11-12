/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author sajithjeewantha
 */
public class Validation {

    public Validation() {
    }

    /**
     * Returns true or false
     *
     * @param mobile
     * @return true or false
     */
    public static boolean isMobileValid(String mobile) {
        return mobile.matches("^[0]{1}[1|3|7]{1}[0|1|2|4|5|6|7|8]{1}[0-9]{7}$");
    }

    /**
     * Returns true or false
     *
     * @param email
     * @return true or false
     */
    public static boolean isEmailValid(String email) {
        return email.matches("^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*)@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$");
    }

    /**
     * Returns true or false
     *
     * @param password
     * @return true or false
     */
    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=]).{8,}$");
    }

    /**
     * Returns true or false
     *
     * @param Double
     * @return true or false
     */
    public static boolean isDouble(String text) {
        return text.matches("^\\d+(\\.\\d{2})?$");
    }

    /**
     * Returns true or false
     *
     * @param Integer
     * @return true or false
     */
    public static boolean isInteger(String text) {
        return text.matches("^\\d+$");
    }
    
    /**
     * Returns true or false
     *
     * @param year
     * @return true or false
     */
    public static boolean isYearValid(String year) {
        return year.matches("^\\d{4}$");
    }
}
