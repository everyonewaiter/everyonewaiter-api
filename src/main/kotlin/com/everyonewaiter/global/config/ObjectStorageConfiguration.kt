package com.everyonewaiter.global.config

import com.oracle.bmc.ConfigFileReader
import com.oracle.bmc.Region
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import com.oracle.bmc.http.client.StandardClientProperties
import com.oracle.bmc.objectstorage.ObjectStorageClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectStorageConfiguration {
    @Bean
    fun objectStorageClient(): ObjectStorageClient {
        val configFile = ConfigFileReader.parseDefault()
        val provider = ConfigFileAuthenticationDetailsProvider(configFile)
        return ObjectStorageClient
            .builder()
            .region(Region.AP_SEOUL_1)
            .clientConfigurator { it.property(StandardClientProperties.BUFFER_REQUEST, false) }
            .build(provider)
    }
}
