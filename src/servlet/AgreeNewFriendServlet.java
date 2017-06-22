package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import my.*;
import jdbc.*;
import org.json.JSONException;

/**
 * Created by ALN on 2017/6/20.
 */
@WebServlet("/AgreeNewFriendServlet")
public class AgreeNewFriendServlet extends HttpServlet {
    Relations uprelate = new Relations();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("gb2312");
        PrintWriter out = response.getWriter();
        uprelate.setAccountid(request.getParameter("accountid"));
        uprelate.setFriendid(request.getParameter("friendid"));

        try{
            out.print("{\"ifNew\":\""+DataInteraction.AgreeNewFriend(uprelate)+"\"}");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
