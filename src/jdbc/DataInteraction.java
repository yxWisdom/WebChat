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
        if(null == findaccountid)
        {selectString = "Select * From INFO Where  nickname LIKE '%" + nickname +
                "%'and accountid != '" + accountid + "'";}
        else if (null == nickname )
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
//aln
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
    }

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

    public static int AgreeNewFriend(Relations relate) throws ClassNotFoundException, java.sql.SQLException, JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        int check1 = stmt.executeUpdate("UPDATE RELATIONS SET STATE=1,GROUPID=0 WHERE ACCOUNTID=" + relate.getAccountid()+ "AND FRIENDID="+ relate.getFriendid());
        int check2 = stmt.executeUpdate("UPDATE RELATIONS SET STATE=1,GROUPID=0 WHERE FRIENDID=" + relate.getAccountid()+ "AND ACCOUNTID="+ relate.getFriendid());

        stmt.close();
        conn.close();

        if( 0 == check1 | 0 == check2 ) return -1;
        else return 1;
    }

    public static String FindUnreadTexts(Message meg) throws ClassNotFoundException, java.sql.SQLException, JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        ResultSet rs = stmt.executeQuery("SELECT  * FROM MESSAGE " +
                "WHERE SENDER=" +
                meg.getSender()+
                "AND RECEIVER=" +
                meg.getReceiver() +
                "AND READ=0");
        String retString = DataInteraction.resultSetToJson(rs);

        Statement updatestmt = conn.createStatement();
        ResultSet updaters = updatestmt.executeQuery("SELECT MESSAGEID FROM MESSAGE " +
                "WHERE SENDER=" +
                meg.getSender()+
                "AND RECEIVER=" +
                meg.getReceiver() +
                "AND READ=0");
        while (updaters.next()){
            Statement dostmt = conn.createStatement();
            dostmt.executeUpdate("UPDATE MESSAGE SET READ=1 WHERE MESSAGEID=" +
                    updaters.getString("MESSAGEID"));
            dostmt.close();
        }
        updaters.close();
        updatestmt.close();
        rs.close();
        stmt.close();
        conn.close();
        return retString;
    }

    public static int SendMessage(Message meg) throws ClassNotFoundException, java.sql.SQLException, JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        int result = stmt.executeUpdate("INSERT INTO MESSAGE(MESSAGEID, TIME, SENDER, RECEIVER, TEXT, READ) " +
                "VALUES(MESSAGEORDER.nextval,SYSDATE,'" +
                meg.getSender()+
                "','" +
                meg.getReceiver() +
                "','"+
                meg.getText() +
                "',0)");

        stmt.close();
        conn.close();
        if( 0 == result )
            return -1;
        else
            return 1;
    }

    public static String ShowAllGroups(Groups group) throws ClassNotFoundException, java.sql.SQLException, JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        ResultSet rs = stmt.executeQuery("SELECT * FROM GROUPS " +
                "WHERE ACCOUNTID=" +
                group.getAccountid() + "Order by GROUPID");
        String retString = DataInteraction.resultSetToJson(rs);

        rs.close();
        stmt.close();
        conn.close();
        return retString;
    }

    public static int EditGroupName(Groups group) throws ClassNotFoundException, java.sql.SQLException, JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        int rs = stmt.executeUpdate("UPDATE GROUPS SET NAME='" +
                group.getName()+
                "' WHERE GROUPID=" +
                group.getGroupid());

        stmt.close();
        conn.close();
        if( 0 == rs){
            return -1;
        }
        else
            return 1;
    }

    public static int DeleteGroup(Groups group) throws ClassNotFoundException, java.sql.SQLException, JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement dostmt = conn.createStatement();
        int rs_do = dostmt.executeUpdate("UPDATE RELATIONS SET GROUPID=0 WHERE GROUPID="+
                group.getGroupid());
        Statement stmt = conn.createStatement();//创建statement
        int rs = stmt.executeUpdate("DELETE FROM GROUPS  WHERE GROUPID=" +
                group.getGroupid());

        stmt.close();
        conn.close();
        if( 0 == rs){
            return -2;
        }
        else if( 0== rs_do)
            return -1;
        else
            return 1;
    }

    public static int ADDGroup(Groups group) throws ClassNotFoundException, java.sql.SQLException, JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement dostmt = conn.createStatement();
        int rs = dostmt.executeUpdate("INSERT INTO GROUPS(GROUPID, ACCOUNTID, NAME) VALUES (GROUPORDER.nextval,'" +
                group.getAccountid()+"','"+
                group.getName()+"')");

        dostmt.close();
        conn.close();
        if( 0 == rs){
            return -1;
        }
        else
            return 1;
    }

    public static int EditPassword(Account olduser,Account newuser) throws ClassNotFoundException, java.sql.SQLException, JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT PASSWORD FROM ACCOUNT WHERE ACCOUNTID=" +
                newuser.getAccountid());
        if (!rs.next()) {
            rs.close();
            stmt.close();
            conn.close();
            return -3;//密码错误
        }
        if ((olduser.getPassword()).equals(rs.getString("PASSWORD"))) {
            Statement dostmt = conn.createStatement();
            int rs_do = dostmt.executeUpdate("UPDATE ACCOUNT SET PASSWORD='" +
                    newuser.getPassword() +
                    "' WHERE ACCOUNTID=" +
                    newuser.getAccountid());
            if (0 == rs_do) {
                rs.close();
                dostmt.close();
                stmt.close();
                conn.close();
                return -2;
            } else {
                rs.close();
                dostmt.close();
                stmt.close();
                conn.close();
                return 1;
            }
        } else {
            rs.close();
            stmt.close();
            conn.close();
            return -1;//密码错误
        }
    }
