package br.com.joston.mslocales.v1.utils.cache;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component("MemoryCacheV1")
public class MemoryCache implements Cache{
    private final Map<String,CacheObject<?>> cache = new HashMap<>();

    @Override
    public <T> T build(String key, Function<CacheObject<T>, CacheObject<T>> builder) {
        if(cache.containsKey(key)){
            return (T) cache.get(key).getData();
        }
        CacheObject<T> cacheObject = builder.apply(new CacheObjectImpl<>());
        cache.put(key,cacheObject);
        return cacheObject.getData();
    }

    static class CacheObjectImpl<T> implements CacheObject<T>{
        private Supplier<T> dataSupplier;
        private LocalDateTime expirationTime;
        private Predicate<T> emptyCondition;

        T data;

        public CacheObject<T> dataSupplier(Supplier<T> supplier) {
            this.dataSupplier = supplier;
            return this;
        }

        @Override
        public CacheObject<T> duration(Duration duration) {
            this.expirationTime = LocalDateTime.now().plus(duration);
            return this;
        }

        public CacheObject<T> emptyCondition(Predicate<T> predicate) {
            this.emptyCondition = predicate;
            return this;
        }

        @Override
        public T getData() {
            if(isEmpty() || isExpired()){
                data = dataSupplier.get();
            }
            return data;
        }

        private boolean isEmpty(){
            if(data == null){
                return true;
            }
            return emptyCondition != null && emptyCondition.test(data);
        }

        private boolean isExpired(){
            return expirationTime != null && expirationTime.isBefore(LocalDateTime.now());
        }
    }
}
