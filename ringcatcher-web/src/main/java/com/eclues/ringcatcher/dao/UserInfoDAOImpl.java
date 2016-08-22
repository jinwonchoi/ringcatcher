package com.eclues.ringcatcher.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eclues.ringcatcher.ctrl.RingMsgController;
import com.eclues.ringcatcher.etc.Constant;
import com.eclues.ringcatcher.obj.MsgInfo;
import com.eclues.ringcatcher.obj.UserInfo;

public class UserInfoDAOImpl implements UserInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserInfoDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public UserInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(UserInfo userInfo) throws Exception {
		if (!userInfo.getUserNum().equals("")) {
			//insert
			String sql = "INSERT INTO user_info(user_num,user_id,user_name,user_email,recom_id,update_date,create_date)"
					+ " VALUES (?, ?, ?, ?, ?,now(),now())";
			logger.debug(String.format("insert:[%s] sql[%s]", userInfo, sql));
			jdbcTemplate.update(sql, userInfo.getUserNum(), userInfo.getUserId()
					, userInfo.getUserName()
					, userInfo.getUserEmail()
					, userInfo.getRecomId());	
		} else {
			throw new Exception(String.format("No key value [%s]", userInfo.getUserNum()));
		}
	}
	
	@Override
	public void update(UserInfo userInfo) throws Exception {
		if (!userInfo.getUserNum().equals("")) {
			//update
			String sql = "UPDATE user_info SET user_id = ? "
					+ " , user_name = ?"
					+ " , user_email = ?"
					+ " , recom_id = ?"
					+ " , update_date = now()"
					+ "where user_num = ?";
			logger.debug(String.format("update:[%s] sql[%s]", userInfo, sql));
			jdbcTemplate.update(sql, userInfo.getUserId(), userInfo.getUserName(), userInfo.getUserEmail()
					, userInfo.getRecomId(), userInfo.getUserNum());
		} else {
			throw new Exception(String.format("No key value [%s]", userInfo.getUserNum()));
		}
	}

	@Override
	public void delete(String id) {
		String sql = "Delete from user_info where user_num = ?";
		logger.debug(String.format("delete:[%s] sql[%s]", id, sql));
		jdbcTemplate.update(sql, id);
	}

	@Override
	public UserInfo get(String userNum) {
		if (Constant.DEFAULT_USER_NUM.get().equals(userNum)) {
			UserInfo userInfo = new UserInfo();
			userInfo.setUserNum(userNum);
			userInfo.setUserName(Constant.STR_DEFAULT_USER_NUM.get());
			return userInfo;
		}
		String sql = "SELECT user_num,user_id,user_name,user_email,recom_id "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM user_info where user_num ='"+userNum+"'";
		logger.debug("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<UserInfo>(){
			@Override
			public UserInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UserInfo userInfo = new UserInfo();
					userInfo.setUserNum(rs.getString("user_num"));
					userInfo.setUserId(rs.getString("user_id"));
					userInfo.setUserName(rs.getString("user_name"));
					userInfo.setUserEmail(rs.getString("user_email"));
					userInfo.setRecomId(rs.getString("recom_id"));
					userInfo.setUpdateDate(rs.getString("update_date"));
					userInfo.setCreateDate(rs.getString("create_date"));
					return userInfo;
				}
				return null;
			}
			
		});
	}
	
	@Override
	public List<UserInfo> getList(List<String> userList) {
		String userNumStr = "(";
		int i = 0;
		for (String user : userList) {
			if (i == 0) 
				userNumStr += "'"+user+"'";
			else
				userNumStr += ",'"+user+"'";
			i++;						
		}
		userNumStr +=")"; 
		String sql = "SELECT user_num,user_id,user_name,user_email,recom_id "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM user_info where user_num in "+userNumStr+"";
		logger.debug("getList: sql:"+sql);
		
		List<UserInfo> listUserInfo = jdbcTemplate.query(sql, new RowMapper<UserInfo>(){
			@Override
			public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserNum(rs.getString("user_num"));
				userInfo.setUserId(rs.getString("user_id"));
				userInfo.setUserName(rs.getString("user_name"));
				userInfo.setUserEmail(rs.getString("user_email"));
				userInfo.setRecomId(rs.getString("recom_id"));
				userInfo.setUpdateDate(rs.getString("update_date"));
				userInfo.setCreateDate(rs.getString("create_date"));
				return userInfo;
			}
		});
		return listUserInfo;

	}
	
	public List<UserInfo> getListBy(Map<String,String> params) throws Exception {
		String sql = "SELECT user_num,user_id,user_name,user_email,recom_id "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM user_info where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userNum")) 
				sql += " and user_num like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("userName"))
				sql += " and user_name like '%"+entry.getValue()+"%'";
			
		}
		logger.debug("getList: sql:"+sql);
		
		List<UserInfo> listUserInfo = jdbcTemplate.query(sql, new RowMapper<UserInfo>(){
			@Override
			public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserNum(rs.getString("user_num"));
				userInfo.setUserId(rs.getString("user_id"));
				userInfo.setUserName(rs.getString("user_name"));
				userInfo.setUserEmail(rs.getString("user_email"));
				userInfo.setRecomId(rs.getString("recom_id"));
				userInfo.setUpdateDate(rs.getString("update_date"));
				userInfo.setCreateDate(rs.getString("create_date"));
				return userInfo;
			}
		});
		return listUserInfo;

	}
}
