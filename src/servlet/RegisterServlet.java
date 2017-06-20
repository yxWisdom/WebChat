package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc.DataInteraction;
import my.Account;


@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        if(gender.equals(""))
        System.out.println(gender);
        System.out.println(gender);
        ArrayList photo = new ArrayList();
        photo.add("../img/randomphoto1.jpg");        //头像1
        photo.add("../img/randomphoto2.jpg");        //头像2
        photo.add("../img/randomphoto3.jpg");        //头像3
        photo.add("../img/randomphoto4.jpg");        //头像4
        photo.add("../img/randomphoto5.jpg");        //头像5
        photo.add("../img/randomphoto6.jpg");        //头像6
        photo.add("../img/randomphoto7.jpg");        //头像7
        photo.add("../img/randomphoto8.jpg");        //头像8
        photo.add("../img/randomphoto9.jpg");        //头像9
        photo.add("../img/randomphoto10.jpg");        //头像10
        Random r = new Random();
        int i = r.nextInt(10);
        try {
            //实际代码
            Account account = DataInteraction.register(nickname,password,gender,birthday,photo.get(i).toString());
            //实际代码
            //测试代码
            //Account account = DataInteraction.register("金泰亨", "v",
            //        "男","19951230","3");
            //Account account = DataInteraction.register("田柾国", "kookie",
            //"男","19970901","4");
            //测试代码
            PrintWriter out = response.getWriter();
            out.print("{\"accountid\":\"" + account.getAccountid() + "\"}");
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
}
