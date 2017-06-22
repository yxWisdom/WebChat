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


@WebServlet("/UpdateAccountInfoServlet")
public class UpdateAccountInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        String accountid = request.getParameter("accountid");
        String nickname = request.getParameter("nickname");;
        String gender = request.getParameter("gender");;
        String birthday = request.getParameter("birthday");;
        PrintWriter out = response.getWriter();
        try {
            //实际代码
            out.print(DataInteraction.updateAccountInfo(accountid,nickname,gender,birthday));
            //实际代码
            //测试代码
            //out.print(DataInteraction.updateAccountInfo("1007","闵玧智","女","20161112"));
            //测试代码
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
