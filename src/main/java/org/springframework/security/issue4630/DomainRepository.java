package org.springframework.security.issue4630;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('STAFF')")
public interface DomainRepository extends CrudRepository<Domain, Long> {

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	void deleteAll();

	@PreAuthorize("hasRole('STAFF') && #domain.ownerName.equals(authorization.user)")
	@Override
	void delete(Domain domain);

	@PreAuthorize("hasRole('STAFF') && #domain.ownerName.equals(authorization.user)")
	@Override
	void delete(Long id);

}
