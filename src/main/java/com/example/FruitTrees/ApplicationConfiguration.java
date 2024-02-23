package com.example.FruitTrees;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public CacheManager cacheManager() {
        return
       new ConcurrentMapCacheManager("openMeteoDataCache","processedWeatherCache" );
    }
}
