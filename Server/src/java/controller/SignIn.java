/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("status", Boolean.FALSE);

        Session openSession = HibernateUtil.getSessionFactory().openSession();

        JsonObject requestjObject = gson.fromJson(request.getReader(), JsonObject.class);
        String mobile = requestjObject.get("mobile").getAsString();
        String password = requestjObject.get("password").getAsString();


        if (mobile.isEmpty()) {
            responseJsonObject.addProperty("message", "Enter Mobile Number");
        } else if (!Validation.isMobileValid(mobile)) {
            responseJsonObject.addProperty("message", "Invalid Mobile Number");
        } else if (password.isEmpty()) {
            responseJsonObject.addProperty("message", "Enter Password");
        } else {

            Criteria userCriteria = openSession.createCriteria(User.class);
            userCriteria.add(Restrictions.eq("mobile", mobile));
            userCriteria.add(Restrictions.eq("password", password));

            if (!userCriteria.list().isEmpty()) {

                User user = (User) userCriteria.uniqueResult();

                String ServerPath = request.getServletContext().getRealPath("");
                String image = ServerPath + File.separator + "AvatarImages" + File.separator + user.getMobile() + ".png";
                File avatarImage = new File(image);

                if (avatarImage.exists()) {
                    responseJsonObject.addProperty("user_image", Boolean.TRUE);
                } else {
                    responseJsonObject.addProperty("user_image", Boolean.FALSE);
                    responseJsonObject.addProperty("avatar_letter", user.getFirst_name().charAt(0) + "" + user.getLast_name().charAt(0));
                }

                responseJsonObject.add("user", gson.toJsonTree(user));
                responseJsonObject.addProperty("status", Boolean.TRUE);
                openSession.close();

            } else {
                responseJsonObject.addProperty("message", "Invalid Credentials!");
            }

        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
    }
}
