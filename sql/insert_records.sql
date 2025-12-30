use ship_manager;
set foreign_key_checks = 0;
truncate table ship;
truncate table port;
truncate table voyage;
truncate table maintenance;
set foreign_key_checks = 1;
-- 船舶表
/* ========== 1. 船舶表 30 条 ========== */
INSERT INTO ship (name, type, status) VALUES
                                          ('远洋一号','货轮','正常'), ('蓝鲸号','油轮','维修中'), ('海风号','客轮','正常'),
                                          ('长城号','集装箱','正常'), ('珠江号','货轮','正常'), ('松花江号','油轮','正常'),
                                          ('黄河号','客轮','停运'), ('泰山号','集装箱','正常'), ('华山号','货轮','维修中'),
                                          ('昆仑号','油轮','正常'), ('太行号','客轮','正常'), ('武夷山号','集装箱','正常'),
                                          ('黄山号','货轮','正常'), ('庐山号','油轮','维修中'), ('峨眉山号','客轮','正常'),
                                          ('大别山号','集装箱','正常'), ('祁连山号','货轮','正常'), ('长白山号','油轮','正常'),
                                          ('天山号','客轮','停运'), ('阴山号','集装箱','维修中'), ('雪峰号','货轮','正常'),
                                          ('东海号','油轮','正常'), ('南海号','客轮','正常'), ('北海号','集装箱','正常'),
                                          ('黄海号','货轮','维修中'), ('红海号','油轮','正常'), ('地中海号','客轮','正常'),
                                          ('加勒比号','集装箱','正常'), ('波罗的海号','货轮','正常'), ('北冰洋号','油轮','维修中');

/* ========== 2. 港口表 20 条 ========== */
INSERT INTO port (name, location) VALUES
                                      ('上海港','中国上海'), ('青岛港','中国山东青岛'), ('宁波港','中国浙江宁波'),
                                      ('深圳港','中国广东深圳'), ('广州港','中国广东广州'), ('天津港','中国天津'),
                                      ('大连港','中国辽宁大连'), ('厦门港','中国福建厦门'), ('连云港','中国江苏连云港'),
                                      ('营口港','中国辽宁营口'), ('日照港','中国山东日照'), ('烟台港','中国山东烟台'),
                                      ('福州港','中国福建福州'), ('泉州港','中国福建泉州'), ('汕头港','中国广东汕头'),
                                      ('湛江港','中国广东湛江'), ('海口港','中国海南海口'), ('三亚港','中国海南三亚'),
                                      ('北海港','中国广西北海'), ('防城港','中国广西防城港');

/* ========== 3. 航行记录 100 条 ========== */
-- 存储过程：随机生成 100 条航次
DROP PROCEDURE IF EXISTS sp_gen_voyage;
DELIMITER ;;
CREATE PROCEDURE sp_gen_voyage()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE ship_cnt INT DEFAULT 30;
    DECLARE port_cnt INT DEFAULT 20;
    DECLARE dep_port INT;
    DECLARE arr_port INT;
    DECLARE status ENUM('计划中', '航行中', '已到达', '已完成') DEFAULT '计划中';
    DECLARE dep_time DATETIME;
    DECLARE arr_time DATETIME;
    DECLARE ship_id INT;
    DECLARE log_text TEXT;

    WHILE i <= 100 DO
            SET ship_id   = 1 + FLOOR(RAND()*ship_cnt);
            SET dep_port  = 1 + FLOOR(RAND()*port_cnt);
            SET arr_port  = 1 + FLOOR(RAND()*port_cnt);
            IF arr_port = dep_port THEN
                SET arr_port = 1 + (arr_port % port_cnt);
            END IF;

            SET dep_time  = DATE_ADD('2025-06-01', INTERVAL FLOOR(RAND()*360) DAY);
            SET arr_time  = DATE_ADD(dep_time, INTERVAL 1 + FLOOR(RAND()*180) DAY);
            SET status = CASE
                            WHEN arr_time > DATE_ADD(NOW(), INTERVAL 7 DAY) THEN '计划中'
                            WHEN arr_time < DATE_SUB(NOW(), INTERVAL 14 DAY)  THEN '已到达'
                            WHEN arr_time < NOW() THEN '已完成'
                            ELSE '航行中'
                        END;
            SET log_text = ELT(1+FLOOR(RAND()*5),
                               '航行正常，天气良好。',
                               '途中遭遇7级大风，减速航行。',
                               '主机轻微故障，已及时修复。',
                               '避让渔船，绕航20海里。',
                               '准时抵达，无异常。');

            INSERT INTO voyage (ship_id, departure_port_id, arrival_port_id,
                                departure_time, arrival_time,status, log)
            VALUES (ship_id, dep_port, arr_port, dep_time, arr_time, status,log_text);
            SET i = i + 1;
        END WHILE;
END;;
DELIMITER ;
CALL sp_gen_voyage();
DROP PROCEDURE sp_gen_voyage;

/* ========== 4. 维护记录 100 条 ========== */
DROP PROCEDURE IF EXISTS sp_gen_maintenance;
DELIMITER ;;
CREATE PROCEDURE sp_gen_maintenance()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE ship_cnt INT DEFAULT 30;
    DECLARE ship_id INT;
    DECLARE m_time DATETIME;
    DECLARE cost_val DECIMAL(10,2);
    DECLARE status ENUM('待维护', '维护中', '已完成') DEFAULT '待维护';
    DECLARE status_pick INT;
    DECLARE desc_text TEXT;
    DECLARE desc_pool TEXT DEFAULT
        '更换主机润滑油|例行船体防锈检查|更换消防救生设备|清洗燃油过滤器|校准导航雷达|更换冷却水泵|主机大修|螺旋桨检查|尾轴密封更换|甲板除锈补漆';
    DECLARE piece TEXT;

    WHILE i <= 100 DO
            SET ship_id = 1 + FLOOR(RAND()*ship_cnt);
            SET m_time  = DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND()*360) DAY);
            SET cost_val = 1000 + FLOOR(RAND()*20000);   -- 1k~21k 随机
            -- 随机取一条描述
            SET piece = SUBSTRING_INDEX(SUBSTRING_INDEX(desc_pool,'|',1+FLOOR(RAND()*10)),'|',-1);
            SET desc_text = piece;

            SET status_pick = FLOOR(RAND()*3);
            SET status = CASE status_pick
                            WHEN 0 THEN '待维护'
                            WHEN 1 THEN '维护中'
                            ELSE '已完成'
                        END;
            INSERT INTO maintenance (ship_id, maintenance_date, description,status,cost)
            VALUES (ship_id, m_time, desc_text, status,cost_val);
            SET i = i + 1;
        END WHILE;
END;;
DELIMITER ;
CALL sp_gen_maintenance();
DROP PROCEDURE sp_gen_maintenance;

/* ========== 完工后简单校验 ========== */
SELECT 'ship' AS tbl, COUNT(1) AS cnt FROM ship
UNION ALL
SELECT 'port', COUNT(1) FROM port
UNION ALL
SELECT 'voyage', COUNT(1) FROM voyage
UNION ALL
SELECT 'maintenance', COUNT(1) FROM maintenance;