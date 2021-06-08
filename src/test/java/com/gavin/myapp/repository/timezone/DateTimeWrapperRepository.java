package com.gavin.myapp.repository.timezone;

import com.diboot.core.mapper.BaseCrudMapper;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link DateTimeWrapper} entity.
 */
@Repository
public interface DateTimeWrapperRepository extends BaseCrudMapper<DateTimeWrapper> {}
