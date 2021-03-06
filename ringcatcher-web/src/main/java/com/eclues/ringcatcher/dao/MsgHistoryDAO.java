package com.eclues.ringcatcher.dao;

import java.util.List;

import com.eclues.ringcatcher.obj.MsgHistory;

public interface MsgHistoryDAO {
	public void create(MsgHistory msgHistory)  throws Exception ;
	public void update(MsgHistory msgHistory)  throws Exception ;
	public void delete(String userNum, String callingNum)  throws Exception ;
	public void create(List<String> userNum, MsgHistory msgHistory)  throws Exception ;
	public void update(List<String> userNum, MsgHistory msgHistory)  throws Exception ;
	public void delete(List<String> userNum, String callingNum)  throws Exception ;
	public MsgHistory get(String userNum, String callingNum, String createDate) throws Exception ;
	public List<MsgHistory> getByUserNum(String userNum) throws Exception ;
	public List<MsgHistory> list() throws Exception ;
}
