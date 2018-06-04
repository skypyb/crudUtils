package top.yibobo.util.handler;

import java.sql.ResultSet;

public class TypeHandlerFactory {
	/**
	 * 工厂模式，通过传入的class判断实例化哪个TypeHandler的实现类
	 * 实现对TypeHandler实现类和调用者之间的解耦
	 * @param cls 判定对象
	 * @return TypeHandler
	 */
	public static TypeHandler creatTypeHandler(Class cls) {
		
		
		
		if(cls.isPrimitive() || cls == String.class ||Number.class.isAssignableFrom(cls)) {
			return new PrimitiveHandler();//基本数据类型及其包装类以及String类处理器
		}else if(cls.getSimpleName().equals("Date")) {
			return new DateHandler();//java.sql.Date与java.util.Date处理器,字符串转Date
			
		}else if(ResultSet.class.isAssignableFrom(cls)) {
			return new ResultSetHandler();//ResultSet结果集处理器,一行结果封装成相应对象
		}
		return null;
	}
}
