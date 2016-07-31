package com.eclues.ringcatcher.dao;

import java.util.List;

import com.eclues.ringcatcher.MsgInfo;

public interface MsgInfoDAO {
	public void create(MsgInfo msgInfo) throws Exception ;
	public void update(MsgInfo msgInfo) throws Exception ;
	public void delete(String userNum, String callingNum) throws Exception ;
	public void create(List<String> userNums, MsgInfo msgInfo) throws Exception ;
	public void update(List<String> userNums, MsgInfo msgInfo) throws Exception ;
	public void delete(List<String> userNums, String callingNum) throws Exception ;
	public MsgInfo get(String userNum, String callingNum) throws Exception ;
	public List<MsgInfo> getByUserNum(String userNum, boolean all) throws Exception ;
	public List<MsgInfo> list() throws Exception ;
}
