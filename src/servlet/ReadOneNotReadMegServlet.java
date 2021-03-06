package servlet;

import jdbc.DataInteraction;
import my.Message;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by ALN on 2017/6/21.
 */
@WebServlet("/ReadOneNotReadMegServlet")
public class ReadOneNotReadMegServlet extends HttpServlet {
    private Message meg = new Message();
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("gb2312");
        PrintWriter out = response.getWriter();
        meg.setSender(request.getParameter("sender"));
        meg.setReceiver(request.getParameter("receiver"));

        try{
            String FindUnreadTexts = DataInteraction.FindUnreadTexts(meg);
            out.print(FindUnreadTexts);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
