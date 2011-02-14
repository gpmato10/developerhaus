package developerhaus.repository.jdbc;

public class RepositoryUtils {
	
	public static String toColumn(String... columns) {

		StringBuilder sb = new StringBuilder(" ");
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
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
}