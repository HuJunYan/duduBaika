package com.dudubaika.model.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjy on 16/9/16.
 */
public class JpushBaseBean {
    public String msg_type; // 消息类型
    public MsgContent msg_content; // 消息内容


    public class MsgContent implements Parcelable {
        public String msg_id;
        public String msg_url;
        public String msg_title;


        protected MsgContent(Parcel in) {
            msg_id = in.readString();
            msg_url = in.readString();
            msg_title = in.readString();

        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(msg_url);
            dest.writeString(msg_id);
            dest.writeString(msg_title);
        }

        @Override
        public String toString() {
            return "MsgContent{" +
                    "msg_id='" + msg_id + '\'' +
                    ", msg_url='" + msg_url + '\'' +
                    ", msg_title='" + msg_title + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public  final Creator<MsgContent> CREATOR = new Creator<MsgContent>() {
            @Override
            public MsgContent createFromParcel(Parcel in) {
                return new MsgContent(in);
            }

            @Override
            public MsgContent[] newArray(int size) {
                return new MsgContent[size];
            }
        };

    }

}
