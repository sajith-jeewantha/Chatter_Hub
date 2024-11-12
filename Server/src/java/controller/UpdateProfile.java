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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@MultipartConfig
@WebServlet(name = "UpdateProfile", urlPatterns = {"/UpdateProfile"})
public class UpdateProfile extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("status", Boolean.FALSE);

        Session openSession = HibernateUtil.getSessionFactory().openSession();

        String userId = request.getParameter("id");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        Part image = request.getPart("image");

        if (firstName.isEmpty()) {
            responseJsonObject.addProperty("message", "Enter First Name");
        } else if (lastName.isEmpty()) {
            responseJsonObject.addProperty("message", "Enter Last Name");
        } else {
            User user = (User) openSession.get(User.class, Integer.parseInt(userId));
            user.setFirst_name(firstName);
            user.setLast_name(lastName);

            openSession.update(user);
            openSession.beginTransaction().commit();

            String servletPath = request.getServletContext().getRealPath("");
            String imagepath = servletPath + File.separator + "AvatarImages" + File.separator + user.getMobile() + ".png";
            File file = new File(imagepath);

            if (image != null) {
                Files.copy(image.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            if (file.exists()) {
                responseJsonObject.addProperty("user_image", Boolean.TRUE);
            } else {
                responseJsonObject.addProperty("user_image", Boolean.FALSE);
                responseJsonObject.addProperty("avatar_letter", user.getFirst_name().charAt(0) + "" + user.getLast_name().charAt(0));
            }

            responseJsonObject.add("user", gson.toJsonTree(user));
            responseJsonObject.addProperty("status", Boolean.TRUE);

            openSession.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
    }
}
