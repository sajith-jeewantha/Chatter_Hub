/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.User;
import entity.User_Status;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "LoadHomeData", urlPatterns = {"/LoadHomeData"})
public class LoadHomeData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Date today = new Date();
        JsonObject respJsonObject = new JsonObject();
        respJsonObject.addProperty("success", Boolean.FALSE);
        respJsonObject.addProperty("message", "Unable to process your requst");

        try {

            Session openSession = HibernateUtil.getSessionFactory().openSession();

            String userId = request.getParameter("id");
            String search = request.getParameter("search");

            User user = (User) openSession.get(User.class, Integer.parseInt(userId));
            user.setStatus((User_Status) openSession.get(User_Status.class, 1));
            openSession.update(user);
            openSession.beginTransaction().commit();

            Criteria userCriteria = openSession.createCriteria(User.class);
            userCriteria.add(Restrictions.ne("id", Integer.parseInt(userId)));
            userCriteria.addOrder(Order.desc("chatTime"));

            if (!search.equals("no")) {
                userCriteria.add(
                        Restrictions.or(
                                Restrictions.like("mobile", search, MatchMode.START),
                                Restrictions.like("first_name", search, MatchMode.START),
                                Restrictions.like("last_name", search, MatchMode.START)
                        )
                );
            }

            List<User> otherUsersList = userCriteria.list();
            JsonArray jsonCharArray = new JsonArray();

            for (User otherUser : otherUsersList) {

                Criteria Messagecriteria = openSession.createCriteria(Chat.class);
                Messagecriteria.add(
                        Restrictions.or(
                                Restrictions.and(
                                        Restrictions.eq("from_user", user),
                                        Restrictions.eq("to_user", otherUser)
                                ),
                                Restrictions.and(
                                        Restrictions.eq("from_user", otherUser),
                                        Restrictions.eq("to_user", user)
                                )
                        )
                );

                Messagecriteria.addOrder(Order.desc("date"));
                Messagecriteria.setMaxResults(1);

                otherUser.setPassword("");

                JsonObject chatItem = new JsonObject();
                chatItem.addProperty("user_id", otherUser.getId());
                chatItem.addProperty("user_mobile", otherUser.getMobile());
                chatItem.addProperty("user_name", otherUser.getFirst_name() + " " + otherUser.getLast_name());
                chatItem.addProperty("user_status", otherUser.getStatus().getId());

                String ServerPath = request.getServletContext().getRealPath("");
                String image = ServerPath + File.separator + "AvatarImages" + File.separator + otherUser.getMobile() + ".png";
                File avatarImage = new File(image);

                if (avatarImage.exists()) {
                    chatItem.addProperty("user_image", Boolean.TRUE);
                } else {
                    chatItem.addProperty("user_image", Boolean.FALSE);
                    chatItem.addProperty("avatar_letter", otherUser.getFirst_name().charAt(0) + "" + otherUser.getLast_name().charAt(0));
                }

                List<Chat> chatList = Messagecriteria.list();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd hh:mm a");
                SimpleDateFormat minutesFormat = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat todayFormat = new SimpleDateFormat("MMM dd");

                if (Messagecriteria.list().isEmpty()) {
                    chatItem.addProperty("message", "Say 'Hi' to " + otherUser.getFirst_name() + " " + otherUser.getLast_name());
                    chatItem.addProperty("dateTime", "");
                    chatItem.addProperty("chat_status_id", 1);

                } else {
                    chatItem.addProperty("message", chatList.get(0).getMessage());

                    String today2 = todayFormat.format(today);
                    Date getdate = chatList.get(0).getDate();
                    String getdate2 = todayFormat.format(getdate);

                    if (today2.equals(getdate2)) {
                        chatItem.addProperty("dateTime", "Today " + minutesFormat.format(chatList.get(0).getDate()));
                    } else {
                        chatItem.addProperty("dateTime", dateFormat.format(chatList.get(0).getDate()));
                    }

                    if (otherUser.getId() == chatList.get(0).getFrom_user().getId()) {
                        chatItem.addProperty("chat_status_id", chatList.get(0).getStatus().getId());
                    }
                }

                jsonCharArray.add(chatItem);
            }

            respJsonObject.addProperty("success", Boolean.TRUE);
            respJsonObject.addProperty("message", "Success");
//            respJsonObject.add("user", gson.toJsonTree(user));
//            respJsonObject.addProperty("otherUserList", gson.toJson(otherUsersList));
            respJsonObject.add("jsonchatArray", gson.toJsonTree(jsonCharArray));

            openSession.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

       
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(respJsonObject));

    }
}
