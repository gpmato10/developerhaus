package developerhaus.repository.jdbc2;


public class DomainTableMapper {
	
	private Class domainClass;
	private String tableName;
	private String key;

	
	public Class getDomainClass() {
		return domainClass;
	}
	public void setDomainClass(Class domainClass) {
		this.domainClass = domainClass;
	}
	
	public DomainTableMapper(String tableName, String key) {
		this.tableName = tableName;
		this.key = key;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
	
}
