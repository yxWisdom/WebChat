package jdbc;


import java.io.IOException;
import java.io.PrintWriter;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import my.Account;
import my.Info;
import my.Message;
import my.Groups;
import my.Relations;


public class DataInteraction {
    public static String resultSetToJson(ResultSet rs) throws SQLException,JSONException
    {
        // json数组
        JSONArray array = new JSONArray();

        // 获取列数
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 遍历ResultSet中的每条数据
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();

            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName =metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            array.put(jsonObj);
        }

        return array.toString();
    }

    // 连接地址是由各个数据库生产商单独提供的，所以需要单独记住
    // 本地主机地址：192.168.56.1
    // SID或者service_name:orcl.168.56.1
    // eg:
    // sid_url:jdbc:oracle:thin:@192.168.56.1:1521:orclzoe
    // service_name_url:sid_urljdbc:oracle:thin:@//192.168.56.1:1521/orcl.168.56.1

    public static final String DBURL = "jdbc:oracle:thin:@//192.168.56.1:1521/orcl.168.56.1";
    // 连接数据库的用户名
    public static final String DBUSER = "scott";
    // 连接数据库的密码
    public static final String DBPWD = "tiger";

    public static Account login(String accountid, String password)throws ClassNotFoundException ,SQLException {

        Account account = new Account();

        Class.forName("oracle.jdbc.OracleDriver");
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        String queryString = "Select * From Account Where accountid='" + Integer.parseInt(accountid) + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(queryString);
        if (rs.next()) {

            account.setAccountid(accountid);
            System.out.print(rs.getString("password"));
            System.out.print(password);
            if (rs.getString("password").equals(password))
            {account.setPassword(password);}
        }
        rs.close();
        stmt.close();
        con.close();
        return account;
    }

    public static Account register(String nickname ,String password,String gender,String birthday ,String photo
    )throws ClassNotFoundException ,SQLException {

        Account account = new Account();
        Class.forName("oracle.jdbc.OracleDriver");
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPWD);

        Statement stmt_insert = con.createStatement();
        String insertAccount = "INSERT INTO ACCOUNT(accountid,password)VALUES (ACCOUNTORDER.nextval,'"+ password + "')";
        stmt_insert.executeUpdate(insertAccount);
        String insertInfo = "INSERT INTO INFO(accountid,nickname,gender,birthday,photo) " +
                "VALUES (ACCOUNTORDER.currval ,'" + nickname + "','" + gender +
                "',to_date('" + birthday + "','yyyy-mm-dd'),'" + photo + "')";
        stmt_insert.executeUpdate(insertInfo);
        stmt_insert.close();
        String selectString = "Select accountid From INFO Where INFO.nickname ='" + nickname +
                "' and INFO.gender = '" + gender + "' and INFO.birthday =to_date('" + birthday +"','yyyy-mm-dd hh24:mi:ss')"
                +"and INFO.photo = '" + photo + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(selectString);
        if (rs.next()) {
            /*info*/
            account.setAccountid(rs.getString("accountid"));
            String checkString = "Select * From ACCOUNT Where ACCOUNTID ='"  + account.getAccountid() + "'";
            Statement stmt_check = con.createStatement();
            ResultSet rs_check = stmt_check.executeQuery(checkString);
            if (rs_check.next()) {
                if (rs_check.getString("password").equals(password))
                {account.setPassword(password);}
                else{
                    account.setAccountid("0");
                }
            }
            rs_check.close();
            stmt_check.close();
        }
        rs.close();
        stmt.close();
        con.close();
        return account;
    }

    public static String findAccount(String accountid, String findaccountid ,String nickname)throws ClassNotFoundException ,SQLException ,JSONException{

        Class.forName("oracle.jdbc.OracleDriver");
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPWD);

        String selectString = "";
        if(findaccountid.equals("null"))
        {selectString = "Select * From INFO Where  nickname LIKE '%" + nickname +
                "%'and accountid != '" + accountid + "'";}
        else if (nickname.equals("null") )
        {selectString = "Select * From INFO Where accountid ='" +  findaccountid +
                "'and accountid != '" + accountid + "'";}
        else {
            selectString = "Select * From INFO Where (accountid ='" + findaccountid +
                    "' OR nickname LIKE '%" + nickname + "%') " +
                    "and accountid != '" + accountid + "'";
        }
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(selectString);
        // json数组
        JSONArray array = new JSONArray();

        // 获取列数
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 遍历ResultSet中的每条数据
        int isFriend = 0;
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();

            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName =metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            String value = rs.getString("accountid");
            String checkString = "Select * From RELATIONS Where ACCOUNTID ='"  + accountid + "'and FRIENDID ='"
                    + value + "'and state = '1' ";
            Statement stmt_check = con.createStatement();
            ResultSet rs_check = stmt_check.executeQuery(checkString);
            if (rs_check.next()) {
                isFriend = 1;
            }
            rs_check.close();
            stmt_check.close();
            jsonObj.put("isFriend", isFriend);
            array.put(jsonObj);
            isFriend = 0;
        }
        rs.close();
        stmt.close();
        con.close();
        return array.toString();
    }

    public static boolean update(String updateString) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.OracleDriver");
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = con.createStatement();
        stmt.executeUpdate(updateString);
        stmt.close();
        con.close();
        return true;
    }

    public static int insertRelations(Relations relation) throws ClassNotFoundException, SQLException {

        Class.forName("oracle.jdbc.OracleDriver");
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        String checkString = "Select * From Relations Where ACCOUNTID ='"  + relation.getAccountid() +
                "'and FRIENDID = '" + relation.getFriendid() + "'";
        Statement stmt_check = con.createStatement();
        ResultSet rs_check = stmt_check.executeQuery(checkString);
        if (rs_check.next()) {
            rs_check.close();
            stmt_check.close();
            con.close();
            return -2;//已发送申请，请耐心等待
        }
        Statement stmt = con.createStatement();
        String insertString = "Insert Into Relations(accountid,friendid) Values('"
                + relation.getAccountid() + "','" + relation.getFriendid() + "')";

        int success1 = stmt.executeUpdate(insertString);

        insertString = "Insert Into Relations(accountid,friendid) Values('"
                + relation.getFriendid() + "','" + relation.getAccountid() + "')";
        int success2 = stmt.executeUpdate(insertString);
        stmt.close();
        con.close();
        if(0 == success1 | 0 == success2) return -1;//申请失败
        return 1;//申请成功
    }

    public static String readNewFriends(Account user) throws ClassNotFoundException, java.sql.SQLException,JSONException{
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        ResultSet rs = stmt.executeQuery("Select friendid,nickname,photo From RELATIONS RE,INFO FR WHERE RE.STATE = 0 AND RE.ACCOUNTID="+ user.getAccountid()+" AND RE.FRIENDID = FR.ACCOUNTID");
        String usernewfriends = DataInteraction.resultSetToJson(rs);
        rs.close();
        stmt.close();
        conn.close();
        return usernewfriends;
    }

    //////////////////////
    public static String readFriends(Account user) throws ClassNotFoundException, java.sql.SQLException,JSONException{
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        String retString = "\0";
        ResultSet rs = null;
        ResultSet rs1 = stmt.executeQuery("Select GROUPID From GROUPS WHERE RACCOUNTID="+ user.getAccountid());
        while (rs1.next()){
            String groupid = rs.getString("GROUPID");
            rs = stmt.executeQuery("SELECT INFO.ACCOUNTID,INFO.NICKNAME,INFO.PHOTO,INFO.BIRTHDAY,INFO.GENDER " +
                    "FROM RELATIONS,INFO WHERE RELATIONS.ACCOUNTID="+user.getAccountid()+"AND RELATIONS.FRIENDID = INFO.ACCOUNTID");
            retString = retString + groupid + DataInteraction.resultSetToJson(rs);
        }
        rs.close();
        rs1.close();
        stmt.close();
        conn.close();
        return retString;
    }

    /////////////////////
    public static String findUnreadFriends(Account user) throws ClassNotFoundException, java.sql.SQLException,JSONException{
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        ResultSet rs = stmt.executeQuery("Select SENDER,nickname,photo,time,text From INFO FR,RELATIONS RE,(SELECT * FROM MESSAGE where receiver = "+ user.getAccountid()+" order by time desc) WHERE READ = 0 "
                +" AND SENDER = FR.ACCOUNTID  AND ROWNUM = 1"); //得到结果集Statement stmt = con.createStatement()
        String userfriends = DataInteraction.resultSetToJson(rs);
        rs.close();
        stmt.close();
        conn.close();
        return userfriends;
    }///////////////////////////

    public static Info readInfo(Info userInfo) throws ClassNotFoundException, java.sql.SQLException{
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        ResultSet rs = stmt.executeQuery("Select * From Info WHERE ACCOUNTID="+ userInfo.getAccountid()); //得到结果集Statement stmt = con.createStatement()
        if(rs.next()) {
            userInfo.setNickname(rs.getString("nickname"));
            userInfo.setGender(rs.getString("gender"));
            userInfo.setBirthday(rs.getString("birthday"));
            userInfo.setPhoto(rs.getString("photo"));
        }
        rs.close();
        stmt.close();
        conn.close();
        return userInfo;
    }

    ////////////////
    public static String AgreeNewFriend(Relations relate) throws ClassNotFoundException, java.sql.SQLException, JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        stmt.executeUpdate("UPDATE RELATIONS SET STATE=1,GROUPID=0 WHERE ACCOUNTID=" + relate.getAccountid()+ "AND FRIENDID="+ relate.getFriendid());
        stmt.executeUpdate("UPDATE RELATIONS SET STATE=1,GROUPID=0 WHERE FRIENDID=" + relate.getAccountid()+ "AND ACCOUNTID="+ relate.getFriendid());
        stmt.close();
        conn.close();
        conn = DriverManager.getConnection("jdbc:oracle:thin:@ALN-VAIO:1521:orcl", "scott", "tiger");//创建连接
        stmt = conn.createStatement();//创建statement
        ResultSet rs1 = stmt.executeQuery("SELECT STATE FROM RELATIONS WHERE ACCOUNTID=" + relate.getAccountid()+ "AND FRIENDID="+ relate.getFriendid());
        ResultSet rs2 = stmt.executeQuery("SELECT STATE FROM RELATIONS WHERE FRIENDID=" + relate.getAccountid()+ "AND ACCOUNTID="+ relate.getFriendid());
        String ret= DataInteraction.resultSetToJson(rs1);
        rs1.close();
        rs2.close();
        stmt.close();
        conn.close();
        return ret;
        /*if (rs1.getString("state") == "0") {
            ret = "-1";
        }
        else if(rs2.getString("state") == "0") {
            ret = "-2";
        }
        else {
            ret= "1";
        }
        rs1.close();
        rs2.close();
        stmt.close();
        conn.close();
        return ret;*/
    }
    //aln
    public static boolean deleteFriend(String accountid, String friendid) throws ClassNotFoundException, SQLException {

        String deleteString = "Delete from RELATIONS where (ACCOUNTID = '"+accountid+"' and FRIENDID = '"+friendid
                +"') or (ACCOUNTID = '"+friendid+"' and FRIENDID = '"+accountid+"')";
        return update(deleteString);//申请成功
    }
    public static String searchHistory(String accountid, String friendid)throws ClassNotFoundException ,SQLException ,JSONException{

        Class.forName("oracle.jdbc.OracleDriver");
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        String selectString = "Select * From MESSAGE where (sender = '"+accountid+"' and receiver = '"+friendid
                +"'and (read = '1' or read = '3' ) ) or (sender = '"+friendid+"' and receiver = '"+accountid+"'and (read = '1' or read = '2'))";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(selectString);
        String jsonString = resultSetToJson(rs);
        rs.close();
        stmt.close();
        con.close();
        return jsonString;
    }
    public static String getAccountInfo(String accountid)throws ClassNotFoundException ,SQLException ,JSONException{

        Class.forName("oracle.jdbc.OracleDriver");
        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        String selectString = "Select * From Info where (accountid = '"+accountid+"')";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(selectString);
        String jsonString = resultSetToJson(rs);
        rs.close();
        stmt.close();
        con.close();
        return jsonString;
    }
    public static boolean delete(String tableName, String key, String[] ids)
            throws ClassNotFoundException, SQLException {
        String deleteString = "Delete From " + tableName + " Where " + key + " in (";
        for (int i = 0; i < ids.length; ++i) {
            deleteString += "'" + ids[i] + "',";
        }
        deleteString = deleteString.substring(0, deleteString.length() - 1);
        deleteString += ")";
        return update(deleteString);
    }
    public static boolean deleteHistory(String accountid, String[] ids)
            throws ClassNotFoundException, SQLException {

        String deleteString = "Delete From message Where (read = '2' or read = '3' ) and messageid in (";
        for (int i = 0; i < ids.length; ++i) {
            deleteString += "'" + ids[i] + "',";
        }
        deleteString = deleteString.substring(0, deleteString.length() - 1);
        deleteString += ")";
        update(deleteString);

        String updateString = "Update message Set read='2'  Where read = '1' and sender = '"+accountid
                +"'and messageid in (";
        for (int i = 0; i < ids.length; ++i) {
            updateString += "'" + ids[i] + "',";
        }
        updateString = updateString.substring(0, updateString.length() - 1);
        updateString += ")";
        update(updateString);

        updateString = "Update message Set read='3'  Where read = '1' and receiver = '"+accountid
                +"'and messageid in (";
        for (int i = 0; i < ids.length; ++i) {
            updateString += "'" + ids[i] + "',";
        }
        updateString = updateString.substring(0, updateString.length() - 1);
        updateString += ")";
        return update(updateString);
    }
}