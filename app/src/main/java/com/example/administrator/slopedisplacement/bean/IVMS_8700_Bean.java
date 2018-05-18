package com.example.administrator.slopedisplacement.bean;

import java.io.Serializable;

/**
 * 海康8700播放视频所需数据
 */

public class IVMS_8700_Bean implements Serializable{

    private String mUserName;
    private String mPassword;
    private String mSysCode;
    private String mIp;
    private String mPort;
    private String mType;

    private String Cam_Dx_Puid;
    private String camId;
    private String camName;
    private String mRtsp;
    private String camFlowState;

    public IVMS_8700_Bean() {
    }

    public IVMS_8700_Bean(String mUserName, String mPassword, String mSysCode, String mIp, String mPort, String mType, String cam_Dx_Puid, String camId, String camName, String camFlowState) {
        this.mUserName = mUserName;
        this.mPassword = mPassword;
        this.mSysCode = mSysCode;
        this.mIp = mIp;
        this.mPort = mPort;
        this.mType = mType;
        Cam_Dx_Puid = cam_Dx_Puid;
        this.camId = camId;
        this.camName = camName;
        this.camFlowState = camFlowState;

    }

    public String getmRtsp() {
        return mRtsp;
    }

    public void setmRtsp(String mRtsp) {
        this.mRtsp = mRtsp;
    }

    public String getCamFlowState() {
        return camFlowState;
    }

    public void setCamFlowState(String camFlowState) {
        this.camFlowState = camFlowState;
    }


    public String getCam_Dx_Puid() {
        return Cam_Dx_Puid;
    }

    public void setCam_Dx_Puid(String cam_Dx_Puid) {
        Cam_Dx_Puid = cam_Dx_Puid;
    }



    public String getCamId() {
        return camId;
    }

    public void setCamId(String camId) {
        this.camId = camId;
    }

    public String getCamName() {
        return camName;
    }

    public void setCamName(String camName) {
        this.camName = camName;
    }
    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmSysCode() {
        return mSysCode;
    }

    public void setmSysCode(String mSysCode) {
        this.mSysCode = mSysCode;
    }

    public String getmIp() {
        return mIp;
    }

    public void setmIp(String mIp) {
        this.mIp = mIp;
    }

    public String getmPort() {
        return mPort;
    }

    public void setmPort(String mPort) {
        this.mPort = mPort;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    @Override
    public String toString() {
        return "IVMS_8700_Bean{" +
                "mUserName='" + mUserName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mSysCode='" + mSysCode + '\'' +
                ", mIp='" + mIp + '\'' +
                ", mPort='" + mPort + '\'' +
                ", mType='" + mType + '\'' +
                '}';
    }
}
