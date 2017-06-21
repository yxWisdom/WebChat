package servlet;

import jdbc.DataInteraction;
import my.*;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by ALN on 2017/6/20.
 */
@WebServlet("/ReadUserFriendsServlet")
public class ReadUserFriendsServlet extends HttpServlet {

    Account user=new Account();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("gb2312");
        PrintWriter out = response.getWriter();
        //user.setAccountid(request.getParameter("accountid"));
        user.setAccountid("1000");


        try{
            String userFriends = DataInteraction.readFriends(user);
            if(userFriends == null)
                out.print("{\"userFriends\":\"0\"}");
            else
                out.print(userFriends);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
