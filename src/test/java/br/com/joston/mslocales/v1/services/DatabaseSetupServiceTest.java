package br.com.joston.mslocales.v1.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DatabaseSetupServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;
    private AutoCloseable closeable;
    private DatabaseSetupService databaseSetupService;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
        databaseSetupService = new DatabaseSetupService(mongoTemplate);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void populateDatabaseWhenIsEmpty() throws IOException, URISyntaxException {
        when(mongoTemplate.collectionExists(anyString())).thenReturn(false);
        databaseSetupService.bindLocalesOnDatabase();
        verify(mongoTemplate, times(1)).insertAll(any());
    }

    @Test
    void shouldNotPerformAnyInsertionWhenDatabaseAlreadyPopulated(){
        when(mongoTemplate.collectionExists(anyString())).thenReturn(true);
        verify(mongoTemplate,never()).insertAll(any());
        verify(mongoTemplate,never()).insert(any());
    }

    @Test
    void testExceptionWhenReadFileContent() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, URISyntaxException {
        Method method = databaseSetupService.getClass().getDeclaredMethod("getDataFromJsonLocaleFile", File.class);
        method.setAccessible(true);
        assertNull(method.invoke(databaseSetupService,new File(this.getClass().getClassLoader().getResource("invalidJsonCountry.json").toURI())));
    }
}