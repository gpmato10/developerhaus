package developerhaus.repository.jdbc.strategy;

public class DefaultTableStrategy implements TableStrategy{
	
	private String tableName;
	private String aliasName;
	private String[] allColumn;
	
	public DefaultTableStrategy(String tableName, String aliasName){
		this.tableName = tableName;
		this.aliasName = aliasName;
	}

	@Override
	public String getTableName() {
		return this.tableName;
	}

	@Override
	public String getAliasName() {
		return this.aliasName;
	}
	
	@Override
	public String[] getAllColumn() {
		return this.allColumn;
	}

	@Override
	public TableStrategy setAllColumn(String... allColumn) {
		this.allColumn = allColumn;
		return this;
	}

}