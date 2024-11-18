# 테스트 DB
-- 유저 insert 쿼리
# insert into users(age, created_at, updated_at, user_id, email, location, login_id, password, role, username, gender, login_type)
# values (null, NOW(), NOW(), 1, 'test1@naver.com', null, 'login1', '1111', 'ROLE_USER', 'testA', 'MALE', 'OUR');
#
# -- 진단기록 insert 쿼리
# insert into diagnosis(confidence_score, diagnosis_type, created_at, diagnosis_id, updated_at, user_id, result)
# VALUES (90, 'CANCER', NOW(), 1, NOW(), 1, '흑색종');