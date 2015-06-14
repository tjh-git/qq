package comm.langsin.msg_log_reg;

public class MsgRegResp extends MsgHead{
	private byte State;
	@Override
	public String toString() {
		return super.toString()+"MsgRegResp [State=" + State + "]";
	}

	public byte getState() {
		return State;
	}

	public void setState(byte state) {
		State = state;
	}
	
}
