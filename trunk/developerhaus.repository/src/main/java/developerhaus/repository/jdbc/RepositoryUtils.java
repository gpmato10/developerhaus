package developerhaus.repository.jdbc;

public class RepositoryUtils {
	
	public static StringBuilder toColumn(String... columns) {

		StringBuilder sb = new StringBuilder(" ");
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(columns[i]);
		}
		return sb;
	}
}
