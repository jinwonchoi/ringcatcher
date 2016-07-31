package com.eclues.ringcatcher.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eclues.ringcatcher.MsgHistory;

public class MsgHistoryDAOImpl implements MsgHistoryDAO {

	@Override
	public void create(List<String> userNum, MsgHistory msgHistory) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(List<String> userNum, MsgHistory msgHistory) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(List<String> userNum, String callingNum) throws Exception {
		// TODO Auto-generated method stub
		
	}


	private JdbcTemplate jdbcTemplate;
	
	public MsgHistoryDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		//jdbcTemplate.
	}
		
	@Override
	public void create(MsgHistory msgHistory) throws Exception {
		if (!msgHistory.getUserId().equals("")&& !msgHistory.getUserNum().equals("")
				&&!msgHistory.getCallingNum().equals("")
				&&!msgHistory.getRegisterDate().equals("")
				&&!msgHistory.getJsonMsg().equals("")) {
			//insert
			String sql = "INSERT INTO msg_history(user_num,calling_num, calling_name, register_date, expired_date, user_id, json_msg, duration_type, update_date,create_date)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, now(), now())";
			jdbcTemplate.update(sql, msgHistory.getUserNum()
					, msgHistory.getCallingNum(), msgHistory.getCallingName(), msgHistory.getRegisterDate(), msgHistory.getExpiredDate()
					, msgHistory.getUserId(), msgHistory.getJsonMsg(), msgHistory.getDurationType());	
		} else {
			throw new Exception(String.format("No key value [%s][%s][%s]", msgHistory.getUserNum()
					,msgHistory.getCallingNum()
					,msgHistory.getCreateDate()));
		}
	}

	@Override
	public void update(MsgHistory msgHistory) throws Exception {
		if (!msgHistory.getUserNum().equals("")&&!msgHistory.getCallingNum().equals("")
				&&!msgHistory.getCreateDate().equals("")) {
			//update
			String sql = "UPDATE msg_history SET register_date = ? "
					+ " , expired_date = ?"
					+ " , json_msg = ?"
					+ " , duration_type = ?"
					+ " , update_date = now()"
					+ "where user_Num = ? and calling_num = ? and create_date = str_to_date('?',\"%Y%m%d%H%i%s\")";
			jdbcTemplate.update(sql, msgHistory.getRegisterDate(), msgHistory.getExpiredDate(), msgHistory.getJsonMsg(), msgHistory.getDurationType()
					, msgHistory.getUserNum(), msgHistory.getCallingNum(), msgHistory.getCreateDate());
		} else {
			throw new Exception(String.format("No key value [%s][%s][%s]", msgHistory.getUserNum()
					,msgHistory.getCallingNum()
					,msgHistory.getCreateDate()));
		}
	}
	
	@Override
	public void delete(String userNum, String callingNum) {
		String sql = "Delete from msg_history where user_num = ? and  calling_num = ? ";
		jdbcTemplate.update(sql, userNum, callingNum);
	}

	@Override
	public MsgHistory get(String userNum, String callingNum, String createDate) {
		String sql = "SELECT user_Num, user_num,calling_num,calling_name, register_date, expired_date, user_id, json_msg, duration_type "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_history where user_Num = '"+userNum+"' and  calling_num = '"+callingNum+"' and create_date = str_to_date('"+createDate+"',\"%Y%m%d%H%i%s\")";
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MsgHistory>(){
			@Override
			public MsgHistory extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MsgHistory msgHistory= new MsgHistory();
					msgHistory.setUserNum(rs.getString("user_num"));
					msgHistory.setCallingNum(rs.getString("calling_num"));
					msgHistory.setCallingName(rs.getString("calling_name"));
					msgHistory.setRegisterDate(rs.getString("register_date"));
					msgHistory.setExpiredDate(rs.getString("expired_date"));
					msgHistory.setUserId(rs.getString("user_id"));
					msgHistory.setJsonMsg(rs.getString("json_msg"));
					msgHistory.setDurationType(rs.getString("duration_type"));
					msgHistory.setUpdateDate(rs.getString("update_date"));
					msgHistory.setCreateDate(rs.getString("create_date"));
					return msgHistory;
				}
				return null;
			}
			
		});
		

	}
	
	@Override
	public List<MsgHistory> getByUserNum(String userNum) throws Exception {
		String sql = "SELECT user_num,calling_num,calling_name, register_date, expired_date, user_id, json_msg, duration_type "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_history where user_num = '"+userNum+"'";
		List<MsgHistory> listmsgHistory = jdbcTemplate.query(sql, new RowMapper<MsgHistory>(){
			@Override
			public MsgHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgHistory msgHistory = new MsgHistory();
				msgHistory.setUserNum(rs.getString("user_num"));
				msgHistory.setCallingNum(rs.getString("calling_num"));
				msgHistory.setCallingName(rs.getString("calling_name"));
				msgHistory.setRegisterDate(rs.getString("register_date"));
				msgHistory.setExpiredDate(rs.getString("expired_date"));
				msgHistory.setUserId(rs.getString("user_id"));
				msgHistory.setJsonMsg(rs.getString("json_msg"));
				msgHistory.setDurationType(rs.getString("duration_type"));
				msgHistory.setUpdateDate(rs.getString("update_date"));
				msgHistory.setCreateDate(rs.getString("create_date"));
				return msgHistory;
			}
		});
		return listmsgHistory;		
	}


	@Override
	public List<MsgHistory> list() {
		String sql = "SELECT user_num,calling_num, calling_name, register_date, expired_date, user_id, json_msg, duration_type "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_history";
		List<MsgHistory> listmsgHistory = jdbcTemplate.query(sql, new RowMapper<MsgHistory>(){
			@Override
			public MsgHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgHistory msgHistory = new MsgHistory();
				msgHistory.setUserNum(rs.getString("user_num"));
				msgHistory.setCallingNum(rs.getString("calling_num"));
				msgHistory.setCallingName(rs.getString("calling_name"));
				msgHistory.setRegisterDate(rs.getString("register_date"));
				msgHistory.setExpiredDate(rs.getString("expired_date"));
				msgHistory.setUserId(rs.getString("user_id"));
				msgHistory.setJsonMsg(rs.getString("json_msg"));
				msgHistory.setDurationType(rs.getString("duration_type"));
				msgHistory.setUpdateDate(rs.getString("update_date"));
				msgHistory.setCreateDate(rs.getString("create_date"));
				return msgHistory;
			}
		});
		return listmsgHistory;
	}

}
