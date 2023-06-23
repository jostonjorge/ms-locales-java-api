package br.com.joston.mslocales.v1.utils.cache;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemoryCacheTest {

    private static MemoryCache cache;

    @BeforeAll
    static void setup(){
        cache = new MemoryCache();
    }

    @Test
    void shouldExecuteDataSupplierAndReturnCacheData() {
        List<String> expected = providerListOfLetters();
        assertEquals(expected,getDataFromCache());
    }

    @Test
    void shouldGetDataSavedOnCacheWithoutExecuteDataSupplier(){
        List<String> expected = providerListOfLetters();
        assertEquals(expected,getDataFromCache());
    }

    @Test
    void shouldExecuteDataSupplierWhenCacheIsTrueForEmptyCondition(){
        List<String> expected = providerListOfLetters();

        Cache.CacheObject<List<String>> cacheObject = new MemoryCache.CacheObjectImpl<>();
        cacheObject.dataSupplier(this::providerListOfLetters)
                .duration(Duration.ofMinutes(1))
                .emptyCondition(List::isEmpty);

        ReflectionTestUtils.setField(cacheObject,"data",new ArrayList<>());

        assertEquals(expected,cacheObject.getData());
    }

    @Test
    void shouldReturnAnEmptyObjectButNotNullWhenEmptyConditionIsNotPresent(){
        List<String> expected = new ArrayList<>();

        Cache.CacheObject<List<String>> cacheObject = new MemoryCache.CacheObjectImpl<>();
        cacheObject.dataSupplier(this::providerListOfLetters)
                .duration(Duration.ofMinutes(1));

        ReflectionTestUtils.setField(cacheObject,"data",new ArrayList<>());

        assertEquals(expected,cacheObject.getData());
    }

    @Test
    void shouldRetrieveNewDataWhenCacheTimeIsExpired(){
        List<String> expected = providerListOfLetters();

        Cache.CacheObject<List<String>> cacheObject = new MemoryCache.CacheObjectImpl<>();
        cacheObject.dataSupplier(this::providerListOfLetters);

        ReflectionTestUtils.setField(cacheObject,"data",providerListOfLetters());
        ReflectionTestUtils.setField(cacheObject,"expirationTime", LocalDateTime.now().minusMinutes(1));

        assertEquals(expected,cacheObject.getData());
    }

    @Test
    void testForEternalExpirationTime(){
        List<String> expected = providerListOfLetters();

        Cache.CacheObject<List<String>> cacheObject = new MemoryCache.CacheObjectImpl<>();
        cacheObject.dataSupplier(this::providerListOfLetters);
        ReflectionTestUtils.setField(cacheObject,"data",providerListOfLetters());

        assertEquals(expected,cacheObject.getData());
    }

    private List<String> getDataFromCache(){
        return cache.build("letters",builder ->
                builder.dataSupplier(this::providerListOfLetters)
                        .duration(Duration.ofMinutes(1))
                        .emptyCondition(List::isEmpty)
        );
    }

    private List<String> providerListOfLetters(){
        return List.of(
                "A","B","C"
        );
    }
}