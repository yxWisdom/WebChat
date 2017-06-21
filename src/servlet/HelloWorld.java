package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        response.setContentType("text/html");
        response.setCharacterEncoding("gb2312");
        PrintWriter out = response.getWriter();
        out.println("<table border=1>");
        out.println("<tr><td>Content:</td></tr>");
        try {

            Class.forName("oracle.jdbc.OracleDriver");//加载驱动
            conn = DriverManager.getConnection("jdbc:oracle:thin:@ALN-VAIO:1521:orcl", "scott", "tiger");//创建连接
            stmt = conn.createStatement();//创建statement
            rs = stmt.executeQuery("select EMPNO from EMP"); //得到结果集
            while (rs.next()) {//遍历结果集
                out.println("<tr>");
                out.println("<td>" + rs.getString("empno") + "</td>");//取出列值
                out.println("</tr>");
            }
            out.println("</table>");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
