USE enterprise_db;

-- Create t_role table
CREATE TABLE IF NOT EXISTS t_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Insert default roles (using INSERT IGNORE to prevent duplicates on subsequent runs)
INSERT IGNORE INTO t_role (name) VALUES ('ROLE_ADMIN');
INSERT IGNORE INTO t_role (name) VALUES ('ROLE_MANAGER');
INSERT IGNORE INTO t_role (name) VALUES ('ROLE_EMPLOYEE');

-- Create t_user table
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    role_id BIGINT NOT NULL, -- New foreign key column
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES t_role(id)
);

-- Insert example user data (these inserts now use role_id and are idempotent)
INSERT INTO t_user (username, password, real_name, email, phone, role_id)
SELECT * FROM (SELECT 'admin', 'admin123', '系统管理员', 'admin@enterprise.com', '13800138000', (SELECT id FROM t_role WHERE name = 'ROLE_ADMIN')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_user WHERE username = 'admin');

INSERT INTO t_user (username, password, real_name, email, phone, role_id)
SELECT * FROM (SELECT 'zhangsan', 'zhangsan123', '张三', 'zhangsan@enterprise.com', '13800138001', (SELECT id FROM t_role WHERE name = 'ROLE_MANAGER')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_user WHERE username = 'zhangsan');

INSERT INTO t_user (username, password, real_name, email, phone, role_id)
SELECT * FROM (SELECT 'lisi', 'lisi123', '李四', 'lisi@enterprise.com', '13800138002', (SELECT id FROM t_role WHERE name = 'ROLE_EMPLOYEE')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_user WHERE username = 'lisi');


-- Create t_department table
CREATE TABLE IF NOT EXISTS t_department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500)
);

-- Insert example department data (if t_department is empty)
INSERT IGNORE INTO t_department (name, description) VALUES ('研发部', '负责产品研发和技术创新');
INSERT IGNORE INTO t_department (name, description) VALUES ('市场部', '负责市场推广和客户关系管理');
INSERT IGNORE INTO t_department (name, description) VALUES ('人力资源部', '负责员工招聘、培训和绩效管理');
INSERT IGNORE INTO t_department (name, description) VALUES ('财务部', '负责公司财务管理和会计核算');

-- Create t_position table
CREATE TABLE IF NOT EXISTS t_position (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    department_id BIGINT NOT NULL,
    CONSTRAINT fk_position_department FOREIGN KEY (department_id) REFERENCES t_department(id)
);

-- Insert example position data
INSERT INTO t_position (name, description, department_id)
SELECT * FROM (SELECT '软件工程师', '负责软件开发与维护', (SELECT id FROM t_department WHERE name = '研发部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_position WHERE name = '软件工程师');

INSERT INTO t_position (name, description, department_id)
SELECT * FROM (SELECT '项目经理', '负责项目规划与执行', (SELECT id FROM t_department WHERE name = '研发部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_position WHERE name = '项目经理');

INSERT INTO t_position (name, description, department_id)
SELECT * FROM (SELECT '市场专员', '负责市场推广活动', (SELECT id FROM t_department WHERE name = '市场部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_position WHERE name = '市场专员');

INSERT INTO t_position (name, description, department_id)
SELECT * FROM (SELECT '招聘专员', '负责人才招聘与管理', (SELECT id FROM t_department WHERE name = '人力资源部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_position WHERE name = '招聘专员');

INSERT INTO t_position (name, description, department_id)
SELECT * FROM (SELECT '财务会计', '负责公司财务核算', (SELECT id FROM t_department WHERE name = '财务部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_position WHERE name = '财务会计');


-- Create t_employee table
CREATE TABLE IF NOT EXISTS t_employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    hire_date DATE NOT NULL,
    position_id BIGINT NOT NULL, -- Changed to foreign key
    status VARCHAR(20) NOT NULL,
    department_id BIGINT NOT NULL,
    CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES t_department(id),
    CONSTRAINT fk_employee_position FOREIGN KEY (position_id) REFERENCES t_position(id)
);

-- Create t_employee_movement table
CREATE TABLE IF NOT EXISTS t_employee_movement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movement_type VARCHAR(50) NOT NULL,
    movement_date DATE NOT NULL,
    description VARCHAR(500),
    employee_id BIGINT NOT NULL,
    CONSTRAINT fk_movement_employee FOREIGN KEY (employee_id) REFERENCES t_employee(id)
);

-- Create t_notification table
CREATE TABLE IF NOT EXISTS t_notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create t_file_transfer table
CREATE TABLE IF NOT EXISTS t_file_transfer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_hash VARCHAR(255) NOT NULL,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP NULL,
    received_at TIMESTAMP NULL,
    expires_at TIMESTAMP NULL,
    message TEXT,
    CONSTRAINT fk_file_transfer_sender FOREIGN KEY (sender_id) REFERENCES t_user(id),
    CONSTRAINT fk_file_transfer_receiver FOREIGN KEY (receiver_id) REFERENCES t_user(id)
);

-- Insert example employee data (updated to use position_id)
INSERT INTO t_employee (employee_id, name, gender, hire_date, position_id, status, department_id)
SELECT * FROM (SELECT 'E001', '王五', 'MALE', '2023-01-15', (SELECT id FROM t_position WHERE name = '软件工程师'), 'REGULAR', (SELECT id FROM t_department WHERE name = '研发部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_employee WHERE employee_id = 'E001');

INSERT INTO t_employee (employee_id, name, gender, hire_date, position_id, status, department_id)
SELECT * FROM (SELECT 'E002', '赵六', 'FEMALE', '2023-03-20', (SELECT id FROM t_position WHERE name = '市场专员'), 'REGULAR', (SELECT id FROM t_department WHERE name = '市场部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_employee WHERE employee_id = 'E002');

INSERT INTO t_employee (employee_id, name, gender, hire_date, position_id, status, department_id)
SELECT * FROM (SELECT 'E003', '孙七', 'MALE', '2023-05-10', (SELECT id FROM t_position WHERE name = '招聘专员'), 'REGULAR', (SELECT id FROM t_department WHERE name = '人力资源部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_employee WHERE employee_id = 'E003');

INSERT INTO t_employee (employee_id, name, gender, hire_date, position_id, status, department_id)
SELECT * FROM (SELECT 'E004', '李明', 'MALE', '2022-11-01', (SELECT id FROM t_position WHERE name = '项目经理'), 'REGULAR', (SELECT id FROM t_department WHERE name = '研发部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_employee WHERE employee_id = 'E004');

INSERT INTO t_employee (employee_id, name, gender, hire_date, position_id, status, department_id)
SELECT * FROM (SELECT 'E005', '张丽', 'FEMALE', '2023-07-25', (SELECT id FROM t_position WHERE name = '财务会计'), 'ON_PROBATION', (SELECT id FROM t_department WHERE name = '财务部')) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM t_employee WHERE employee_id = 'E005');
