package developerhaus.repository.jdbc.strategy;

public interface TableStrategy {
	
	String getTableName();
	String getAliasName();
	String[] getAllColumn();
	

//	TableStrategy setTableName(String tableName);
	TableStrategy setAllColumn(String... allColumn);
	
	
//	TableStrategy setAliasName(String aliasName);
}
