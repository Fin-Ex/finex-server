package ru.finex.core.repository;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.name.Names;
import com.mycila.guice.ext.closeable.CloseableModule;
import com.mycila.guice.ext.jsr250.Jsr250Module;
import com.mycila.jmx.JmxModule;
import lombok.Data;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.finex.core.GlobalContext;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.inject.module.DbModule;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author m0nster.mind
 */
public class RepositoryProxyTest {

    @Test
    public void testInject() {
        GlobalContext.reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forJavaClassPath())
            .addScanners(
                Scanners.SubTypes,
                Scanners.TypesAnnotated,
                Scanners.MethodsAnnotated,
                Scanners.ConstructorsAnnotated,
                Scanners.FieldsAnnotated,
                Scanners.Resources
            )
        );

        List<Module> modules = new ArrayList<>();
        modules.add(new CloseableModule());
        modules.add(new Jsr250Module());
        modules.add(new JmxModule());
        modules.add(new TestDbModule());

        Injector injector = Guice.createInjector(Stage.PRODUCTION, modules);
        TestRepository repository = injector.getInstance(TestRepository.class);
        Assertions.assertNotNull(repository);
    }

    @Test
    public void testQuery() {
        String queryJpql = "SELECT FROM TestEntity WHERE persistenceId = :id";
        DefaultCrudRepository<TestEntity, Long> repository = mock(DefaultCrudRepository.class);

        var query = mock(org.hibernate.query.Query.class);
        Session session = mock(Session.class);
        when(session.createQuery(eq(queryJpql)))
            .thenReturn(query);

        TransactionalContext ctx = mock(TransactionalContext.class);
        when(ctx.session()).thenReturn(session);
        TransactionalContext.set(ctx);

        TestRepository proxy = (TestRepository) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[]{ TestRepository.class },
            new RepositoryProxy<>(repository, TestRepository.class)
        );

        Long id = 11L;
        proxy.testOp(id);
        verify(session).createQuery(eq(queryJpql));
        verify(query).setParameter(eq("id"), eq(id));
        verify(query).getSingleResult();
    }

    @Test
    public void testAsyncQuery() {
        String queryJpql = "SELECT FROM TestEntity WHERE persistenceId = :id";
        DefaultCrudRepository<TestEntity, Long> repository = mock(DefaultCrudRepository.class);

        var query = mock(org.hibernate.query.Query.class);
        Session session = mock(Session.class);
        when(session.createQuery(eq(queryJpql)))
            .thenReturn(query);

        TransactionalContext ctx = mock(TransactionalContext.class);
        when(ctx.session()).thenReturn(session);
        TransactionalContext.set(ctx);

        TestRepository proxy = (TestRepository) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[]{ TestRepository.class },
            new RepositoryProxy<>(repository, TestRepository.class)
        );

        Long id = 11L;
        proxy.testOp(id);
        verify(repository).asyncOperation(any());
    }

    @Data
    @Entity
    @Table(name = "test_table")
    public static class TestEntity implements ru.finex.core.model.entity.Entity<Long> {
        @Id
        @Column(name = "id")
        private Long persistenceId;
    }

    public interface TestRepository extends CrudRepository<TestEntity, Long> {
        @Query("SELECT FROM TestEntity WHERE persistenceId = :id")
        TestEntity testOp(Long id);

        @Query("SELECT FROM TestEntity WHERE persistenceId = :id")
        RepositoryFuture<TestEntity> testOpAsync(Long id);
    }

    public static class TestDbModule extends DbModule {

        @Override
        protected void configure() {
            bind(ExecutorService.class).annotatedWith(Names.named("RepositoryExecutor")).toInstance(Mockito.mock(ExecutorService.class));
            bindCrudRepositories();
        }

    }

}
