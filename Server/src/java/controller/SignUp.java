/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import entity.User_Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */

@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("status", Boolean.FALSE);

        Session openSession = HibernateUtil.getSessionFactory().openSession();

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        String mobile = requestJsonObject.get("mobile").getAsString();
        String firstName = requestJsonObject.get("firstname").getAsString();
        String lastName = requestJsonObject.get("lastname").getAsString();
        String password = requestJsonObject.get("password").getAsString();

         if (firstName.isEmpty()) {
            responseJsonObject.addProperty("message", "Enter First Name");
        } else if (lastName.isEmpty()) {
            responseJsonObject.addProperty("message", "Enter Last Name");
        } else if (mobile.isEmpty()) {
            responseJsonObject.addProperty("message", "Enter Mobile Number");
        } else if (!Validation.isMobileValid(mobile)) {
            responseJsonObject.addProperty("message", "Invalid Mobile Number");
        } else if (password.isEmpty()) {
            responseJsonObject.addProperty("message", "Enter Password");
        } else {
            Criteria userCriteria = openSession.createCriteria(User.class);
            userCriteria.add(Restrictions.eq("mobile", mobile));

            if (!userCriteria.list().isEmpty()) {
                responseJsonObject.addProperty("message", "This Mobile Number is already use");
            } else {

                User user = new User();
                user.setFirst_name(firstName);
                user.setLast_name(lastName);
                user.setMobile(mobile);
                user.setPassword(password);
                user.setRegistered_date_time(new Date());
                user.setStatus((User_Status) openSession.get(User_Status.class, 2));

                openSession.save(user);
                openSession.beginTransaction().commit();

                responseJsonObject.addProperty("status", Boolean.TRUE);
                responseJsonObject.addProperty("message", "success");

                openSession.close();
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
    }
}
