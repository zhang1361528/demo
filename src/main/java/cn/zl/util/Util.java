package cn.zl.util; /**
 * Util.java
 * com.uxuexi.core.utils
 * Copyright (c) 2011, 北京聚智未来科技有限公司版权所有.
*/


import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
/**
 * 常用的工具类的集合
 * <p>
 * 包含最常用工具类，比如判空、判断相等等与具体对象无关的方法
 * 
 * @author   庄君祥  
 * @Date	 2014-4-3 
 */
public final class Util {

	/**
	 * 判断一个对象是否为空。它支持如下对象类型：
	 * <ul>
	 * <li>null : 一定为空
	 * <li>字符串     : ""为空,多个空格也为空
	 * <li>数组
	 * <li>集合
	 * <li>Map
	 * <li>Optional
	 * <li>其他对象 : 一定不为空
	 * </ul>
	 * 
	 * @param obj
	 *            任意对象
	 * @return 是否为空
	 */
	public final static boolean isEmpty(final Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			return "".equals(String.valueOf(obj).trim());
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof Collection<?>) {
			return ((Collection<?>) obj).isEmpty();
		}
		if (obj instanceof Map<?, ?>) {
			return ((Map<?, ?>) obj).isEmpty();
		}
		if (obj instanceof Optional<?>) {
			Optional<?> result = (Optional<?>) obj;
			return !result.isPresent();
		}
		return false;
	}


}
