package top.yibobo.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

public class QueryTableDAO {
	private QueryUtil qu = QueryUtil.getInstance();

	private String tableName;
	private Class cls;
	private Connection conn;

	private Field[] fields;// 所有成员属性

	public QueryTableDAO(String tableName, Class cls) {
		this.tableName = tableName;
		this.cls = cls;

		fields = cls.getDeclaredFields();
	}

	/**
	 * 查询数据表中所有参数
	 * @author pyb
	 * @return 一个包含了数据表中映射所有对象的List
	 */
	public List findAll() {
		conn = JDBCUtils.getConnection();

		String sql = "SELECT * FROM " + tableName;
		List<?> list = qu.query(conn, sql, cls);

		JDBCUtils.close(conn, null, null);
		return list;
	}

	/**
	 * 通过给定的key和value值查询出一个集合（条件查询）
	 * @author pyb
	 * @param key
	 * @param value
	 * @return
	 */
	public List findCondition(String key, String value) {
		conn = JDBCUtils.getConnection();

		String sql = "SELECT * FROM " + tableName + " WHERE " + key + "=" + value;
		List<?> list = qu.query(conn, sql, cls);

		JDBCUtils.close(conn, null, null);
		return list;
	}

	/**
	 * 通过key和value实现查询数据表中的单条数据 在sql语句中映射乘 key=value 比如 id=0
	 * @author pyb
	 * @param key
	 * @param value
	 * @return 此数据对象
	 */
	public <T> T findSingle(String key, String value) {
		conn = JDBCUtils.getConnection();
		String sql = "SELECT * FROM " + tableName + " WHERE " + key + "=?";

		T t = (T) qu.query(conn, sql, cls, new Object[] { value });
		JDBCUtils.close(conn, null, null);
		return t;
	}

	/**
	 * 删除单条数据
	 * @author pyb
	 * @param pk 为表主键
	 * @return
	 */
	public boolean delete(String pk) {
		conn = JDBCUtils.getConnection();
		boolean flag = false;

		String sql = "DELETE " + tableName + " WHERE " + fields[0].getName() + "=?";
		int row = qu.update(conn, sql, new Object[] { pk });

		if (row > 0) {
			flag = true;
		}
		JDBCUtils.close(conn, null, null);
		return flag;
	}

	/**
	 * 增加一条数据
	 * 通过传入的对象和序列名字来动态生成sql语句
	 * @param t
	 * @param seq
	 * @return
	 */
	public <T> boolean insert(T t, String seq) {
		conn = JDBCUtils.getConnection();
		boolean flag = false;

		String sql = getInsertSql(fields.length - 1, seq);
		Object[] objs = getFieldObjectArray(t);
		System.out.println(sql);
		int row = qu.update(conn, sql, objs);

		if (row > 0) {
			flag = true;
		}
		JDBCUtils.close(conn, null, null);
		return flag;
	}

	/**
	 * 修改一条数据
	 * 通过传入的对象和序列名字来动态生成sql语句
	 * 将数据库中一列数据修改为传入对象对应的属性
	 * @author pyb
	 * @param t
	 * @param seqValue
	 * @return
	 */
	public <T> boolean update(T t, String seqValue) {
		conn = JDBCUtils.getConnection();

		boolean flag = false;
		String sql = getUpdateSql();
		Object[] objs1 = getFieldObjectArray(t);// 除主键值外的所有属性value值数组
		Object[] objs2 = new Object[fields.length];// 与field长度相等的Obejct数组

		for (int i = 0; i < objs2.length - 1; i++) {
			objs2[i] = objs1[i];
		}

		objs2[objs2.length - 1] = seqValue;// 把主键的value值放进objs2的最后一个位置

		System.out.println(sql);
		int row = qu.update(conn, sql, objs2);

		if (row > 0) {
			flag = true;
		}
		JDBCUtils.close(conn, null, null);
		return flag;
	}

	// 获取修改语句
	private String getUpdateSql() {

		String zhanwei = "";
		String sql = "UPDATE " + tableName + " SET ";

		for (int i = 1; i < fields.length; i++) {
			zhanwei = zhanwei + "," + fields[i].getName() + "=? ";
		}
		zhanwei = zhanwei.substring(1) + "WHERE " + fields[0].getName() + "=?";

		return sql + zhanwei;
	}

	/**
	 * 通过一个数字，自动得到一个INSERT的sql语句
	 * 
	 * @param length
	 * @return
	 */
	private String getInsertSql(int length, String seq) {
		String zhanwei = "";

		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
				zhanwei = zhanwei + "?)";
			} else {
				zhanwei = zhanwei + "?,";
			}

		}
		String sql = "INSERT INTO " + tableName + " VALUES(" + seq + "," + zhanwei;
		return sql;
	}

	/**
	 * 通过传入的实例和获取一个包含此实例所有属性的Object数组 (不保存主键值)
	 * @author pyb
	 * @param t
	 * @param fields
	 * @return
	 */
	private <T> Object[] getFieldObjectArray(T t) {

		Object[] objs = new Object[fields.length - 1];

		for (int i = 1; i < fields.length; i++) {
			Field f = fields[i];

			String fieldName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
			try {

				Method getMethod = t.getClass().getMethod(fieldName, null);

				objs[i - 1] = getMethod.invoke(t, null);

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return objs;
	}

}
