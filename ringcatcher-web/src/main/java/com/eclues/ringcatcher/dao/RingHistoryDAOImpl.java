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

import com.eclues.ringcatcher.RingHistory;

public class RingHistoryDAOImpl implements RingHistoryDAO {

	@Override
	public void create(List<String> userNums, RingHistory ringHistory) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(List<String> userNums, RingHistory ringHistory) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(List<String> userNums, String callingNum) throws Exception {
		// TODO Auto-generated method stub
		
	}


	private JdbcTemplate jdbcTemplate;
	
	public RingHistoryDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		//jdbcTemplate.
	}
		
	@Override
	public void create(RingHistory ringHistory) throws Exception {
		if (!ringHistory.getUserId().equals("")&& !ringHistory.getUserNum().equals("")
				&&!ringHistory.getCallingNum().equals("")
				&&!ringHistory.getRegisterDate().equals("")
				&&!ringHistory.getRingFileName().equals("")) {
			//insert
			String sql = "INSERT INTO ring_history(user_num,calling_num, calling_name, register_date, expired_date, user_id, ring_file_name, duration_type, update_date,create_date)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, now(), now())";
			jdbcTemplate.update(sql, ringHistory.getUserNum()
					, ringHistory.getCallingNum(), ringHistory.getCallingName(), ringHistory.getRegisterDate(), ringHistory.getExpiredDate()
					, ringHistory.getUserId(), ringHistory.getRingFileName(), ringHistory.getDurationType());	
		} else {
			throw new Exception(String.format("No key value [%s][%s][%s]", ringHistory.getUserNum()
					,ringHistory.getCallingNum()
					,ringHistory.getRegisterDate()));
		}
	}

	@Override
	public void update(RingHistory ringHistory) throws Exception {
		if (!ringHistory.getUserNum().equals("")&&!ringHistory.getCallingNum().equals("")
				&&!ringHistory.getCreateDate().equals("")) {
			//update
			String sql = "UPDATE ring_history SET register_date = ? "
					+ " , expired_date = ?"
					+ " , ring_file_name = ?"
					+ " , duration_type = ?"
					+ " , update_date = now()"
					+ "where user_Num = ? and calling_num = ? and create_date = str_to_date('?',\"%Y%m%d%H%i%s\")";
			jdbcTemplate.update(sql, ringHistory.getRegisterDate(), ringHistory.getExpiredDate(), ringHistory.getRingFileName(), ringHistory.getDurationType()
					, ringHistory.getUserNum(), ringHistory.getCallingNum(),ringHistory.getCreateDate());
		} else {
			throw new Exception(String.format("No key value [%s][%s][%s]", ringHistory.getUserNum()
					,ringHistory.getCallingNum()
					,ringHistory.getCreateDate()));
		}
	}
	
	@Override
	public void delete(String userNum, String callingNum) {
		String sql = "Delete from ring_history where user_num = ? and  calling_num = ? ";
		jdbcTemplate.update(sql, userNum, callingNum);
	}

	@Override
	public RingHistory get(String userNum, String callingNum, String createDate) {
		String sql = "SELECT user_Num, user_num,calling_num,calling_name, register_date, expired_date, user_id, ring_file_name, duration_type "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM ring_history where user_Num = '"+userNum+"' and  calling_num = '"+callingNum+"' and create_date = str_to_date('"+createDate+"',\"%Y%m%d%H%i%s\") and expired_date >= DATE_FORMAT(now(), '%Y%m%d') ";
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<RingHistory>(){
			@Override
			public RingHistory extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					RingHistory ringHistory= new RingHistory();
					ringHistory.setUserNum(rs.getString("user_num"));
					ringHistory.setCallingNum(rs.getString("calling_num"));
					ringHistory.setCallingName(rs.getString("calling_name"));
					ringHistory.setRegisterDate(rs.getString("register_date"));
					ringHistory.setExpiredDate(rs.getString("expired_date"));
					ringHistory.setUserId(rs.getString("user_id"));
					ringHistory.setRingFileName(rs.getString("ring_file_name"));
					ringHistory.setDurationType(rs.getString("duration_type"));
					ringHistory.setUpdateDate(rs.getString("update_date"));
					ringHistory.setCreateDate(rs.getString("create_date"));
					return ringHistory;
				}
				return null;
			}
			
		});
		

	}
	
	@Override
	public List<RingHistory> getByUserNum(String userNum) throws Exception {
		String sql = "SELECT user_num,calling_num,calling_name, register_date, expired_date, user_id, ring_file_name, duration_type "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM ring_history where user_num = '"+userNum+"'";
		List<RingHistory> listringHistory = jdbcTemplate.query(sql, new RowMapper<RingHistory>(){
			@Override
			public RingHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
				RingHistory ringHistory = new RingHistory();
				ringHistory.setUserNum(rs.getString("user_num"));
				ringHistory.setCallingNum(rs.getString("calling_num"));
				ringHistory.setCallingName(rs.getString("calling_name"));
				ringHistory.setRegisterDate(rs.getString("register_date"));
				ringHistory.setExpiredDate(rs.getString("expired_date"));
				ringHistory.setUserId(rs.getString("user_id"));
				ringHistory.setRingFileName(rs.getString("ring_file_name"));
				ringHistory.setDurationType(rs.getString("duration_type"));
				ringHistory.setUpdateDate(rs.getString("update_date"));
				ringHistory.setCreateDate(rs.getString("create_date"));
				return ringHistory;
			}
		});
		return listringHistory;		
	}


	@Override
	public List<RingHistory> list() {
		String sql = "SELECT user_num,calling_num, calling_name, register_date, expired_date, user_id, ring_file_name, duration_type "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM ring_history";
		List<RingHistory> listringHistory = jdbcTemplate.query(sql, new RowMapper<RingHistory>(){
			@Override
			public RingHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
				RingHistory ringHistory = new RingHistory();
				ringHistory.setUserNum(rs.getString("user_num"));
				ringHistory.setCallingNum(rs.getString("calling_num"));
				ringHistory.setCallingName(rs.getString("calling_name"));
				ringHistory.setRegisterDate(rs.getString("register_date"));
				ringHistory.setExpiredDate(rs.getString("expired_date"));
				ringHistory.setUserId(rs.getString("user_id"));
				ringHistory.setRingFileName(rs.getString("ring_file_name"));
				ringHistory.setDurationType(rs.getString("duration_type"));
				ringHistory.setUpdateDate(rs.getString("update_date"));
				ringHistory.setCreateDate(rs.getString("create_date"));
				return ringHistory;
			}
		});
		return listringHistory;
	}

}
