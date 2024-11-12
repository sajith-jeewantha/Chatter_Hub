/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.Chat_Status;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "SendChat", urlPatterns = {"/SendChat"})
public class SendChat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", Boolean.FALSE);

        Session openSession = HibernateUtil.getSessionFactory().openSession();
        Transaction beginTransaction = openSession.beginTransaction();

        try {

            String logged_user_id = request.getParameter("user_id");
            String Other_user_id = request.getParameter("other_user_id");
            String message = request.getParameter("message");

            //user
            User logged_user = (User) openSession.get(User.class, Integer.parseInt(logged_user_id));

            //other user
            User other_user = (User) openSession.get(User.class, Integer.parseInt(Other_user_id));
            
            //date
            Date date = new Date();
            
            Chat chat = new Chat();
            chat.setDate(date);
            chat.setFrom_user(logged_user);
            chat.setMessage(message);
            chat.setStatus((Chat_Status) openSession.get(Chat_Status.class, 2));
            chat.setTo_user(other_user);

            logged_user.setChatTime(date);
            other_user.setChatTime(date);

            openSession.save(chat);
            openSession.update(logged_user);
            openSession.update(other_user);

            beginTransaction.commit();
            responseJsonObject.addProperty("success", Boolean.TRUE);
        } catch (Exception e) {
            beginTransaction.rollback();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

    }

}
