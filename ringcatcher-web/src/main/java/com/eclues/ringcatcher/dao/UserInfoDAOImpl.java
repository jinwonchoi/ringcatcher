package com.eclues.ringcatcher.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eclues.ringcatcher.UserInfo;

public class UserInfoDAOImpl implements UserInfoDAO {

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
			jdbcTemplate.update(sql, userInfo.getUserId(), userInfo.getUserName(), userInfo.getUserEmail()
					, userInfo.getRecomId(), userInfo.getUserNum());
		} else {
			throw new Exception(String.format("No key value [%s]", userInfo.getUserNum()));
		}
	}

	@Override
	public void delete(String id) {
		String sql = "Delete from user_info where user_id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public UserInfo get(String userNum) {
		String sql = "SELECT user_num,user_id,user_name,user_email,recom_id "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM user_info where user_num ='"+userNum+"'";
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
	
}