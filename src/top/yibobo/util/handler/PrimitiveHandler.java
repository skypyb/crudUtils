package top.yibobo.util.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PrimitiveHandler implements TypeHandler {
	protected PrimitiveHandler() {}
	/**
	 * 此方法实现将指定的value值对应class的基本数据+String类型转换
	 */
	@Override
	public Object typeHandler(Class type, Object value) {
		
		//判断是否赋值兼容，直接兼容的话就没必要往下了
		if(type.isInstance(value)) {
			return value;
		}
		
		//要转的类型是String的话，直接返回value的toString就行了
		if(type == String.class) {
			return value.toString();
		}
		
		//如果要转的类型是布尔值
		if(type.getName().equals("boolean")) {
			
			if(value.toString().equals("true") 
					|| value.toString().equals("false")) {
				if(value.toString().equals("true")) {
					return true;
				}
			}
			
			return false;
		}
		
		//如果要转的是char就提取value的String值第一个字
		if(type.getName().equals("char")) {
			String val = value.toString();
			return val.toCharArray()[0];
		}
		
		
		//判断value能不能赋值给String,能的话就证明他是一个String，进入String转基本数据类型的操作
		if(String.class.isInstance(value)) {
			String val = value.toString();
			String typeName = type.getSimpleName();
			//如果本身就是包装类，则直接调用。
			if(Number.class.isAssignableFrom(type)) {
				if(type==Integer.class) {
					typeName="int";
				}
				return NumHandler(type, val, typeName);
			}
			
			switch (typeName) {
			
			case "int":
				return NumHandler(Integer.class, val, typeName);
			case "double":
				return NumHandler(Double.class, val, typeName);
			case "long":
				return NumHandler(Long.class, val, typeName);
			case "float":
				return NumHandler(Float.class, val, typeName);
			case "short":
				return NumHandler(Short.class, val, typeName);
			case "byte":
				return NumHandler(Byte.class, val, typeName);
			default:
				break;
			}
		}
		
		
		
		
		
		return null;
	}
	
	
	/**
	 * String类型和六种基本数字类型的转换
	 * @param cls parse方法调用者的class
	 * @param num 要转换成对应数字类的String参数
	 * @param typeName 传入的当前类型的字符串
	 * @return
	 */
	private Object NumHandler(Class<? extends Number> cls,String num,String typeName) {
		Object obj = null;
		try {
			String parse = "parse"+typeName.substring(0, 1).toUpperCase()+typeName.substring(1);
			Method m = cls.getMethod(parse, String.class);
			obj = m.invoke(null, num);
			
			
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}

}
