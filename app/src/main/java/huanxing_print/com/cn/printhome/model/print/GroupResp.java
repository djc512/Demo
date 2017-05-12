package huanxing_print.com.cn.printhome.model.print;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/5/12.
 */

public class GroupResp extends CommonResp {

    private List<Group> data;

    public List<Group> getData() {
        return data;
    }

    public void setData(List<Group> data) {
        this.data = data;
    }

    //  "balance":"测试内容3h6b","groupId":"测试内容a4k8","groupName":"测试内容6quq","groupUrl":"测试内容9il2"
    public static class Group {

        private String balance;
        private String groupId;
        private String groupName;
        private String groupUrl;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupUrl() {
            return groupUrl;
        }

        public void setGroupUrl(String groupUrl) {
            this.groupUrl = groupUrl;
        }

        @Override
        public String toString() {
            return "Group{" +
                    "balance='" + balance + '\'' +
                    ", groupId='" + groupId + '\'' +
                    ", groupName='" + groupName + '\'' +
                    ", groupUrl='" + groupUrl + '\'' +
                    '}';
        }
    }


}
