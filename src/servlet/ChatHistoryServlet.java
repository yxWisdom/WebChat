package servlet;

import jdbc.DataInteraction;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


@WebServlet("/ChatHistoryServlet")
public class ChatHistoryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String accountid = request.getParameter("accountid");
        String friendid = request.getParameter("friendid");
        PrintWriter out = response.getWriter();
        try {
            //实际代码
            out.print(DataInteraction.searchHistory(accountid,friendid));
            //实际代码
            //测试代码
            //out.print(DataInteraction.searchHistory("1027","1028"));
            //测试代码9
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
