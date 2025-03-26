package com.everyonewaiter

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<EveryonewaiterApiApplication>().with(TestcontainersConfiguration::class).run(*args)
}
