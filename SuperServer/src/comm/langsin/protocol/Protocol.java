package comm.langsin.protocol;

public interface Protocol {// ͨ��Э��
	public static String ServerIp = "localhost";
	public static int ServerPort = 60000;
	public static int ServerNUMBER = 9999;
	public static byte common_log = 0x00;// ��¼������Ϣ
	public static byte common_log_resp = 0x01;// ��¼Ӧ����Ϣ

	public static byte common_reg = 0x02;// ע��������Ϣ
	public static byte common_reg_resp = 0x03;// ע��Ӧ����Ϣ

	public static byte common_chatmsg = 0x04;// ������Ϣ
	public static byte common_chatfile = 0x05;// ���ļ�

	public static byte common_finduser = 0x06;// �����û�
	public static byte common_finduser_resp = 0x07;// �����û�Ӧ��

	public static byte common_findpacket = 0x08;// ����Ⱥ��
	public static byte common_findpacket_resp = 0x09;// ����Ⱥ��Ӧ����Ϣ

	public static byte common_createpacket = 0x10;// ����Ⱥ��
	public static byte common_createpacket_resp = 0x11;// ����Ⱥ��Ӧ����Ϣ

	public static byte common_addfriend = 0x12;// ��Ӻ���������Ϣ
	public static byte common_addfriend_resp = 0x13;// ��Ӻ���Ӧ����Ϣ

	public static byte common_friendlist = 0x14;// �����б�

	public static byte common_friendmsg = 0x15;// �鿴�û�����������Ϣ
	public static byte common_friendmsg_resp = 0x16;// �鿴�û�����Ӧ����Ϣ

	public static byte common_nostatic = 0x17;// ��������

	public static byte common_addgroup = 0x18;// ���Ⱥ��������Ϣ
	public static byte common_addgroupresp = 0x19;// ���Ⱥ��Ӧ����Ϣ

	public static byte common_grouplist = 0x20;// Ⱥ���б�

	public static byte common_getmember = 0x21;// ��ȡȺ��Ա������Ϣ
	public static byte common_getmember_resp = 0x21;// ��ȡȺ��ԱӦ����Ϣ

	public static byte common_chatgroupmsg = 0x22;// Ⱥ��
	public static byte common_chatgroupfile = 0x23;// Ⱥ�ļ�

	public static byte common_voice = 0x24;// ��������
	public static byte common_voice_resp = 0x25;// ����Ӧ����Ϣ

	public static byte common_remove = 0x26;// ɾ������
	public static byte commpn_remove_group = 0x27;// ������Ⱥ

	public static byte common_online = 0x28;// ����֪ͨ
	public static byte common_offline = 0x29;// ����֪ͨ

	public static byte common_sendgroupfile = 0x30;// ����Ⱥ�ļ�

	public static byte common_receivefile = 0x31;// ����Ⱥ�ļ�

	public static byte common_removemeber = 0x32;// ɾ��Ⱥ��Ա

	public static byte common_findmember = 0x33;// ���ҳ�Ա

	public static byte common_addmember = 0x34;// ��ӳ�Ա
	public static byte common_addmemberresp = 0x35;// ���Ⱥ��ԱӦ����Ϣ
}
