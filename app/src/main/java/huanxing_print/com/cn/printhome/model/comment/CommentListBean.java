package huanxing_print.com.cn.printhome.model.comment;


import java.util.List;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class CommentListBean {

    /**
     * address : 防守打法似懂非懂
     * bad : 2
     * comm : 0
     * detail : [{"imageList":{},"dateTime":"2017-03-08","nickName":"呵**呵","remark":"好用的不要不要的42343","totalScore":5},{"imageList":{},"dateTime":"2017-03-08","nickName":"呵**呵","remark":"好用的不要不要的12","totalScore":5},{"imageList":{},"dateTime":"2017-03-08","nickName":"呵**呵","remark":"好用的不要不要的","totalScore":5},{"imageList":{},"dateTime":"2017-03-08","nickName":"呵**呵","remark":"非常好用","totalScore":5}]
     * good : 2
     */

    private String address;
    private int bad;
    private int comm;
    private int good;
    private List<DetailBean> detail;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBad() {
        return bad;
    }

    public void setBad(int bad) {
        this.bad = bad;
    }

    public int getComm() {
        return comm;
    }

    public void setComm(int comm) {
        this.comm = comm;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public static class DetailBean {
        public List<String> getImageList() {
            return imageList;
        }

        /**
         * imageList : {}
         * dateTime : 2017-03-08
         * nickName : 呵**呵
         * remark : 好用的不要不要的42343
         * totalScore : 5
         */

        private List<String> imageList;
        private String dateTime;
        private String nickName;
        private String remark;
        private int totalScore;

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(int totalScore) {
            this.totalScore = totalScore;
        }
    }
}
