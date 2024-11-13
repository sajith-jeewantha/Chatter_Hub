/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Chat;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "DeleteChat", urlPatterns = {"/DeleteChat"})
public class DeleteChat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");

//        System.out.println(id);
//        System.out.println("Hello");
        Session openSession = HibernateUtil.getSessionFactory().openSession();

        Chat chat = (Chat) openSession.get(Chat.class, Integer.parseInt(id));

        if (chat != null) {
//            System.out.println(chat.getMessage());
            
            openSession.delete(chat);
            openSession.beginTransaction().commit();
        }

        openSession.close();
    }

}
