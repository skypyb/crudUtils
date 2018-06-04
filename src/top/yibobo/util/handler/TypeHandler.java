package top.yibobo.util.handler;
/**
 * 类型处理器
 * 通过指定的class实现类型的转换
 * @author pyb
 *
 */
public interface TypeHandler {
	public Object typeHandler(Class type, Object value); 
}
