package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc.DataInteraction;
import my.Account;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    public void init() throws ServletException {
        super.init();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String accountid = request.getParameter("accountid");
        String password = request.getParameter("password");

        try {
            PrintWriter out = response.getWriter();
            Account account = DataInteraction.login(accountid, password);
            if(null == account.getAccountid()){
                out.print("{\"logInfo\":\"-1\"}");//用户不存在
            }else if(null == account.getPassword()){
                out.print("{\"logInfo\":\"-2\"}");//密码错误
            }else{
                out.print("{\"logInfo\":\"" + account.getAccountid()+"\"}");// 根据accountid查找info
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    public void destroy() {
        super.destroy();
    }
}
