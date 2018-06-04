package top.yibobo.util.handler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;

public class ResultSetHandler implements TypeHandler{
	protected ResultSetHandler(){}
	
	
	/**
	 * 一个工具
	 * 将一个对象和ResultSet结果集传入进来
	 * 可以实现将当前结果集所查询的一行映射到传入的对象中
	 * 
	 * @param t
	 * @param rs
	 * @return 将映射完毕的对象返回
	 */
	private <T> T getQueryObject(T t,ResultSet rs){
		Class<ResultSet> rsclass = ResultSet.class;
		
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field f:fields){
			//获取set方法名
			String fieldName = "set"+f.getName().substring(0,1).toUpperCase()
					+f.getName().substring(1);
			//获取ResultSet要使用的get方法名
			String rsFieldName = "get"+f.getType().getSimpleName().substring(0,1).toUpperCase()
					+f.getType().getSimpleName().substring(1);
			try {
				//给对象赋值的set方法
				Method m = t.getClass().getMethod(fieldName, f.getType());
				//rs要用的get方法
				Method m2 = rsclass.getMethod(rsFieldName, String.class);
				Object obj = m2.invoke(rs, f.getName());
				
				m.invoke(t, obj);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return t;
	}
	
	
	/**
	 * 类型处理器的实现类，将传入的value结果集进行处理，以class的类型对象返回
	 * @param type class类型对象
	 * @param value 结果集
	 * @return
	 */
	@Override
	public Object typeHandler(Class type, Object value) {
		Object obj = null;
		try {
			obj = getQueryObject(type.getConstructor(null).newInstance(null), (ResultSet)value);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return obj;
	}



}
