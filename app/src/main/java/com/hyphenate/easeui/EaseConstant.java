/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui;

public class EaseConstant {
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";

    //红包
    public static final String MESSAGE_IS_RED_PACKAGE = "message_is_red_package";
    public static final String MESSAGE_RED_USER_ID = "userId";//userId
    public static final String MESSAGE_RED_ICON_URL = "iconUrl";//图片地址
    public static final String MESSAGE_RED_NICK_NAME = "nickName";//昵称
    public static final String MESSAGE_RED_PACKAGE_ID = "packetId";//红包Id


    //审批通知
    public static final String MESSAGE_ATTR_IS_APPROVAL = "em_is_approval";
    public static final String MESSAGE_ATTR_APPROVAL_ID = "em_approval_id";

    //抢红包通知
    public static final String MESSAGE_ATTR_IS_HINT = "em_is_hint";
    public static final String MESSAGE_HINT_ID = "hintId";//红包提示id
    public static final String MESSAGE_HINT_GROUP_ID = "groupId";//红包提示群组id
    public static final String MESSAGE_HINT_MESSAGE = "message";
    public static final String MESSAGE_HINT_PACKET_ID = "packetId";

    //群通知
    public static final String MESSAGE_ATTR_IS_GROUP_HINT = "is_group_hint";
    public static final String MESSAGE_ATTR_GROUP_HINT_ID = "group_hint_id";
    public static final String MESSAGE_ATTR_GROUP_HINT_APPLY_ID = "group_apply_id";
    public static final String MESSAGE_HINT_GROUP_MESSAGE = "group_message";
    public static final String MESSAGE_HINT_GROUP_ID_MANAGER = "group_id";
    public static final String MESSAGE_HINT_GROUP_MESSAGE_TYPE = "group_message_hint_type";

    public static final String MESSAGE_ATTR_AT_MSG = "em_at_list";
    public static final String MESSAGE_ATTR_VALUE_AT_MSG_ALL = "ALL";


    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";
}
