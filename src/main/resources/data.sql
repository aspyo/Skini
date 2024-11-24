-- 테스트 DB
-- 유저 insert 쿼리
insert into users(age, created_at, updated_at, user_id, email, address, login_id, password, role, username, gender, login_type)
values (10, NOW(), NOW(), 1, 'test1@naver.com', '서울시 강서구', 'login1', '1111', 'ROLE_USER', 'testA', 'MALE', 'OUR');

-- 진단기록 insert 쿼리
-- insert into diagnosis(confidence_score, diagnosis_type, created_at, diagnosis_id, updated_at, user_id, result)
-- VALUES (90, 'CANCER', NOW(), 1, NOW(), 1, '흑색종');

-- 게시글 insert 쿼리
insert into post(created_at, post_id, updated_at, user_id, content, title, post_type)
values (NOW(), 1, NOW(), 1, 'postA의 게시글입니다.', 'postA', 'QnA');
