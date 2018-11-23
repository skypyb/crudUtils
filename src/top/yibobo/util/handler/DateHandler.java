package com.rocket.util.beanUtils.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHandler implements TypeHandler {
    protected DateHandler() {
    }


    /**
     * 此方法实现将传入的value转换成指定的Date值
     */
    @Override
    public Object typeHandler(Class type, Object value) {
        //判断是否赋值兼容，直接兼容的话就没必要往下了
        if (type.isInstance(value)) {
            return value;
        }

        if (type == String.class && Date.class.isAssignableFrom(value.getClass())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(value);
        }

        //如果value是个String类型的，就进行到字符串转日期的操作
        if (String.class.isInstance(value)) {
            String date = value.toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //得到java.util.Date类型和java.sql.Date类型的时间
            java.util.Date date1 = null;
            try {
                date1 = format.parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            java.sql.Date date2 = new java.sql.Date(date1.getTime());

            if (type.getName().equals("java.sql.Date")) {
                return date2;
            }
            if (type.getName().equals("java.util.Date")) {
                return date1;
            }

        }
        return null;

    }

}
