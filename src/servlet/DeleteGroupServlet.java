package servlet;

import jdbc.DataInteraction;
import my.Groups;
import org.json.JSONException;
import sun.java2d.windows.GDIRenderer;

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
@WebServlet("/DeleteGroupServlet")
public class DeleteGroupServlet extends HttpServlet {
    Groups group = new Groups();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("gb2312");
        PrintWriter out = response.getWriter();
        group.setGroupid(request.getParameter("groupid"));
        group.setAccountid(request.getParameter("accountid"));
//        group.setGroupid("4");
//        group.setAccountid("1020");

        try{
            out.print("[{\"DeleteGroup\":\""+ DataInteraction.DeleteGroup(group)+"\"}]");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
