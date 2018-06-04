package top.yibobo.test;


import java.sql.Connection;
import java.util.List;

import top.yibobo.pojo.Book;
import top.yibobo.util.JDBCUtils;
import top.yibobo.util.QueryTableDAO;

public class Test{
	
	
	public static void main(String[] args) {
		Connection conn = JDBCUtils.getConnection();
		//QueryTableDAO qtd = new QueryTableDAO("books",Book.class,conn);
		
		
		//Book book = new Book(0,"禅道","道玄禅师",50.25);
		
		//System.out.println(qtd.update(book,"12"));
		
		//System.out.println(qtd.delete("12"));
		//按照需求调不同的方法
	}

}
