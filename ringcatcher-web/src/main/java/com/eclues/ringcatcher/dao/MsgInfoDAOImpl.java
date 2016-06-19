package com.eclues.ringcatcher.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eclues.ringcatcher.MsgInfo;

public class MsgInfoDAOImpl implements MsgInfoDAO {

	private JdbcTemplate jdbcTemplate;
	
	public MsgInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
		
	@Override
	public void create(MsgInfo msgInfo) throws Exception {
		if (!msgInfo.getUserNum().equals("")&&!msgInfo.getCallingNum().equals("")) {
			//insert
			String sql = "INSERT INTO msg_info(user_num,calling_num,calling_name, register_date, json_msg, update_date,create_date)"
					+ " VALUES (?, ?, ?, ?,?, now(),now())";
			jdbcTemplate.update(sql, msgInfo.getUserNum()
					, msgInfo.getCallingNum(), msgInfo.getCallingName(), msgInfo.getRegisterDate()
					, msgInfo.getJsonMsg());	
		} else {
			throw new Exception(String.format("No key value [%s][%s]", msgInfo.getUserNum()
					,msgInfo.getCallingNum()));
		}
	}

	@Override
	public void update(MsgInfo msgInfo) throws Exception {
		if (!msgInfo.getUserNum().equals("")&&!msgInfo.getCallingNum().equals("")) {
			//update
			String sql = "UPDATE msg_info SET register_date = ? "
					+ " , json_msg = ?"
					+ " , download_cnt = ?"
					+ " , update_date = now()"
					+ "where user_num = ? and calling_num = ?";
			jdbcTemplate.update(sql, msgInfo.getRegisterDate(), msgInfo.getJsonMsg()
					, msgInfo.getDownload_cnt() 
					, msgInfo.getUserNum(), msgInfo.getCallingNum());
		} else {
			throw new Exception(String.format("No key value [%s][%s]", msgInfo.getUserNum()
					,msgInfo.getCallingNum()));
		}
	}

	
	@Override
	public void delete(String userNum, String callingNum) {
		String sql = "Delete from msg_info where user_num = ? and calling_num = ?";
		jdbcTemplate.update(sql, userNum, callingNum);
	}

	@Override
	public MsgInfo get(String userNum, String callingNum) {
		String sql = "SELECT user_num,calling_num,calling_name, register_date, json_msg, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_info where user_num = '"+userNum+"' and calling_num = '"+callingNum+"'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<MsgInfo>(){
			@Override
			public MsgInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MsgInfo msgInfo = new MsgInfo();
					msgInfo.setUserNum(rs.getString("user_num"));
					msgInfo.setCallingNum(rs.getString("calling_num"));
					msgInfo.setCallingName(rs.getString("calling_name"));
					msgInfo.setRegisterDate(rs.getString("register_date"));
					msgInfo.setJsonMsg(rs.getString("json_msg"));
					msgInfo.setDownload_cnt(rs.getInt("download_cnt"));
					msgInfo.setUpdateDate(rs.getString("update_date"));
					msgInfo.setCreateDate(rs.getString("create_date"));
					return msgInfo;
				}
				return null;
			}
			
		});
	}
	
	@Override
	public List<MsgInfo> getByUserNum(String userNum, boolean all) throws Exception 
	 {
		String sql = "SELECT user_num,calling_num, calling_name, register_date, json_msg, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_info where user_num = '"+userNum+"'";
		if (!all)
			sql	+="  and download_cnt = 0";
		List<MsgInfo> listMsgInfo = jdbcTemplate.query(sql, new RowMapper<MsgInfo>(){
			@Override
			public MsgInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgInfo msgInfo = new MsgInfo();
				msgInfo.setUserNum(rs.getString("user_num"));
				msgInfo.setCallingNum(rs.getString("calling_num"));
				msgInfo.setCallingName(rs.getString("calling_name"));
				msgInfo.setRegisterDate(rs.getString("register_date"));
				msgInfo.setJsonMsg(rs.getString("json_msg"));
				msgInfo.setDownload_cnt(rs.getInt("download_cnt"));
				msgInfo.setUpdateDate(rs.getString("update_date"));
				msgInfo.setCreateDate(rs.getString("create_date"));
				return msgInfo;
			}
		});
		return listMsgInfo;
	}


	@Override
	public List<MsgInfo> list() {
		String sql = "SELECT user_num,calling_num, calling_name, register_date, json_msg, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_info";
		List<MsgInfo> listMsgInfo = jdbcTemplate.query(sql, new RowMapper<MsgInfo>(){
			@Override
			public MsgInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgInfo msgInfo = new MsgInfo();
				msgInfo.setUserNum(rs.getString("user_num"));
				msgInfo.setCallingNum(rs.getString("calling_num"));
				msgInfo.setCallingName(rs.getString("calling_name"));
				msgInfo.setRegisterDate(rs.getString("register_date"));
				msgInfo.setJsonMsg(rs.getString("json_msg"));
				msgInfo.setDownload_cnt(rs.getInt("download_cnt"));
				msgInfo.setUpdateDate(rs.getString("update_date"));
				msgInfo.setCreateDate(rs.getString("create_date"));
				return msgInfo;
			}
		});
		return listMsgInfo;
	}

}
