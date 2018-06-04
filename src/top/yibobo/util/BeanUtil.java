package top.yibobo.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import top.yibobo.util.handler.PrimitiveHandler;
import top.yibobo.util.handler.TypeHandlerFactory;

public class BeanUtil {
	
	/**
	 * 此方法获取传入的对象实例和传入的map
	 * 通过反射将实例的属性名取出以此作为Key获取map中的value值
	 * 并将其动态的进行类型转换后给对象赋值
	 * 需要条件:对象属性值在Map的Key中有对应的值
	 * @author pyb
	 * @param obj
	 * @param Map<String,？>
	 */
	public void populate(Object obj,Map<String,?> map) {
		Class type = obj.getClass();//获取传入对象其类型
		Field[] fields = type.getDeclaredFields();//得到其所有的属性值
		for(Field f:fields) {
			
			//将属性名字作为键值取出map中的对象
			Object value = map.get(f.getName());
			//如果value为空则跳过
			if(value ==null) {
				continue;
			}
			//如果value是个数组的话，就把他第一个单位取出来作为value
			if(value.getClass().isArray()) {
				Object[] array = (Object[]) map.get(f.getName());
				value = array[0];
			}
			//获取该属性的set方法名字
			String fieldName = "set"+f.getName().substring(0, 1).toUpperCase()+f.getName().substring(1);
			
			try {
				//实例化该set方法
				Method m = obj.getClass().getMethod(fieldName, f.getType());
				
				//管他是啥类型，作为调用者只需要用就行了，具体new出哪个处理器让工厂来判断
				Object val = TypeHandlerFactory.creatTypeHandler(f.getType()).typeHandler(f.getType(), value);
				m.invoke(obj, val);//调用set方法给实例赋值
				
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
	}
}
