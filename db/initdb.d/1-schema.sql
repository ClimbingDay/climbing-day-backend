CREATE TABLE IF NOT EXISTS member
(
    member_id               BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,             -- 회원 id
    email                   VARCHAR(30)     NOT NULL,                               -- 회원 이메일(e.g., 접속 아이디)
    password                VARCHAR(80)     NOT NULL,                               -- 회원 비밀번호
    name                    VARCHAR(20)     NOT NULL,                               -- 회원 이름
    birth_date              DATE            NOT NULL,                               -- 회원 생년월일
    phone_number            VARCHAR(20)     NOT NULL,                               -- 회원 핸드폰 번호
    status                  VARCHAR(20)     NOT NULL,                               -- 회원 상태(e.g., 휴면, 활성화 등)
    roles                   VARCHAR(20)     NOT NULL,                               -- 회원 권한(e.g., 일반, 관리자 등)
    created_date            DATETIME,
    created_by              VARCHAR(20),
    updated_date            DATETIME,
    updated_by              VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS terms
(
    terms_id                BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,             -- 약관 id
    type                    VARCHAR(255)    NOT NULL,                               -- 약관 종류(e.g., 개인 정보 수집/이영 등)
    version                 VARCHAR(255)    NOT NULL,                               -- 약관 버전
    content                 TEXT            NOT NULL,                               -- 약관 내용
    is_mandatory            BOOLEAN         NOT NULL,                               -- 약관 필수 여부
    created_date            DATETIME
)

CREATE TABLE IF NOT EXISTS member_terms
(
    member_terms_id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,             -- 회원 약관 id
    member_id               BIGINT          NOT NULL,                               -- 회원 id
    terms_id                BIGINT          NOT NULL,                               -- 약관 id
    agree_date              DATETIME        NOT NULL                                -- 약관 동의한 날짜
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (terms_id) REFERENCES terms(terms_id)
)