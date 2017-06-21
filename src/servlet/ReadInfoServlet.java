package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jdbc.DataInteraction;
import my.Info;

/**
 * Created by ALN on 2017/6/19.
 */
@WebServlet("/ReadInfoServlet")
public class ReadInfoServlet extends HttpServlet{

        Info userInfo = new Info();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//建立连接,传入参数String id
// 依次读取==id的数据
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("gb2312");
        PrintWriter out = response.getWriter();
        userInfo.setAccountid(request.getParameter("accountid"));

        try {
            userInfo = DataInteraction.readInfo(userInfo);
            if(userInfo.getAccountid() == null){
                out.print("{\"accountid\":\"-1\"}");
            }
            else {
                out.print("{\"ACCOUNTID\":\""+ userInfo.getAccountid()+
                        "\",\"NICKNAME\":\"" + userInfo.getNickname()+
                        "\",\"GENDER\":\"" + userInfo.getGender()+
                        "\",\"BIRTHDAY\":\"" + userInfo.getBirthday()+
                        "\",\"PHOTO\":\"" + userInfo.getPhoto()+ "\"}");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
