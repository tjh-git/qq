package comm.langsin.protocol;

public interface Protocol {// 通信协议
	public static String ServerIp = "localhost";
	public static int ServerPort = 60000;
	public static int ServerNUMBER = 9999;
	public static byte common_log = 0x00;// 登录请求消息
	public static byte common_log_resp = 0x01;// 登录应答消息

	public static byte common_reg = 0x02;// 注册请求消息
	public static byte common_reg_resp = 0x03;// 注册应答消息

	public static byte common_chatmsg = 0x04;// 单聊消息
	public static byte common_chatfile = 0x05;// 传文件

	public static byte common_finduser = 0x06;// 查找用户
	public static byte common_finduser_resp = 0x07;// 查找用户应答

	public static byte common_findpacket = 0x08;// 查找群组
	public static byte common_findpacket_resp = 0x09;// 查找群组应答消息

	public static byte common_createpacket = 0x10;// 创建群组
	public static byte common_createpacket_resp = 0x11;// 创建群组应答消息

	public static byte common_addfriend = 0x12;// 添加好友请求消息
	public static byte common_addfriend_resp = 0x13;// 添加好友应答消息

	public static byte common_friendlist = 0x14;// 好友列表

	public static byte common_friendmsg = 0x15;// 查看用户资料请求消息
	public static byte common_friendmsg_resp = 0x16;// 查看用户资料应答消息

	public static byte common_nostatic = 0x17;// 抖动窗体

	public static byte common_addgroup = 0x18;// 添加群组请求消息
	public static byte common_addgroupresp = 0x19;// 添加群组应答消息

	public static byte common_grouplist = 0x20;// 群组列表

	public static byte common_getmember = 0x21;// 获取群成员请求消息
	public static byte common_getmember_resp = 0x21;// 获取群成员应答消息

	public static byte common_chatgroupmsg = 0x22;// 群聊
	public static byte common_chatgroupfile = 0x23;// 群文件

	public static byte common_voice = 0x24;// 语音请求
	public static byte common_voice_resp = 0x25;// 语音应答消息

	public static byte common_remove = 0x26;// 删除好友
	public static byte commpn_remove_group = 0x27;// 主动退群

	public static byte common_online = 0x28;// 上线通知
	public static byte common_offline = 0x29;// 上线通知

	public static byte common_sendgroupfile = 0x30;// 发送群文件

	public static byte common_receivefile = 0x31;// 接受群文件

	public static byte common_removemeber = 0x32;// 删除群成员

	public static byte common_findmember = 0x33;// 查找成员

	public static byte common_addmember = 0x34;// 添加成员
	public static byte common_addmemberresp = 0x35;// 添加群成员应答消息
}
