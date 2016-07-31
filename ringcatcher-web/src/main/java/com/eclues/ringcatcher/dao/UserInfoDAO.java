package com.eclues.ringcatcher.dao;

import java.util.List;

import com.eclues.ringcatcher.UserInfo;

public interface UserInfoDAO {
	public void create(UserInfo userInfo) throws Exception ;
	public void update(UserInfo userInfo) throws Exception ;
	public void delete(String userNum) throws Exception ;
	public UserInfo get(String userNum) throws Exception ;
	public List<UserInfo> getList(List<String> userNum) throws Exception ;
}
