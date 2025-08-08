package com.everyonewaiter.global.annotation;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = SUPPORTS)
public @interface ReadOnlyTransactional {

}
