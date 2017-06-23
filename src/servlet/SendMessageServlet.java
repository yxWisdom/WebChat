package servlet;

import jdbc.DataInteraction;
import my.Message;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by ALN on 2017/6/21.
 */
@WebServlet("/SendMessageServlet")
public class SendMessageServlet extends HttpServlet {
    Message meg = new Message();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("gb2312");
        PrintWriter out = response.getWriter();
        meg.setSender(request.getParameter("sender"));
        meg.setReceiver(request.getParameter("receiver"));
        meg.setText(request.getParameter("text"));
//        meg.setSender("1020");
//        meg.setReceiver("1000");
//        meg.setText("hello my name is test5");

        try{
            out.print("[{\"SendMessage\":\""+DataInteraction.SendMessage(meg)+"\"}]");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
