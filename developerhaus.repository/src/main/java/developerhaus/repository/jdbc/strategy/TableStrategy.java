package developerhaus.repository.jdbc.strategy;

public interface TableStrategy {
	
	String getTableName();
	String[] getAllColumn();
	
//	String getAliasName();

//	TableStrategy setTableName(String tableName);
	TableStrategy setAllColumn(String... allColumn);
	
	
//	TableStrategy setAliasName(String aliasName);
}
