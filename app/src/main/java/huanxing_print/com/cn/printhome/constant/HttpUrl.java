package huanxing_print.com.cn.printhome.constant;

public class HttpUrl {

    public static final String test = "http://106.14.77.102:22012/";//测试的，正式环境替换

    public static final String POSTHTTP_DAILY = "http://appprint.inkin.cc/";// 线下

   // public static final String POSTHTTP_RELEASE = "http://appprint.inkin.cc/";// 测试
    public static final String POSTHTTP_RELEASE = "http://appprint.oao2o.com/";// 正式

    public static final String POSTHTM5HTTP_RELEASE = "http://print.oao2o.com";// 正式

    public static final String POSTHTM5HTTP_DAILY = "http://print.inkin.cc";// 测试

    public static final String login = "member/login";// 登录
    public static final String loginWeiXin = "member/loginByWechat";// 微信登录
    public static final String bindWechat = "member/bindWechat";// 绑定微信
    public static final String LoginOut = "member/signOut";// 退出登录
    public static final String register = "member/sign";// 注册
    public static final String getVeryCode = "common/getValidCode"; // 获取短信验证码
    public static final String resetPasswd = "member/resetPasswd"; // 重置密码
    public static final String fileUpload = "common/fileUpload"; // 文件上传
    public static final String versionCheck = "common/versionCheck"; // 版本检查
    public static final String appDownLoad = "common/app/download"; // 微信分享地址
    public static final String userInfo = "member/getMemberInfo"; // 用户信息
    public static final String updateInfo = "member/updateMember"; // 修改用户信息
    public static final String feedBack = "feedback/add"; // 反馈信息
    public static final String chongzhi = "pay/recharge/queryConfig"; // 充值接口
    public static final String totlebanlance = "member/getBalance"; // 余额查询
    public static final String myinfo = "member/getMemberInfo"; //个人资料查询
    public static final String czRecord = "pay/recharge/queryOrder"; // 充值记录接口
    public static final String normalDebit = "pay/bill/addCommBill"; // 普通发票接口
    public static final String valueDebit = "pay/bill/addVATBill"; // 增值发票接口
    public static final String getAreaInfo = "common/getAreaInfo";// 省市地区查询
    public static final String mxDetail = "/pay/queryConsume"; // 账单明细接口
    public static final String payOrderId = "pay/recharge/addOrder"; // 获取充值订单号接口
    public static final String doPay = "pay/doAlipay"; // 跳转支付宝的接口
    public static final String doWeChat = "pay/doWxpay"; // 跳转微信的接口
    public static final String dyList = "order/getPrintHistory"; //打印订单列表接口
    public static final String printerList = "print/printer/history"; // 最近使用的打印机列表
    public static final String setDefaultprinter = "print/printer/setDefault"; // 设置默认打印机
    public static final String go2Debit = "pay/bill/getBillAmount"; // 获取能否开发票的接口
    public static final String orderDetail = "order/getOrderDetail"; // 查询订单详情
    public static final String orderList = "order/getPrintHistory"; // 订单列表
    public static final String queryOrderDetail = "pay/recharge/queryOrderDetail"; // 充值订单详情查询
    public static final String queryFriendList = "friend/list";//好友列表
    public static final String friendSearch = "friend/search";//搜索联系人
    public static final String friendSearchAdd = "friend/add";//添加联系人
    public static final String newFriend = "friend/newFriList";//新的朋友
    public static final String operationNewFriend = "friend/adult";//处理加好友请求
    public static final String checkTel = "friend/checkTelNo";//查询手机号是否是印家号
    public static final String groupList = "group/list";//群列表
    public static final String createGroup = "group/add";//添加群
    public static final String queryGroupMsg = "group/queryInfo";//群信息查询
    public static final String addMemberToGroup = "group/addMember";//群信息查询
    public static final String delMemberFromGroup = "group/delMember";//群信息查询
    public static final String exitGroup = "group/getOut";//退群
    public static final String transferGroup = "group/transferOwner";//群转让
    public static final String dissolutionGroup = "group/release";//群解散
    public static final String modifyGroup = "group/updateInfo";//群解散
    public static final String addApproval = "approve/add";//新增审批
    public static final String queryApprovalList = "approve/list";//查询审批列表
    public static final String queryApprovalDetail = "approve/detail";//查询审批详情
    public static final String queryLast = "approve/queryLast";//查询上次的审批人和抄送人
    public static final String approval = "approve/adult";//审批
    public static final String proof = "approve/proof";//查看凭证
    public static final String queryCount = "approve/queryCount";//查看未读消息
    public static final String approvalRevoke = "approve/revocation";//审批撤销
    public static final String submitComment = "appraise/add";//添加评论
    public static final String getCommentList = "appraise/list";//评论列表
    public static final String batchFileUpload = "common/batchFileUpload";//批量上传
    public static final String queryMessageStatus = "message/queryStatus";//查询应用消息提醒状态
    public static final String sendRedPackage = "redPackage/person/send";//发送红包
    public static final String receiveRedPackage = "redPackage/person/receive";//收红包
    public static final String queryPackageDetail = "redPackage/person/queryDetail";//查看红包记录
    public static final String sendGroupPackage = "redPackage/group/sendCommon";//发群红包
    public static final String sendLuckyGroupPackage = "redPackage/group/sendLucky";//发拼手气群红包
    public static final String robRedPackage = "redPackage/group/receive";//抢红包
    public static final String checkLuckyGroupPackageDetail = "redPackage/group/queryLuckyDetail";//查看手气群红包记录
    public static final String checkGroupPackageDetail = "redPackage/group/queryCommDetail";//查看普通群红包记录
    public static final String getMemberHead = "chat/getMemberHead";//查询用户头像名称
    public static final String getGroupHead = "chat/getGroupHead";//查询群头像名称
    public static final String groupManagerApproval = "group/adult";//查询群头像名称


    public static final String community ="/#/bbs";// 印家社区
    public static final String myLay ="/offerManage/lay/";//布点建议
    public static final String myEarn ="/offerManage/earn/";// 打印点加盟

    private static String postUrl;

    private static String htmUrl;

    private static HttpUrl httpUrl;

    public static String getPostUrl() {
        return postUrl;
    }

    public static void setPostUrl(String postUrl) {
        HttpUrl.postUrl = postUrl;
    }

    public static String getHtmUrl() {
        return htmUrl;
    }

    public static void setHtmUrl(String htmUrl) {
        HttpUrl.htmUrl = htmUrl;
    }

    public static HttpUrl getInstance() {
        if (null != httpUrl) {
            httpUrl = new HttpUrl();
        }
        initEnvironment();
        return httpUrl;

    }

    private static void initEnvironment() {
        switch (ConFig.CURRENT_ENVIRONMENT) {
            case RELEASE:
                setPostUrl(POSTHTTP_RELEASE);
                setHtmUrl(POSTHTM5HTTP_RELEASE);
                break;
            case DAILY:
                setPostUrl(POSTHTTP_DAILY);
                setHtmUrl(POSTHTM5HTTP_DAILY);
                break;
            default:
                break;
        }
    }

}
