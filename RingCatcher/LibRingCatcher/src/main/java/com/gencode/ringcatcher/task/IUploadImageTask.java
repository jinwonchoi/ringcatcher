package com.gencode.ringcatcher.task;

import com.gencode.ringcatcher.obj.UploadImageResult;

/**
 * Created by Administrator on 2016-04-20.
 */
public interface IUploadImageTask {
    public void OnTaskCompleted(UploadImageResult result);
}
