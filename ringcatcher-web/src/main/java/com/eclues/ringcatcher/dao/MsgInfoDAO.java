package com.eclues.ringcatcher.dao;

import java.util.List;
import java.util.Map;

import com.eclues.ringcatcher.obj.MsgInfo;

public interface MsgInfoDAO {
	public void create(MsgInfo msgInfo) throws Exception ;
	public void update(MsgInfo msgInfo) throws Exception ;
	public void delete(String userNum, String callingNum) throws Exception ;
	public void create(List<String> userNums, MsgInfo msgInfo) throws Exception ;
	public void update(List<String> userNums, MsgInfo msgInfo) throws Exception ;
	public void delete(List<String> userNums, String callingNum) throws Exception ;
	public MsgInfo get(String userNum, String callingNum) throws Exception ;
	public List<MsgInfo> getList(List<String> userNum, String callingNum) throws Exception ;
	/** 나한테 보낸 메시지나 default로 보낸 메시지를 쿼리*/
	public List<MsgInfo> getList(List<String> fromNums, String toNum, String updateDate) throws Exception ;
	public List<MsgInfo> getByUserNum(String userNum, boolean all) throws Exception ;
	public List<MsgInfo> getListByAdmin(Map<String, String> argMap) throws Exception ;
}
