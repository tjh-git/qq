package com.langsin.client;

import comm.langsin.msg_log_reg.MsgHead;


//客户端监听消息接口
public interface ClientMsgListener {
	public void FirstMsg(MsgHead msg);//处理接收到的第一条消息对象
}
