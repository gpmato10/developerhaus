package developerhaus.repository.jdbc2;

import developerhaus.repository.jdbc3.criteria.GenericCriteria;


public interface RepositoryDao<D>  {


	D get(D domain, GenericCriteria criteria) ;

}
