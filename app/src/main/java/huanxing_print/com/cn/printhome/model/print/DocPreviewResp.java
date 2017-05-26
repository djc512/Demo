package huanxing_print.com.cn.printhome.model.print;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/5/10.
 */

public class DocPreviewResp extends CommonResp {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Parcelable {
        private List<String> fileUrlList;
        private int paperNum;

        public List<String> getFileUrlList() {
            return fileUrlList;
        }

        public ArrayList<String> getArryList() {
            if (fileUrlList == null) {
                return null;
            }
            ArrayList<String> list = new ArrayList<>();
            for (String str : fileUrlList) {
                list.add(str);
            }
            return list;
        }

        public void setFileUrlList(List<String> fileUrlList) {
            this.fileUrlList = fileUrlList;
        }

        public int getPaperNum() {
            return paperNum;
        }

        public void setPaperNum(int paperNum) {
            this.paperNum = paperNum;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringList(this.fileUrlList);
            dest.writeInt(this.paperNum);
        }

        public Data() {
        }

        protected Data(Parcel in) {
            this.fileUrlList = in.createStringArrayList();
            this.paperNum = in.readInt();
        }

        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
    }
}

