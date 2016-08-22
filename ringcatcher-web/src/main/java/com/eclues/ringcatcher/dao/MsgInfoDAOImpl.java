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

import com.eclues.ringcatcher.obj.MsgInfo;

public class MsgInfoDAOImpl implements MsgInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(MsgInfoDAOImpl.class);
	@Override
	public void create(List<String> userNums, MsgInfo msgInfo) throws Exception {
		if (!msgInfo.getUserNum().equals("")&&!msgInfo.getCallingNum().equals("")) {
			//insert
			String sql = "INSERT INTO msg_info(user_num, calling_num, calling_name, register_date, expired_date, json_msg, duration_type, update_date, create_date)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, now(),now())";
			logger.info("sql="+sql);
			List<Object[]> batchArgs = new ArrayList<Object[]>(); 
			for (String userNum : userNums) {
				Object[] arrParam = {userNum, msgInfo.getCallingNum(), msgInfo.getCallingName(), msgInfo.getRegisterDate()
						,msgInfo.getExpiredDate(), msgInfo.getJsonMsg(), msgInfo.getDurationType()};
				batchArgs.add(arrParam);
				logger.info(String.format("arrParam[%s][%s][%s][%s][%s][%s][%s]"
						,userNum, msgInfo.getCallingNum(), msgInfo.getCallingName(), msgInfo.getRegisterDate()
						,msgInfo.getExpiredDate(), msgInfo.getJsonMsg(), msgInfo.getDurationType()));

			}
			jdbcTemplate.batchUpdate(sql, batchArgs);	
		} else {
			throw new Exception(String.format("No key value [%s][%s]", msgInfo.getUserNum()
					,msgInfo.getCallingNum()));
		}
	}

	@Override
	public void update(List<String> userNums, MsgInfo msgInfo) throws Exception {
		if (!msgInfo.getUserNum().equals("")&&!msgInfo.getCallingNum().equals("")) {
			//update
			String sql = "UPDATE msg_info SET register_date = ? , expired_date = ? "
					+ " , json_msg = ?"
					+ " , duration_type = ?"
					+ " , download_cnt = ?"
					+ " , update_date = now()"
					+ "where user_num = ? and calling_num = ?";
			logger.info("sql="+sql);
			List<Object[]> batchArgs = new ArrayList<Object[]>(); 
			for (String userNum : userNums) {
				Object[] arrParam = {msgInfo.getRegisterDate(), msgInfo.getExpiredDate(), msgInfo.getJsonMsg(), msgInfo.getDurationType()
						, msgInfo.getDownloadCnt() 
						, userNum, msgInfo.getCallingNum()};
				batchArgs.add(arrParam);
				logger.info(String.format("arrParam[%s][%s][%s][%s][%d][%s][%s]"
						,msgInfo.getRegisterDate(), msgInfo.getExpiredDate(), msgInfo.getJsonMsg(), msgInfo.getDurationType()
						, msgInfo.getDownloadCnt() 
						, userNum, msgInfo.getCallingNum()));
			}
			int ret[] = jdbcTemplate.batchUpdate(sql, batchArgs);
			String retStr = "";
		} else {
			throw new Exception(String.format("No key value [%s][%s]", msgInfo.getUserNum()
					,msgInfo.getCallingNum()));
		}
	}

	@Override
	public void delete(List<String> userNums, String callingNum) throws Exception {
		// TODO Auto-generated method stub
		if (!(userNums.size()==0)&&!callingNum.equals("")) {
			//update
			String sql = "Delete from msg_info where user_num = ? and calling_num = ?";
			List<Object[]> batchArgs = new ArrayList<Object[]>(); 
			for (String userNum : userNums) {
				Object[] arrParam = {userNum, callingNum};
				batchArgs.add(arrParam);
			}
			logger.info("sql="+sql);
			logger.info("batchArgs="+batchArgs);
			jdbcTemplate.batchUpdate(sql, batchArgs);	
		} else {
			throw new Exception(String.format("No key value [%s][%s]", userNums.get(0)
					,callingNum));
		}

	}


	private JdbcTemplate jdbcTemplate;
	
	public MsgInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
		
	@Override
	public void create(MsgInfo msgInfo) throws Exception {
		if (!msgInfo.getUserNum().equals("")&&!msgInfo.getCallingNum().equals("")) {
			//insert
			String sql = "INSERT INTO msg_info(user_num, calling_num, calling_name, register_date, expired_date, json_msg, duration_type, update_date, create_date)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, now(),now())";
			logger.info("sql="+sql);
			logger.info("msgInfo="+msgInfo);
			jdbcTemplate.update(sql, msgInfo.getUserNum()
					, msgInfo.getCallingNum(), msgInfo.getCallingName(), msgInfo.getRegisterDate(), msgInfo.getExpiredDate()
					, msgInfo.getJsonMsg(), msgInfo.getDurationType());	
		} else {
			throw new Exception(String.format("No key value [%s][%s]", msgInfo.getUserNum()
					,msgInfo.getCallingNum()));
		}
	}
	
	@Override
	public void update(MsgInfo msgInfo) throws Exception {
		if (!msgInfo.getUserNum().equals("")&&!msgInfo.getCallingNum().equals("")) {
			//update
			String sql = "UPDATE msg_info SET register_date = ? , expired_date = ? "
					+ " , json_msg = ?"
					+ " , duration_type = ?"
					+ " , download_cnt = ?"
					+ " , update_date = now()"
					+ "where user_num = ? and calling_num = ?";
			logger.info("sql="+sql);
			logger.info("msgInfo="+msgInfo);
			jdbcTemplate.update(sql, msgInfo.getRegisterDate(), msgInfo.getExpiredDate(), msgInfo.getJsonMsg(), msgInfo.getDurationType()
					, msgInfo.getDownloadCnt() 
					, msgInfo.getUserNum(), msgInfo.getCallingNum());
		} else {
			throw new Exception(String.format("No key value [%s][%s]", msgInfo.getUserNum()
					,msgInfo.getCallingNum()));
		}
	}

	
	@Override
	public void delete(String userNum, String callingNum) {
		String sql = "Delete from msg_info where user_num = ? and calling_num = ?";
		logger.info("sql="+sql);
		logger.info(String.format("userNum[%s]callingNum[%s]",userNum, callingNum));
		jdbcTemplate.update(sql, userNum, callingNum);
	}

	@Override
	public MsgInfo get(String userNum, String callingNum) {
		String sql = "SELECT user_num,calling_num,calling_name, register_date, expired_date, json_msg, duration_type, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_info where user_num = '"+userNum+"' and calling_num = '"+callingNum+"'";
		logger.info("sql="+sql);
		logger.info(String.format("userNum[%s]callingNum[%s]",userNum, callingNum));
		return jdbcTemplate.query(sql, new ResultSetExtractor<MsgInfo>(){
			@Override
			public MsgInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MsgInfo msgInfo = new MsgInfo();
					msgInfo.setUserNum(rs.getString("user_num"));
					msgInfo.setCallingNum(rs.getString("calling_num"));
					msgInfo.setCallingName(rs.getString("calling_name"));
					msgInfo.setRegisterDate(rs.getString("register_date"));
					msgInfo.setExpiredDate(rs.getString("expired_date"));
					msgInfo.setJsonMsg(rs.getString("json_msg"));
					msgInfo.setDurationType(rs.getString("duration_type"));
					msgInfo.setDownloadCnt(rs.getInt("download_cnt"));
					msgInfo.setUpdateDate(rs.getString("update_date"));
					msgInfo.setCreateDate(rs.getString("create_date"));
					return msgInfo;
				}
				return null;
			}
			
		});
	}
	
	@Override
	public List<MsgInfo> getList(List<String> userList, String callingNum) throws Exception {
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
		
		String sql = "SELECT user_num,calling_num,calling_name, register_date, expired_date, json_msg, duration_type, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_info where user_num in "+userNumStr+" and calling_num = '"+callingNum+"'";
		logger.info("sql="+sql);
		logger.info(String.format("userNumStr[%s]callingNum[%s]",userNumStr, callingNum));

		List<MsgInfo> listMsgInfo = jdbcTemplate.query(sql, new RowMapper<MsgInfo>(){
			@Override
			public MsgInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgInfo msgInfo = new MsgInfo();
				msgInfo.setUserNum(rs.getString("user_num"));
				msgInfo.setCallingNum(rs.getString("calling_num"));
				msgInfo.setCallingName(rs.getString("calling_name"));
				msgInfo.setRegisterDate(rs.getString("register_date"));
				msgInfo.setExpiredDate(rs.getString("expired_date"));
				msgInfo.setJsonMsg(rs.getString("json_msg"));
				msgInfo.setDurationType(rs.getString("duration_type"));
				msgInfo.setDownloadCnt(rs.getInt("download_cnt"));
				msgInfo.setUpdateDate(rs.getString("update_date"));
				msgInfo.setCreateDate(rs.getString("create_date"));
				return msgInfo;
			}
		});
		return listMsgInfo;
	}
	
	/**
	 *  나한테 보낸 메시지나 default로 보낸 메시지를 쿼리
	 * 
	 */
	public List<MsgInfo> getList(List<String> fromNums, String toNum, String updateDate) throws Exception {
		String fromNumStr = "(";
		int i = 0;
		for (String user : fromNums) {
			if (i == 0) 
				fromNumStr += "'"+user+"'";
			else
				fromNumStr += ",'"+user+"'";
			i++;						
		}
		fromNumStr +=")"; 

		String sql = "SELECT user_num,calling_num,calling_name, register_date, expired_date, json_msg, duration_type, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_info where calling_num in "+fromNumStr+" and user_num = '"+toNum+"'";
		logger.info("sql="+sql);
		logger.info(String.format("fromNumStr[%s]toNum[%s]",fromNumStr, toNum));

		if (updateDate != null && !"".equals(updateDate)) {
			sql += " and update_date > str_to_date('"+updateDate+"',\"%Y%m%d%H%i%s\")";
		}

		logger.info("sql:"+sql);
		List<MsgInfo> listMsgInfo = jdbcTemplate.query(sql, new RowMapper<MsgInfo>(){
			@Override
			public MsgInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgInfo msgInfo = new MsgInfo();
				msgInfo.setUserNum(rs.getString("user_num"));
				msgInfo.setCallingNum(rs.getString("calling_num"));
				msgInfo.setCallingName(rs.getString("calling_name"));
				msgInfo.setRegisterDate(rs.getString("register_date"));
				msgInfo.setExpiredDate(rs.getString("expired_date"));
				msgInfo.setJsonMsg(rs.getString("json_msg"));
				msgInfo.setDurationType(rs.getString("duration_type"));
				msgInfo.setDownloadCnt(rs.getInt("download_cnt"));
				msgInfo.setUpdateDate(rs.getString("update_date"));
				msgInfo.setCreateDate(rs.getString("create_date"));
				return msgInfo;
			}
		});
		return listMsgInfo;
	}
	
	@Override
	public List<MsgInfo> getByUserNum(String userNum, boolean all) throws Exception 
	 {
		String sql = "SELECT user_num,calling_num, calling_name, register_date, expired_date, json_msg, duration_type, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_info where user_num = '"+userNum+"'";
		if (!all)
			sql	+="  and download_cnt = 0";
		logger.info("sql="+sql);
		logger.info(String.format("userNum[%s]",userNum));

		List<MsgInfo> listMsgInfo = jdbcTemplate.query(sql, new RowMapper<MsgInfo>(){
			@Override
			public MsgInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgInfo msgInfo = new MsgInfo();
				msgInfo.setUserNum(rs.getString("user_num"));
				msgInfo.setCallingNum(rs.getString("calling_num"));
				msgInfo.setCallingName(rs.getString("calling_name"));
				msgInfo.setRegisterDate(rs.getString("register_date"));
				msgInfo.setExpiredDate(rs.getString("expired_date"));
				msgInfo.setJsonMsg(rs.getString("json_msg"));
				msgInfo.setDurationType(rs.getString("duration_type"));
				msgInfo.setDownloadCnt(rs.getInt("download_cnt"));
				msgInfo.setUpdateDate(rs.getString("update_date"));
				msgInfo.setCreateDate(rs.getString("create_date"));
				return msgInfo;
			}
		});
		return listMsgInfo;
	}

	@Override
	public List<MsgInfo> getListByAdmin(Map<String, String> argMap) {
		String sql = "SELECT user_num,calling_num, calling_name, register_date, expired_date, json_msg, duration_type, download_cnt "
				+",DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date "
				+",DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date"
				+" FROM msg_info"
				+" where 1 = 1";
		for (Entry<String, String> entry : argMap.entrySet()) {
			if (entry.getKey().equals("userNum")) 
				sql += " and user_num like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("callingNum")) 
				sql += " and calling_num like '%"+entry.getValue()+"%'";
		}
		
		logger.info("sql="+sql);
		List<MsgInfo> listMsgInfo = jdbcTemplate.query(sql, new RowMapper<MsgInfo>(){
			@Override
			public MsgInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgInfo msgInfo = new MsgInfo();
				msgInfo.setUserNum(rs.getString("user_num"));
				msgInfo.setCallingNum(rs.getString("calling_num"));
				msgInfo.setCallingName(rs.getString("calling_name"));
				msgInfo.setRegisterDate(rs.getString("register_date"));
				msgInfo.setExpiredDate(rs.getString("expired_date"));
				msgInfo.setJsonMsg(rs.getString("json_msg"));
				msgInfo.setDurationType(rs.getString("duration_type"));
				msgInfo.setDownloadCnt(rs.getInt("download_cnt"));
				msgInfo.setUpdateDate(rs.getString("update_date"));
				msgInfo.setCreateDate(rs.getString("create_date"));
				return msgInfo;
			}
		});
		return listMsgInfo;
	}

}
