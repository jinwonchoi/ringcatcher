package com.eclues.ringcatcher.dao;

import java.util.List;

import com.eclues.ringcatcher.obj.RingHistory;

public interface RingHistoryDAO {
	public void create(RingHistory ringHistory)  throws Exception ;
	public void update(RingHistory ringHistory)  throws Exception ;
	public void delete(String userNum, String callingNum)  throws Exception ;
	public void create(List<String> userNums, RingHistory ringHistory)  throws Exception ;
	public void update(List<String> userNums, RingHistory ringHistory)  throws Exception ;
	public void delete(List<String> userNums, String callingNum)  throws Exception ;
	public RingHistory get(String userNum, String callingNum, String createDate) throws Exception ;
	public List<RingHistory> getByUserNum(String userNum) throws Exception ;
	public List<RingHistory> list() throws Exception ;
}
