package com.everyonewaiter;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@TypeExcludeFilters(PersistenceTestConfiguration.ExcludeRedisConfiguration.class)
@Import({
    TestContainerSupport.class,
    PersistenceTestConfiguration.class,
    ExternalSystemTestConfiguration.class
})
@ExtendWith(DatabaseCleanUpExtension.class)
public abstract class IntegrationTest {

}
