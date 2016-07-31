package com.eclues.ringcatcher.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eclues.ringcatcher.RingInfo;

public class RingInfoDAOImpl implements RingInfoDAO {

	private JdbcTemplate jdbcTemplate;
	
	public RingInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
		
	@Override
	public void create(RingInfo ringInfo) throws Exception {
		if (!ringInfo.getUserNum().equals("")&&!ringInfo.getCallingNum().equals("")) {
			//insert
			String sql = "INSERT INTO ring_info(user_num,calling_num,calling_name, register_date, expired_date, ring_file_name, duration_type, update_date,create_date)"
					+ " VALUES (?, ?, ?, ?,?, ?,?,now(),now())";
			jdbcTemplate.update(sql, ringInfo.getUserNum()
					, ringInfo.getCallingNum(), ringInfo.getCallingName(), ringInfo.getRegisterDate(), ringInfo.getExpiredDate()
					, ringInfo.getRingFileName(), ringInfo.getDurationType());	
		} else {
			throw new Exception(String.format("No key value [%s][%s]", ringInfo.getUserNum()
					,ringInfo.getCallingNum()));
		}
	}

	@Override
	public void update(List<String> userNumList, RingInfo ringInfo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(List<String> userNumList, String callingNum) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create(List<String> userNums, RingInfo ringInfo) throws Exception {
		//todo
	}

	@Override
	public void update(RingInfo ringInfo) throws Exception {
		if (!ringInfo.getUserNum().equals("")&&!ringInfo.getCallingNum().equals("")) {
			//update
			String sql = "UPDATE ring_info SET register_date = ? "
					+ " , expired_date = ?"
					+ " , ring_file_name = ?"
					+ " , duration_type = ?"
					+ " , download_cnt = ?"
					+ " , update_date = now()"
					+ "where user_num = ? and calling_num = ?";
			jdbcTemplate.update(sql, ringInfo.getRegisterDate(), ringInfo.getExpiredDate(), ringInfo.getRingFileName(), ringInfo.getDurationType()
					, ringInfo.getDownload_cnt() 
					, ringInfo.getUserNum(), ringInfo.getCallingNum());
		} else {
			throw new Exception(String.format("No key value [%s][%s]", ringInfo.getUserNum()
					,ringInfo.getCallingNum()));
		}
	}

	
	@Override
	public void delete(String userNum, String callingNum) {
		String sql = "Delete from ring_info where user_num = ? and calling_num = ?";
		jdbcTemplate.update(sql, userNum, callingNum);
	}

	@Override
	public RingInfo get(String userNum, String callingNum) {
		String sql = "SELECT user_num,calling_num,calling_name, register_date, expired_date, ring_file_name, duration_type, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM ring_info where user_num = '"+userNum+"' and calling_num = '"+callingNum+"'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<RingInfo>(){
			@Override
			public RingInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					RingInfo ringInfo = new RingInfo();
					ringInfo.setUserNum(rs.getString("user_num"));
					ringInfo.setCallingNum(rs.getString("calling_num"));
					ringInfo.setCallingName(rs.getString("calling_name"));
					ringInfo.setExpiredDate(rs.getString("expired_date"));
					ringInfo.setRingFileName(rs.getString("ring_file_name"));
					ringInfo.setDurationType(rs.getString("duration_type"));
					ringInfo.setDownload_cnt(rs.getInt("download_cnt"));
					ringInfo.setUpdateDate(rs.getString("update_date"));
					ringInfo.setCreateDate(rs.getString("create_date"));
					return ringInfo;
				}
				return null;
			}
			
		});
	}
	
	@Override
	public List<RingInfo> getByUserNum(String userNum, boolean all) throws Exception 
	 {
		String sql = "SELECT user_num,calling_num, calling_name, register_date, expired_date, ring_file_name, duration_type, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM ring_info where user_num = '"+userNum+"'";
		if (!all)
			sql	+="  and download_cnt = 0";
		List<RingInfo> listRingInfo = jdbcTemplate.query(sql, new RowMapper<RingInfo>(){
			@Override
			public RingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RingInfo ringInfo = new RingInfo();
				ringInfo.setUserNum(rs.getString("user_num"));
				ringInfo.setCallingNum(rs.getString("calling_num"));
				ringInfo.setCallingName(rs.getString("calling_name"));
				ringInfo.setRegisterDate(rs.getString("register_date"));
				ringInfo.setExpiredDate(rs.getString("expired_date"));
				ringInfo.setRingFileName(rs.getString("ring_file_name"));
				ringInfo.setDurationType(rs.getString("duration_type"));
				ringInfo.setDownload_cnt(rs.getInt("download_cnt"));
				ringInfo.setUpdateDate(rs.getString("update_date"));
				ringInfo.setCreateDate(rs.getString("create_date"));
				return ringInfo;
			}
		});
		return listRingInfo;
	}


	@Override
	public List<RingInfo> list() {
		String sql = "SELECT user_num,calling_num, calling_name, register_date, expired_date, ring_file_name, duration_type, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM ring_info";
		List<RingInfo> listRingInfo = jdbcTemplate.query(sql, new RowMapper<RingInfo>(){
			@Override
			public RingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RingInfo ringInfo = new RingInfo();
				ringInfo.setUserNum(rs.getString("user_num"));
				ringInfo.setCallingNum(rs.getString("calling_num"));
				ringInfo.setCallingName(rs.getString("calling_name"));
				ringInfo.setRegisterDate(rs.getString("register_date"));
				ringInfo.setExpiredDate(rs.getString("expired_date"));
				ringInfo.setRingFileName(rs.getString("ring_file_name"));
				ringInfo.setDurationType(rs.getString("duration_type"));
				ringInfo.setDownload_cnt(rs.getInt("download_cnt"));
				ringInfo.setUpdateDate(rs.getString("update_date"));
				ringInfo.setCreateDate(rs.getString("create_date"));
				return ringInfo;
			}
		});
		return listRingInfo;
	}

}
