package servlet;

import jdbc.DataInteraction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/DeleteHistoryServlet")
public class DeleteHistoryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        ///*实际代码
        String   accountid = request.getParameter("accountid");
        String[] messageid=request.getParameterValues("messageid[]");
        //实际代码*/
        ///*测试
        //String   accountid = "1027";
        //String[] messageid={"3","4","5","6"};
        //测试*/
        PrintWriter out = response.getWriter();
        try {
            out.print(DataInteraction.deleteHistory(accountid, messageid));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
