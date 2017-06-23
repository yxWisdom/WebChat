package servlet;

import jdbc.DataInteraction;
import my.Account;
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
@WebServlet("/EditPasswordServlet")
public class EditPasswordServlet extends HttpServlet {
    Account olduser = new Account();
    Account newuser = new Account();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("gb2312");
        PrintWriter out = response.getWriter();
        olduser.setAccountid(request.getParameter("accountid"));
        newuser.setAccountid(request.getParameter("accountid"));
        olduser.setPassword(request.getParameter("oldpass"));
        newuser.setPassword(request.getParameter("newpass"));

        try{
            out.print("[{\"EditPassword\":\""+ DataInteraction.EditPassword(olduser,newuser)+"\"}]");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
