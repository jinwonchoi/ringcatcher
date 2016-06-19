CREATE DATABASE rc_db;

USE rc_db;
delete FROM user_info;
delete FROM ring_info;
delete FROM ring_history;
delete FROM msg_info;
delete FROM msg_history;



SELECT substr(user_id,1,20), a.* FROM user_info a;



commit;
SELECT a.ring_file_name
  FROM rc_db.ring_info a;

SELECT * FROM ring_info;
SELECT * FROM ring_history;

SELECT *
  FROM user_info
 WHERE create_date = str_to_date('20160329205843', "%Y%m%d%H%i%s");

INSERT INTO user_info(customer_id, customer_name)
     VALUES ('secondman', 'Second Man');

COMMIT;

DROP TABLE user_info;
DROP TABLE ring_info;
DROP TABLE ring_history;
DROP TABLE msg_info;
DROP TABLE msg_history;


/* user_id 획득방법--> 보
CREATE TABLE sequence
(
   id   INT NOT NULL류
);

INSERT INTO sequence
     VALUES (0);

UPDATE sequence
   SET id = LAST_INSERT_ID(id + 1);

SELECT LAST_INSERT_ID();
*/
CREATE TABLE user_info (
  user_num varchar(20) NOT NULL,
  user_id varchar(200) NOT NULL,
  user_name varchar(100) DEFAULT NULL,
  user_email varchar(40) NOT NULL,
  recom_id varchar(20) DEFAULT NULL,
  update_date timestamp NOT NULL,
  create_date timestamp NOT NULL,
  PRIMARY KEY (`user_num`),
  KEY `idx_user_id_01` (`user_num`),
  KEY `idx_user_id_02` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

CREATE TABLE ring_info (
  user_num varchar(20) NOT NULL,
  calling_num varchar(20) NOT NULL,
  calling_name varchar(100) DEFAULT NULL,
  register_date varchar(8) NOT NULL,
  ring_file_name varchar(200) NOT NULL,
  download_cnt int DEFAULT 0,
  update_date timestamp NOT NULL,
  create_date timestamp NOT NULL,
  PRIMARY KEY (user_num,calling_num),
  KEY idx_ring_info_01 (user_num,calling_num),
  KEY idx_ring_info_02 (user_num,download_cnt)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

CREATE TABLE msg_info (
  user_num varchar(20) NOT NULL,
  calling_num varchar(20) NOT NULL,
  calling_name varchar(100) DEFAULT NULL,
  register_date varchar(8) NOT NULL,
  json_msg TEXT,
  download_cnt int DEFAULT 0,
  update_date timestamp NOT NULL,
  create_date timestamp NOT NULL,
  PRIMARY KEY (user_num,calling_num),
  KEY idx_msg_info_01 (user_num,calling_num),
  KEY idx_msg_info_02 (user_num,download_cnt)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;


CREATE TABLE msg_history (
  user_num varchar(20) NOT NULL,
  calling_num varchar(20) NOT NULL,
  calling_name varchar(100) DEFAULT NULL,
  register_date varchar(8) NOT NULL,
  user_id varchar(200) NOT NULL,
  json_msg TEXT,
  update_date timestamp NOT NULL,
  create_date timestamp NOT NULL,
  PRIMARY KEY (user_num,calling_num,create_date),
  KEY idx_msg_history_01 (user_num,calling_num, create_date)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

SELECT unix_timestamp(create_date) FROM user_info;

SELECT DATE_FORMAT(now(), "%Y%m%d%H%i%s");

/*user_id 획득방법*/

SELECT   conv(
            concat(substring(uid, 16, 3),
                   substring(uid, 10, 4),
                   substring(uid, 1, 8)),
            16,
            10) DIV
            10000
       - (141427 * 24 * 60 * 60 * 1000)
          AS current_mills
  FROM (SELECT uuid() uid) AS alias;


INSERT INTO user_info(user_id,
                      user_num,
                      user_email,
                      recom_id,
                      update_date,
                      create_date)
     VALUES ('test-token-id',
             '0244445555',
             'test@gmail.com',
             '',
             now(),
             now());
