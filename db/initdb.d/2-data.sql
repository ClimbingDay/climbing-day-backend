-- 관리자 계정
INSERT INTO member(member_id, email, password, name, birth_date, phone_number, status, roles, created_date)
VALUES(1, 'admin@gmail.com', '{bcrypt}$2a$10$RV9gh4kqF7DichT0KphMn.zLGPcdeVC8Gx2yCMjQFtdho.EyIRSWK', 'admin', '1111-11-1', '010-000-0000', 'ACTIVE', 'ROLE_ADMIN', CURRENT_DATE)