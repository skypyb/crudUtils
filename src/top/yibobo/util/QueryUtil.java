package top.yibobo.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import top.yibobo.util.handler.ResultSetHandler;
import top.yibobo.util.handler.TypeHandler;
import top.yibobo.util.handler.TypeHandlerFactory;

public class QueryUtil {
	private static QueryUtil INSTANCE = null;
	private static TypeHandler th = TypeHandlerFactory.creatTypeHandler(ResultSet.class);
	
	private QueryUtil(){}
	
	public static QueryUtil getInstance(){
		if(INSTANCE == null){
			synchronized (QueryUtil.class) {
				if(INSTANCE == null){
					INSTANCE = new QueryUtil();
				}
			}
		}
		
		return INSTANCE;
	}
	
	/**
	 * 此方法使用反射将type中的成员变量与方法进行动态获取
	 * 并且通过反射将成员变量的类型动态提取出来从而实现ResultSet中getXXX的动态获取
	 * 方法运行结果为将所查询数据表中的每一行保存为对象，并在查询完毕后将包含所有对象的集合返回
	 * @author pyb
	 * @param connection
	 * @param sql
	 * @param type
	 * @return sql语句查询出来的对象结果集合
	 */
	public <T> List<T> query(Connection connection,String sql,Class<T> type){
		
		List<T> list = new ArrayList<T>();
		Field[] field = type.getDeclaredFields();//获得class中所有的成员属性
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()){//遍历结果集
				//通过传入的class获取此class所代表的类的实例
				//Constructor c = type.getConstructor(null);
				//c.newInstance等同于new出来的实例
				T t = (T) th.typeHandler(type, rs);
				list.add(t);
			}
			
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}   catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(null, ps, rs);
		}
		return list;
	}
	
	/**
	 * 此方法实现将sql语句查询的一行数据保存为相应的对象并返回
	 * @param connection
	 * @param sql
	 * @param type
	 * @param objs
	 * @return 与数据表中的一行数据映射的对象
	 */
	public <T> T query(Connection connection,String sql,Class<T> type,Object[] objs){
		
		Field[] fields = type.getDeclaredFields();
		T t = null;
		
			PreparedStatement pr = null;
			ResultSet rs = null;
			try {
				//创建完整的PrepareStatement，给？赋值
				pr = connection.prepareStatement(sql);
				for(int i = 0;i<objs.length;i++){
					pr.setObject(i+1, objs[i]);
				}

				rs = pr.executeQuery();
				if(rs.next()){//调用ResultSetHandler方法获得一个对象
					t = (T)th.typeHandler(type, rs);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}  catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally {
				JDBCUtils.close(null, pr, rs);
			}
		return t;
	}
	
	/**
	 * 此方法实现增删改的功能
	 * @param connection
	 * @param sql
	 * @param objs
	 * @return
	 */
	public int update(Connection connection,String sql,Object[] objs){
		int row = 0;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			for(int i = 0 ; i < objs.length ; i++){
				ps.setObject(i+1, objs[i]);
			}
			row = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(null, ps, null);
		}
		return row;
	}
	
	
	
}
