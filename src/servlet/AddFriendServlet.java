package servlet;

import jdbc.DataInteraction;
import my.Relations;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/AddFriendServlet")
public class AddFriendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        Relations relation = new Relations();
        ///*实际代码
        relation.setAccountid(request.getParameter("accountid"));
        relation.setFriendid(request.getParameter("friendid"));
        //*/
        ///*测试代码
        //relation.setAccountid("1027");
        //relation.setFriendid("1028");
        //*/
        try
        {
            PrintWriter out = response.getWriter();
            //1:申请成功 -1:申请失败 -2:已发送申请，请耐心等待
            out.print("{\"addInfo\":\""+DataInteraction.insertRelations(relation)+"\"}");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
