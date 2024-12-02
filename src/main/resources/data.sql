# 테스트 DB
-- 유저 insert 쿼리
-- insert into users(age, created_at, updated_at, user_id, email, address, login_id, password, role, username, gender, login_type)
-- values (10, NOW(), NOW(), 1, 'test1@naver.com', '서울시 강서구', 'login1', '1111', 'ROLE_USER', 'testA', 'MALE', 'OUR');

-- 진단기록 insert 쿼리
-- insert into diagnosis(confidence_score, diagnosis_type, created_at, diagnosis_id, updated_at, user_id, result)
-- VALUES ("90%", 'CANCER', NOW(), 1, NOW(), 1, '흑색종');

-- 게시글 insert 쿼리
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 1, NOW(), 1, 'postA의 게시글입니다.', 'postA', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 2, NOW(), 1, 'postB의 게시글입니다.', 'postB', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 3, NOW(), 1, 'postC의 게시글입니다.', 'postC', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 4, NOW(), 1, 'postD의 게시글입니다.', 'postD', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 5, NOW(), 1, 'postE의 게시글입니다.', 'postE', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 6, NOW(), 1, 'postF의 게시글입니다.', 'postF', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 7, NOW(), 1, 'postG의 게시글입니다.', 'postG', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 8, NOW(), 1, 'postH의 게시글입니다.', 'postH', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 9, NOW(), 1, 'postI의 게시글입니다.', 'postI', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 10, NOW(), 1, 'postJ의 게시글입니다.', 'postJ', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 11, NOW(), 1, 'postK의 게시글입니다.', 'postK', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 12, NOW(), 1, 'postL의 게시글입니다.', 'postL', 'QnA');
-- insert into post(created_at, post_id, updated_at, user_id, content, title, post_type) values (NOW(), 13, NOW(), 1, 'postN의 게시글입니다.', 'postN', 'QnA');

-- 진단정보 insert 쿼리
