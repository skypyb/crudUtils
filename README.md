# crudUtils
使用反射和JDBC实现的单表CRUD小框架，和一个简单的数据处理器  
弄完这个之后最直观的感受就是感觉反射这个东西真是好玩并且好用，极强的帮助自己理解了反射  
test 包下有大概使用方式  

  
## change log
  
  
#### v1.0.8(2018-10-19)  
时隔四个月的更新。去除了无用文件。  
编辑器对名字为 isXxxx 的属性进行 getter、setter 生成时会抹去 is，针对这种会出现异常的情况作了处理( BeanUtil.java 文件下)  
在 PrimitiveHandler.java 文件下添加了对于 boolean 的包装类 Boolean 的反射处理
#### v1.0.7(2018-06-04)  
目前上传版本，之前的修改都不记得了，暂时没发现有什么bug  

## Explain
这个小工具大致几个主要功能及其对应的类
* QueryUtil类：基于JDBC和反射的CURD工具，执行sql并返回不同的结果，和apache的DBUtils.jar包部分功能类似

* BeanUtil类：将MAP中的值动态赋值给某对象的对应字段，条件：MAP的key=实体对象的属性名

* QueryTableDAO类：万能DAO方法（单表），实现了对单表的CRUD以及条件查询等，通过调用不同的方法返回不同的结果，需要表字段名和类属性名一致，并且顺序一致

* TypeHandlerFactory类：数据处理，实现了一个Factory模式，传入一个需要转换的Object对象和转换后的数据类型Class，实现类型的转换 
<br> 
<br> 
`目前实现的类型转换`:
     >基本数据类型及其包装类以及String类互相转换  
     >结果集转换为一个对象  
     >java.sql.Date与java.util.Date和String之间的转换  
