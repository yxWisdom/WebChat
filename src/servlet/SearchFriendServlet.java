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


@WebServlet("/SearchFriendServlet")
public class SearchFriendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String accountid = request.getParameter("accountid");
        String findaccountid = request.getParameter("findaccountid");
        String nickname = request.getParameter("nickname");
        PrintWriter out = response.getWriter();
        try {
            //实际代码
            out.print(DataInteraction.findAccount(accountid,findaccountid,nickname));
            //实际代码
            /*//测试代码
            out.print(DataInteraction.findAccount("1027","1027","国"));
            out.print(DataInteraction.findAccount("1027","1028","号"));
            out.print(DataInteraction.findAccount("1027","1028","null"));
            out.print(DataInteraction.findAccount("1027","null","国"));
            //测试代码*/
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
