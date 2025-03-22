package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class ModelResponseData {
    @SerializedName("g_r_tag")
    @Expose
    private String gRTag;
    @SerializedName("g_b_tag")
    @Expose
    private String gBTag;
    @SerializedName("g_n_tag")
    @Expose
    private String gNTag;
    @SerializedName("g_ao_tag")
    @Expose
    private String gAoTag;
    @SerializedName("g_i_tag")
    @Expose
    private String gITag;
    @SerializedName("t_sec")
    @Expose
    private Integer tSec = 60;
    @SerializedName("a_start")
    @Expose
    private Integer aStart = 0;
    @SerializedName("is_default")
    @Expose
    private Integer isDefault = 0;
    @SerializedName("app_version")
    @Expose
    private Integer appVersion = 0;

    public String getgRTag() {
        return gRTag;
    }

    public void setgRTag(String gRTag) {
        this.gRTag = gRTag;
    }

    public String getgBTag() {
        return gBTag;
    }

    public void setgBTag(String gBTag) {
        this.gBTag = gBTag;
    }

    public String getgNTag() {
        return gNTag;
    }

    public void setgNTag(String gNTag) {
        this.gNTag = gNTag;
    }

    public String getgAoTag() {
        return gAoTag;
    }

    public void setgAoTag(String gAoTag) {
        this.gAoTag = gAoTag;
    }

    public String getgITag() {
        return gITag;
    }

    public void setgITag(String gITag) {
        this.gITag = gITag;
    }

    public Integer gettSec() {
        return tSec;
    }

    public void settSec(Integer tSec) {
        this.tSec = tSec;
    }

    public Integer getaStart() {
        return aStart;
    }

    public void setaStart(Integer aStart) {
        this.aStart = aStart;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }
}