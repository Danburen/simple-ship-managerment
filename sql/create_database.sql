create database if not exists ship_manager;
use ship_manager;

set foreign_key_checks = 0;
-- drop table if exists user;
drop table if exists ship;
drop table if exists port;
drop table if exists voyage;
drop table if exists maintenance;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    nickname VARCHAR(30) NULL,
    password VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)
);

-- 船舶表
CREATE TABLE ship (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    status ENUM('正常', '维修中', '停运', '已退役') DEFAULT '正常',
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 港口表
CREATE TABLE port (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 航行记录表
CREATE TABLE voyage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ship_id BIGINT NOT NULL,
    departure_port_id BIGINT NOT NULL,
    arrival_port_id BIGINT NOT NULL,
    departure_time TIMESTAMP(3),
    arrival_time TIMESTAMP(3),
    status ENUM('计划中', '航行中', '已到达', '已完成') DEFAULT '计划中',
    log TEXT,
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (ship_id) REFERENCES ship(id),
    FOREIGN KEY (departure_port_id) REFERENCES port(id),
    FOREIGN KEY (arrival_port_id) REFERENCES port(id)
);

-- 维护记录表
CREATE TABLE maintenance (
      id BIGINT PRIMARY KEY AUTO_INCREMENT,
      ship_id BIGINT NOT NULL,
      maintenance_date TIMESTAMP(3),
      description TEXT,
      status ENUM('待维护', '维护中', '已完成') DEFAULT '待维护',
      cost DECIMAL(10,2),
      created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
      updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
      is_deleted BOOLEAN DEFAULT FALSE,
      FOREIGN KEY (ship_id) REFERENCES ship(id)
);
