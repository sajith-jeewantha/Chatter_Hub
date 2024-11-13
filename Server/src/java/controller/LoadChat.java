/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.Chat_Status;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "LoadChat", urlPatterns = {"/LoadChat"})
public class LoadChat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session openSession = HibernateUtil.getSessionFactory().openSession();
        Gson gson = new Gson();

        String logged_user_id = request.getParameter("user_id");
        String Other_user_id = request.getParameter("other_user_id");

        //user
        User logged_user = (User) openSession.get(User.class, Integer.parseInt(logged_user_id));

        //other user
        User other_user = (User) openSession.get(User.class, Integer.parseInt(Other_user_id));

        Criteria chatCriteria = openSession.createCriteria(Chat.class);
        chatCriteria.add(
                Restrictions.or(
                        Restrictions.and(Restrictions.eq("from_user", logged_user), Restrictions.eq("to_user", other_user)),
                        Restrictions.and(Restrictions.eq("from_user", other_user), Restrictions.eq("to_user", logged_user))
                )
        );
        chatCriteria.addOrder(Order.desc("date"));

        List<Chat> list = chatCriteria.list();

        Chat_Status status = (Chat_Status) openSession.get(Chat_Status.class, 1);

        JsonArray chatArray = new JsonArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
        SimpleDateFormat minutesFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat todayFormat = new SimpleDateFormat("MMM dd");

        for (Chat chat : list) {

            JsonObject jsonChatObject = new JsonObject();
            jsonChatObject.addProperty("id", chat.getId());
            jsonChatObject.addProperty("message", chat.getMessage());

            Date today = new Date();
            String today2 = todayFormat.format(today);

            Date getdate = chat.getDate();
            String getdate2 = todayFormat.format(getdate);

            if (today2.equals(getdate2)) {
                jsonChatObject.addProperty("datetime", "today " + minutesFormat.format(chat.getDate()));
            } else {
                jsonChatObject.addProperty("datetime", dateFormat.format(chat.getDate()));
            }

            if (chat.getFrom_user().getId() == other_user.getId()) {

                jsonChatObject.addProperty("side", "left");

//                System.out.println(chat.getStatus().getId());
                if (chat.getStatus().getId() == 2) {

//                    System.out.println("Have Not seen Messages");
                    chat.setStatus(status);
                    openSession.save(chat);
//                    System.out.println("Change status to 1");
                }

            } else {
//                System.out.println(chat.getStatus().getId());

                jsonChatObject.addProperty("side", "right");
                jsonChatObject.addProperty("status", chat.getStatus().getId());
            }

            chatArray.add(jsonChatObject);
        }

        openSession.beginTransaction().commit();
        openSession.close();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(chatArray));
    }

}
