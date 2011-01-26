package developerhaus.repository.jdbc.strategy;

public class DefaultTableStrategy implements TableStrategy{
	
	private String tableName;
	private String[] allColumn;
	
	public DefaultTableStrategy(String tableName){
		this.tableName = tableName;
	}

	@Override
	public String getTableName() {
		return this.tableName;
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
