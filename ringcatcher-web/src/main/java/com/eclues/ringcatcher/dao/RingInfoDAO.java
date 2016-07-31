package com.eclues.ringcatcher.dao;

import java.util.List;

import com.eclues.ringcatcher.RingInfo;

public interface RingInfoDAO {
	public void create(RingInfo ringInfo) throws Exception ;
	public void update(RingInfo ringInfo) throws Exception ;
	public void delete(String userNum, String callingNum) throws Exception ;
	public void create(List<String> userNumList, RingInfo ringInfo) throws Exception ;
	public void update(List<String> userNumList, RingInfo ringInfo) throws Exception ;
	public void delete(List<String> userNumList, String callingNum) throws Exception ;

	public RingInfo get(String userNum, String callingNum) throws Exception ;
	public List<RingInfo> getByUserNum(String userNum, boolean all) throws Exception ;
	public List<RingInfo> list() throws Exception ;
}