//aln
    public static String readFriends(Account user) throws ClassNotFoundException, java.sql.SQLException,JSONException {
        Class.forName("oracle.jdbc.OracleDriver");//加载驱动
        Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        Statement stmt = conn.createStatement();//创建statement
        ResultSet rs = stmt.executeQuery("Select GROUPID,NAME From GROUPS WHERE ACCOUNTID=" + user.getAccountid() + "Order by GROUPID");

        String jsonString = "{";
        while (rs.next()) {
            Statement stmt_info = conn.createStatement();//创建statement
            ResultSet rs_info = stmt_info.executeQuery("Select * From INFO WHERE INFO.ACCOUNTID in " +
                    "( select relations.FRIENDID from relations where relations.GROUPID = '"
                    + rs.getString("groupid") + "'and RELATIONS.STATE = '1') Order by INFO.ACCOUNTID");
            jsonString += "\"" + rs.getString("name")+ "\":" + resultSetToJson(rs_info) + ",";
        }
        jsonString = jsonString.substring(0, jsonString.length() - 1);
        jsonString += "}";
        rs.close();
        stmt.close();
        conn.close();
        return jsonString;
    }
    public static boolean deleteFriend(String accountid, String friendid) throws ClassNotFoundException, SQLException {

        String deleteString = "Delete from RELATIONS where (ACCOUNTID = '"+accountid+"' and FRIENDID = '"+friendid
                +"') or (ACCOUNTID = '"+friendid+"' and FRIENDID = '"+accountid+"')";
        return update(deleteString);
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
//201706220138
    public static boolean moveFriend(String accountid, String friendid, String groupid) throws ClassNotFoundException, SQLException {

        String updateString = "update relations set groupid = '"+ groupid
                +"'where ACCOUNTID = '"+accountid+"' and FRIENDID = '"+ friendid + "'";
        return update(updateString);
    }
    public static String updateAccountInfo(String accountid,String nickname ,String gender,String birthday) throws ClassNotFoundException, SQLException ,JSONException{

        String updateString = "update info set nickname = '"+ nickname +"',gender = '" + gender
                + "',birthday = to_date('" + birthday + "','yyyy-mm-dd hh24:mi:ss') where ACCOUNTID = '" + accountid + "'";
        update(updateString);
        return getAccountInfo(accountid);
    }
//201706220138
}