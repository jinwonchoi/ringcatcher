package com.eclues.ringcatcher.dao;

import java.util.List;
import java.util.Map;

import com.eclues.ringcatcher.obj.AdminInfo;

public interface AdminInfoDAO {
	public void create(AdminInfo adminInfo) throws Exception ;
	public void update(AdminInfo adminInfo) throws Exception ;
	public void delete(String userId) throws Exception ;
	public AdminInfo get(String userId) throws Exception ;
	public AdminInfo get(String userId, String passwd) throws Exception;
	public AdminInfo getList(Map<String, String> paramMap) throws Exception;
	public List<AdminInfo> getList(List<String> Id) throws Exception ;
}
