package developerhaus.repository.jdbc;

import java.lang.reflect.Field;

import developerhaus.repository.jdbc.exception.SqlBuilderException;

public class RepositoryUtils {
	
	public static String toColumn(String... columns) {
		
		return toColumn(null, columns);

	}
	
	public static String toColumn(String alias, String... columns) {
		
		if(alias == null){
			alias = "";
		}
		
		StringBuilder sb = new StringBuilder(" ");
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(alias);
			sb.append(".");
			sb.append(columns[i]);
		}
		return sb.toString();
	}
	
//	TODO : Alias로 넘어온 컬럼명을 실제 DB에 정의된 컬럼명으로 매핑(Spring에서 사용가능 하도록 ex/ :name), 컬럼정책에 관한 파라미터가 아마 하나 더 넘어와야 한다.
	public static String toSqlParameterSource(String... columns) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(":");
			sb.append(columns[i]);
		}
		return sb.toString();
	}
	
	public static String addAliasToColumn(String alias, String columnName){
		return alias + "." + columnName;
	}
	
	public static String getColumnName(String domainFieldName){
		
		return domainFieldName.substring(domainFieldName.indexOf(".") + 1);
	}
	
	public static String getColumnName(String domainFiledName, Object target){
		
		Field f;
		String alaisMappedKey = null;
		try {
			f = target.getClass().getField(domainFiledName.toUpperCase());
			alaisMappedKey = (String) f.get(target);
		} catch (Exception e) {
			throw new SqlBuilderException("도메인 속성명의 대문자로 정의된 공통속성명이 target에 정의되어 있어야 합니다. / domainFiledName : " + domainFiledName, e);
		}
		
		return alaisMappedKey;
	}
}