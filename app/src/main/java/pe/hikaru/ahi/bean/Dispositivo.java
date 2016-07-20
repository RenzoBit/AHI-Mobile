package pe.hikaru.ahi.bean;

public class Dispositivo {

	private int iddispositivo;
	private String mac;
    public static final String DISPOSITIVO_ID = "iddispositivo";
    public static final String DISPOSITIVO_NAME = "mac";

	public int getIddispositivo() {
		return iddispositivo;
	}

	public void setIddispositivo(int iddispositivo) {
		this.iddispositivo = iddispositivo;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

}
