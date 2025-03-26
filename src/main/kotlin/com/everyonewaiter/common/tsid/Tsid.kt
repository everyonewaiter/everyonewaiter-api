package com.everyonewaiter.common.tsid

import com.github.f4b6a3.tsid.TsidCreator

/**
 * Tsid (Timestamp-based Unique ID) 생성을 도와주는 유틸리티 클래스입니다.
 */
class Tsid {
    companion object {
        /**
         * 숫자 Tsid를 생성합니다.
         *
         * @return 생성된 Tsid
         */
        fun nextLong(): Long = TsidCreator.getTsid().toLong()

        /**
         * 문자열 Tsid를 생성합니다.
         *
         * @return 생성된 Tsid
         */
        fun nextString(): String = TsidCreator.getTsid().toString()
    }
}
