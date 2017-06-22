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


@WebServlet("/ReadUserFriendsServlet")
public class ReadUserFriendsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        Account account  = new Account();
        account.setAccountid(request.getParameter("accountid"));
        //account.setAccountid("1007");
        PrintWriter out = response.getWriter();
        try {

            out.print(DataInteraction.readFriends(account));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
