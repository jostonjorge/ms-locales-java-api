package br.com.joston.mslocales.v1.utils.cache;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Cache{
     <T> T build(String key, Function<CacheObject<T>,CacheObject<T>> builder);

     interface CacheObject<T>{
        CacheObject<T> dataSupplier(Supplier<T> supplier);
        CacheObject<T> duration(Duration duration);
        CacheObject<T> emptyCondition(Predicate<T> predicate);
        T getData();
    }
}
