package cn.xsjky.android.model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by OK on 2016/3/16.
 *
 <?xml version="1.0" encoding="utf-8"?>
 <GetValueResultOfLoginSession xmlns="http://www.xsjky.cn/">
 <Value>
 <UserId>int</UserId>
 <ClientName>string</ClientName>
 <SessionId>guid</SessionId>
 <RoleData>int</RoleData>
 </Value>
 </GetValueResultOfLoginSession>
 */
@Table(name="tbl_User")
public class LoginInfo implements Parcelable ,KvmSerializable {
 @Id
 private int UserId;
 @Column(column="ClientName")
 @Unique
 private String ClientName;
 @Column(column="SessionId")
 @Unique
 private String SessionId;
 @Column(column="RoleData")
 @Unique
 private int RoleData;

 protected LoginInfo(Parcel in) {
  UserId = in.readInt();
  ClientName = in.readString();
  SessionId = in.readString();
  RoleData = in.readInt();
 }
 public LoginInfo(){}
 public static final Creator<LoginInfo> CREATOR = new Creator<LoginInfo>() {
  @Override
  public LoginInfo createFromParcel(Parcel in) {
   LoginInfo loginInfo = new LoginInfo();
   loginInfo.UserId=in.readInt();
   loginInfo.SessionId= in.readString();
   loginInfo.RoleData=in.readInt();
   loginInfo.ClientName=in.readString();
   return loginInfo;
  }

  @Override
  public LoginInfo[] newArray(int size) {
   return new LoginInfo[size];
  }
 };


 public int getUserId() {
  return UserId;
 }

 public void setUserId(int userId) {
  UserId = userId;
 }

 public String getClientName() {
  return ClientName;
 }

 public void setClientName(String clientName) {
  ClientName = clientName;
 }

 public String getSessionId() {
  return SessionId;
 }

 public void setSessionId(String sessionId) {
  SessionId = sessionId;
 }

 public int getRoleData() {
  return RoleData;
 }

 public void setRoleData(int roleData) {
  RoleData = roleData;
 }

 @Override
 public String toString() {
  return "LoginInfo{" +
          "UserId=" + UserId +
          ", ClientName='" + ClientName + '\'' +
          ", SessionId='" + SessionId + '\'' +
          ", RoleData=" + RoleData +
          '}';
 }

 @Override
 public int describeContents() {
  return 0;
 }

 @Override
 public void writeToParcel(Parcel dest, int flags) {
  dest.writeInt(UserId);
  dest.writeString(ClientName);
  dest.writeString(SessionId);
  dest.writeInt(RoleData);
 }

 @Override
 public Object getProperty(int arg0) {
  switch (arg0) {
   case 0:
    return UserId;
   case 1:
    return ClientName;
   case 2:
    return SessionId;
   case 3:
    return RoleData;
   default:
    break;
  }
  return null;
 }

 @Override
 public int getPropertyCount() {
  return 4;
 }

 @Override
 public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
  switch (arg0) {
   case 0:
    arg2.type = PropertyInfo.STRING_CLASS;
    arg2.name = "UserId";
    break;
   case 1:
    arg2.type = PropertyInfo.STRING_CLASS;
    arg2.name = "ClientName";
    break;
   case 2:
    arg2.type = PropertyInfo.STRING_CLASS;
    arg2.name = "SessionId";
    break;
   case 3:
    arg2.type = PropertyInfo.STRING_CLASS;
    arg2.name = "RoleData";
    break;
   default:
    break;
  }
 }

 @Override
 public void setProperty(int arg0, Object arg1) {
  switch (arg0) {
   case 0:
    UserId = Integer.valueOf(arg1.toString());
    break;
   case 1:
    ClientName = arg1.toString();
    break;
   case 2:
    SessionId =  arg1.toString();
    break;
   case 3:
    RoleData =  Integer.valueOf(arg1.toString());
    break;
   default:
    break;
  }

 }

}
