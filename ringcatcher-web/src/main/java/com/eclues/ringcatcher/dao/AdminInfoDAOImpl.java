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

import com.eclues.ringcatcher.obj.AdminInfo;

public class AdminInfoDAOImpl implements AdminInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(AdminInfoDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public AdminInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	/*
	 * select a.user_id,a.user_id_type,a.user_name,a.user_passwd,a.description,a.update_date,a.create_date
  from admin_info a;
  
insert into admin_info( user_id,user_id_type,user_name,user_passwd,description,update_date,create_date )
values( , , , , , ,  )
update admin_info
set user_id = '', user_id_type = '', user_name = '', user_passwd = '', description = '', update_date = '', create_date = '' 
where <search condition>
(non-Javadoc)
	 * @see com.eclues.ringcatcher.dao.AdminInfoDAO#create(com.eclues.ringcatcher.AdminInfo)
	 */
	@Override
	public void create(AdminInfo adminInfo) throws Exception {
		if (!adminInfo.getUserId().equals("")) {
			//insert
			String sql = "INSERT INTO admin_info(user_id,auth_id, auth_id_type,user_name,user_passwd,description,update_date,create_date)"
					+ " VALUES (?, ?, ?, ?, ?,?, now(),now())";
			jdbcTemplate.update(sql, adminInfo.getUserId(), adminInfo.getAuthId(), adminInfo.getAuthIdType()
					, adminInfo.getUserName()
					, adminInfo.getUserPasswd()
					, adminInfo.getDescription());	
		} else {
			throw new Exception(String.format("No key value [%s]", adminInfo.getUserId()));
		}
	}
	
	@Override
	public void update(AdminInfo adminInfo) throws Exception {
		if (!adminInfo.getUserId().equals("")) {
			//update
			String sql = "UPDATE admin_info SET user_id = ? "
					+ " , auth_id = ?"
					+ " , auth_id_type = ?"
					+ " , user_name = ?"
					+ " , user_passwd = ?"
					+ " , description = ?"
					+ " , update_date = now()"
					+ "where user_id = ?";
			jdbcTemplate.update(sql, adminInfo.getUserId(), adminInfo.getAuthId(), adminInfo.getAuthIdType(), adminInfo.getUserName(), adminInfo.getUserPasswd()
					, adminInfo.getDescription(), adminInfo.getUserId());
		} else {
			throw new Exception(String.format("No key value [%s]", adminInfo.getUserId()));
		}
	}

	@Override
	public void delete(String id) {
		String sql = "Delete from admin_info where user_id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public AdminInfo get(String userId, String passwd) throws Exception {
		if (!userId.equals("")) {
			String sql = "SELECT user_id,auth_id, auth_id_type,user_name,user_passwd,description "
					+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
					+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
					+" FROM admin_info where user_id = ? and user_passwd = ? ";
			return jdbcTemplate.query(sql, new String[] {userId, passwd}, new ResultSetExtractor<AdminInfo>(){
				@Override
				public AdminInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						AdminInfo adminInfo = new AdminInfo();
						adminInfo.setUserId(rs.getString("user_id"));
						adminInfo.setAuthId(rs.getString("auth_id"));
						adminInfo.setAuthIdType(rs.getString("auth_id_type"));
						adminInfo.setUserName(rs.getString("user_name"));
						adminInfo.setUserPasswd(rs.getString("user_passwd"));
						adminInfo.setDescription(rs.getString("description"));
						adminInfo.setUpdateDate(rs.getString("update_date"));
						adminInfo.setCreateDate(rs.getString("create_date"));
						return adminInfo;
					}
					return null;
				}
				
			});
		} else {
			throw new Exception(String.format("No key value [%s]", userId));
		}
	}

	
	@Override
	public AdminInfo getList(Map<String, String> paramMap) throws Exception {
		String sql = "SELECT user_id,auth_id, auth_id_type,user_name,user_passwd,description "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM admin_info where 1 = 1";
		for (Entry<String, String> entry : paramMap.entrySet()) {
			if (entry.getKey().equals("userId")&&!entry.getValue().trim().equals(""))
				sql += " and user_id like '%"+ entry.getValue()+"%'";
			if (entry.getKey().equals("userName")&&!entry.getValue().trim().equals(""))
				sql += " and user_name like '%"+ entry.getValue()+"%'";
		}
		return jdbcTemplate.query(sql, new ResultSetExtractor<AdminInfo>(){
			@Override
			public AdminInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AdminInfo adminInfo = new AdminInfo();
					adminInfo.setUserId(rs.getString("user_id"));
					adminInfo.setAuthId(rs.getString("auth_id"));
					adminInfo.setAuthIdType(rs.getString("auth_id_type"));
					adminInfo.setUserName(rs.getString("user_name"));
					adminInfo.setUserPasswd(rs.getString("user_passwd"));
					adminInfo.setDescription(rs.getString("description"));
					adminInfo.setUpdateDate(rs.getString("update_date"));
					adminInfo.setCreateDate(rs.getString("create_date"));
					return adminInfo;
				}
				return null;
			}
			
		});
	}
	
	@Override
	public List<AdminInfo> getList(List<String> userList) {
		String userIdStr = "(";
		int i = 0;
		for (String user : userList) {
			if (i == 0) 
				userIdStr += "'"+user+"'";
			else
				userIdStr += ",'"+user+"'";
			i++;						
		}
		userIdStr +=")"; 
		String sql = "SELECT user_id,auth_id,auth_id_type,user_name,user_passwd,description "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM admin_info where user_id in "+userIdStr+"";
		logger.debug("getList: sql:"+sql);
		
		List<AdminInfo> listAdminInfo = jdbcTemplate.query(sql, new RowMapper<AdminInfo>(){
			@Override
			public AdminInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				AdminInfo adminInfo = new AdminInfo();
				adminInfo.setUserId(rs.getString("user_id"));
				adminInfo.setAuthId(rs.getString("auth_id"));
				adminInfo.setAuthIdType(rs.getString("auth_id_type"));
				adminInfo.setUserName(rs.getString("user_name"));
				adminInfo.setUserPasswd(rs.getString("user_passwd"));
				adminInfo.setDescription(rs.getString("description"));
				adminInfo.setUpdateDate(rs.getString("update_date"));
				adminInfo.setCreateDate(rs.getString("create_date"));
				return adminInfo;
			}
		});
		return listAdminInfo;

	}
	@Override
	public AdminInfo get(String userId) throws Exception {
		if (!userId.equals("")) {
			String sql = "SELECT user_id,auth_id, auth_id_type,user_name,user_passwd,description "
					+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
					+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
					+" FROM admin_info where user_id = ? ";
			return jdbcTemplate.query(sql, new String[]{userId}, new ResultSetExtractor<AdminInfo>(){
				@Override
				public AdminInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						AdminInfo adminInfo = new AdminInfo();
						adminInfo.setUserId(rs.getString("user_id"));
						adminInfo.setAuthId(rs.getString("auth_id"));
						adminInfo.setAuthIdType(rs.getString("auth_id_type"));
						adminInfo.setUserName(rs.getString("user_name"));
						adminInfo.setUserPasswd(rs.getString("user_passwd"));
						adminInfo.setDescription(rs.getString("description"));
						adminInfo.setUpdateDate(rs.getString("update_date"));
						adminInfo.setCreateDate(rs.getString("create_date"));
						return adminInfo;
					}
					return null;
				}
			});
		} else {
			throw new Exception(String.format("No key value [%s]", userId));
		}
	}
}
