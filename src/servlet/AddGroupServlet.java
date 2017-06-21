package servlet;

import jdbc.DataInteraction;
import my.Groups;
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
@WebServlet("/AddGroupServlet")
public class AddGroupServlet extends HttpServlet {
    Groups group= new Groups();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("gb2312");
        PrintWriter out = response.getWriter();
        //group.setAccountid(request.getParameter("accountid"));
        //group.setName(request.getParameter("groupname"));
        group.setAccountid("1000");
        group.setName("ADD_1");

        try{
            out.print("[{\"ADDGroup\":\""+ DataInteraction.ADDGroup(group)+"\"}]");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
