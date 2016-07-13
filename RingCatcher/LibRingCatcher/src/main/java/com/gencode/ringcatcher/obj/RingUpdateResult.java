package com.gencode.ringcatcher.obj;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class RingUpdateResult implements Serializable {

	final public String TAG = RingUpdateResult.class.getSimpleName();
	private static final long serialVersionUID = -242858495909118917L;

//	final public String CALLING_NUM = "callingNum";
//	final public String FILE_PATH = "filePath";
	String resultCode;
	String resultMsg;
	List<String> updateList = new ArrayList<String>();//callingNum, filePath
	public RingUpdateResult() {
		// TODO Auto-generated constructor stub
	}
		
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	public List<String> getUpdateList() {
		return updateList;
	}
	
	public void setUpdateItem(String callingNum, String filePath, String expiredDate, String durationType) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonConstants.callingNum, callingNum);
		jsonObject.put(JsonConstants.filePath, filePath);
		jsonObject.put(JsonConstants.expiredDate, expiredDate);
		jsonObject.put(JsonConstants.durationType, durationType);
		updateList.add(jsonObject.toString());
	}

	public void setUpdateList(ArrayList<String> updateList) {
		this.updateList = updateList;
	}

	public String toString()  {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(String.format("[%s][%s]", resultCode, resultMsg));
			for (String item : updateList) {
				JSONObject jobject = new JSONObject(item);
				String callingNum = jobject.getString(JsonConstants.callingNum);
				String filePath = jobject.getString(JsonConstants.filePath);
				String expiredDate = jobject.getString(JsonConstants.expiredDate);
				String durationType = jobject.getString(JsonConstants.durationType);
				sb.append(String.format("{[%s]:[%s],[%s]:[%s],[%s]:[%s],[%s]:[%s]}"
						, JsonConstants.callingNum, callingNum,JsonConstants.expiredDate, expiredDate
						, JsonConstants.durationType, durationType
						, JsonConstants.filePath, filePath));

			}
		} catch (JSONException je) {
			Log.e(TAG, "JSON toString error :"+updateList);
		}

		return sb.toString();
	}

}
