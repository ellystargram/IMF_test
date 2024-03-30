import com.mysql.cj.MysqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class test {
    public static void main(String[] args) throws Exception{
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.103.34/problem_management?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyTrieval=true", "client_identification", "1324");
        Statement stmt = con.createStatement();
        var a = stmt.executeQuery("select * from problems");
        while(a.next()){
            System.out.println(a.getString(2));
        }
        con.close();
    }
}
