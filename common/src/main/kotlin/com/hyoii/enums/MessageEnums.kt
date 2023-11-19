package com.hyoii.enums

enum class MessageEnums(val code: String, val message: String) {
    // Default
    SUCCESS("200", "정상 처리되었습니다."),
    FAIL("400", "잘못된 요청입니다."),
    ERROR("500", "오류가 발생했습니다."),

    // Common
    INVALID_PARAMETER("4000", "파라미터 유효성검증에 실패했습니다."),

    // 5000번대 Member
    MEMBER_EXIST("5000", "이미 등록된 회원입니다."),
    MEMBER_SIGNUP_ERROR("5001", "회원가입 중 오류가 발생했습니다."),
    MEMBER_EMPTY("5002", "조회된 회원이 없습니다."),
    MEMBER_MODIFY_FAIL("5003", "회원정보 수정 중 오류가 발생했습니다."),

    // 6000번대 인증관련
    LOGIN_FAIL("6000", "로그인에 실패했습니다."),
    LOGIN_INCORRECT("6001", "아이디 비밀번호를 확인해주세요.")
}
