package com.everyonewaiter.global.extension

private const val DEFAULT_PAGE_NUM_COUNT = 10

/**
 * 다음 페이지 여부 조회를 위한 최소 데이터 개수 계산 함수입니다.
 * 예를 들어 현재 페이지 번호 1, 조회 사이즈 20, 페이지 번호는 10번까지 있는 경우
 * 다음 페이지 여부를 알기 위해서는 201개의 데이터가 있는지 조회해야 합니다.
 *
 * @param [page] 현재 페이지 번호
 * @param [size] 조회 사이즈
 * @param [pageNumCount] 페이지 번호 개수
 * @return 다음 페이지 여부를 알기 위한 최소 데이터 개수
 */
fun calculatePageLimit(
    page: Long,
    size: Long,
    pageNumCount: Int = DEFAULT_PAGE_NUM_COUNT,
): Long = (((page - 1) / pageNumCount) + 1) * size * pageNumCount + 1

fun calculatePageOffset(
    page: Long,
    size: Long,
): Long = (page - 1) * size
