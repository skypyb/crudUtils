package top.yibobo.util;



import java.sql.Statement;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/*
编写JDBC的工具类，获取数据库的连接
采用读取配置文件的方式
读取文件获取连接，执行一次
 */
public class JDBCUtils {
    private static Connection con;
    private static String driverClass;
    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            readConfig();   
            Class.forName(driverClass);
            //使用配置文件的参数连接数据库
           
        } catch (Exception e) {
            throw new RuntimeException("数据库连接失败");
        }
    }

    private static void readConfig() throws Exception {
    	
        InputStream in =
                JDBCUtils.class.getClassLoader().getResourceAsStream("database.properties");
        
        Properties pro = new Properties();
       
        pro.load(in);
        


        driverClass = pro.getProperty("driverClass");
        url = pro.getProperty("url");
        username = pro.getProperty("username");
        password = pro.getProperty("password");


    }
    
    //获取连接的方法
    public static Connection getConnection() {
    	 try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return con;
    }
    
    // 关闭的方法
 	public static void close(Connection c, Statement s, ResultSet r) {

 		try {
 			if (c != null && !c.isClosed()) {
 				c.close();
 			}
 			if (s != null) {
 				s.close();
 			}
 			if (r != null && !r.isClosed()) {
 				r.close();
 			}
 		} catch (SQLException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} 

 	}
 	
 	
}
