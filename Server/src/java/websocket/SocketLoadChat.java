///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package websocket;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import entity.Chat;
//import entity.Chat_Status;
//import entity.User;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import javax.websocket.EncodeException;
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import model.HibernateUtil;
//import org.hibernate.Criteria;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Restrictions;
//import org.json.JSONObject;
//
///**
// *
// * @author sajithjeewantha
// */
//@ServerEndpoint("/SocketLoadChat")
//public class SocketLoadChat {
//
//    private int logged_user_id;
//    private int Other_user_id;
//
//    public int getLogged_user_id() {
//        return logged_user_id;
//    }
//
//    public void setLogged_user_id(int logged_user_id) {
//        this.logged_user_id = logged_user_id;
//    }
//
//    public int getOther_user_id() {
//        return Other_user_id;
//    }
//
//    public void setOther_user_id(int Other_user_id) {
//        this.Other_user_id = Other_user_id;
//    }
//
//    private org.hibernate.Session openSession = HibernateUtil.getSessionFactory().openSession();
//    private Gson gson = new Gson();
//
//    @OnOpen
//    public void open(Session session) throws IOException, EncodeException {
//        System.out.println("On Open");
//    }
//
//    @OnClose
//    public void close(Session session) {
//        System.out.println("On Close");
//    }
//
//    @OnError
//    public void onError(Throwable error) {
//        System.out.println("On Error");
//    }
//
//    @OnMessage
//    public void handleMessage(String message, Session session) {
//        System.out.println("Message");
//
//        try {
//            JSONObject jsonObject = new JSONObject(message);
//            setLogged_user_id(jsonObject.getInt("user_id"));
//            setOther_user_id(jsonObject.getInt("other_user_id"));
//
//            session.getBasicRemote().sendText(loadMessages().toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private JsonArray loadMessages() {
//        //user
//        User logged_user = (User) openSession.get(User.class, getLogged_user_id());
//
//        //other user
//        User other_user = (User) openSession.get(User.class, getOther_user_id());
//
//        Criteria chatCriteria = openSession.createCriteria(Chat.class);
//        chatCriteria.add(
//                Restrictions.or(
//                        Restrictions.and(Restrictions.eq("from_user", logged_user), Restrictions.eq("to_user", other_user)),
//                        Restrictions.and(Restrictions.eq("from_user", other_user), Restrictions.eq("to_user", logged_user))
//                )
//        );
//        chatCriteria.addOrder(Order.asc("date"));
//
//        List<Chat> list = chatCriteria.list();
//
//        Chat_Status status = (Chat_Status) openSession.get(Chat_Status.class, 1);
//
//        JsonArray chatArray = new JsonArray();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
//        SimpleDateFormat minutesFormat = new SimpleDateFormat("hh:mm a");
//        SimpleDateFormat todayFormat = new SimpleDateFormat("MMM dd");
//
//        for (Chat chat : list) {
//
//            JsonObject jsonChatObject = new JsonObject();
//            jsonChatObject.addProperty("id", chat.getId());
//            jsonChatObject.addProperty("message", chat.getMessage());
//
//            Date today = new Date();
//            String today2 = todayFormat.format(today);
//
//            Date getdate = chat.getDate();
//            String getdate2 = todayFormat.format(getdate);
//
//            if (today2.equals(getdate2)) {
//                jsonChatObject.addProperty("datetime", "today " + minutesFormat.format(chat.getDate()));
//            } else {
//                jsonChatObject.addProperty("datetime", dateFormat.format(chat.getDate()));
//            }
//
//            if (chat.getFrom_user().getId() == other_user.getId()) {
//
//                jsonChatObject.addProperty("side", "left");
//
//                if (chat.getStatus().getId() == 2) {
//
//                    chat.setStatus(status);
//                    openSession.save(chat);
//                }
//            } else {
//                jsonChatObject.addProperty("side", "right");
//                jsonChatObject.addProperty("status", chat.getStatus().getId());
//            }
//
//            chatArray.add(jsonChatObject);
//        }
//
//        openSession.beginTransaction().commit();
//        openSession.close();
//        return chatArray;
//    }
//}
