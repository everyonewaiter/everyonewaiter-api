package com.everyonewaiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TypeExcludeFilters(PersistenceTestConfiguration.ExcludeRedisConfiguration.class)
@Import({TestContainerSupport.class, PersistenceTestConfiguration.class})
public @interface IntegrationTest {

}
