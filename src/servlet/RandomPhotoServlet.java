package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.print.DocFlavor;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RandomPhotoServlet")
public class RandomPhotoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList photo = new ArrayList();
        photo.add("1");        //头像1
        photo.add("2");        //头像2
        photo.add("3");        //头像3
        photo.add("4");        //头像4
        photo.add("5");        //头像5
        photo.add("6");        //头像6
        photo.add("7");        //头像7
        photo.add("8");        //头像8
        photo.add("9");        //头像9
        photo.add("10");        //头像10
        Random r = new Random();
        int i = r.nextInt(10);
        PrintWriter out = response.getWriter();
        out.print("{\"" +
                "\":\"" + photo.get(i).toString()+"\"}");
    }
}
