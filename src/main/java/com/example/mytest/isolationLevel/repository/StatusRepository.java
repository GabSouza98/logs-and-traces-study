package com.example.mytest.isolationLevel.repository;

import com.example.mytest.isolationLevel.domain.StatusDomain;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@UseClasspathSqlLocator
public interface StatusRepository {

    @SqlUpdate
    void update(@BindBean StatusDomain statusDomain);

    @SqlUpdate
    @GetGeneratedKeys("id")
    Long save(@BindBean StatusDomain statusDomain);

    @SqlUpdate
    void deleteByUuid(@Bind("uuid") String uuid);

    @SqlQuery
    @RegisterBeanMapper(StatusDomain.class)
    StatusDomain findByUuid(@Bind("uuid") String uuid);

    @SqlQuery
    @RegisterBeanMapper(StatusDomain.class)
    List<StatusDomain> findAllByUuid(@Bind("uuid") String uuid);

}
