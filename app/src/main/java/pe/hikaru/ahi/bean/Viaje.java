package pe.hikaru.ahi.bean;

public class Viaje {

	private int idviaje;
	private int iddispositivo;
	private int idvehiculo;
	private String descripcion;
	private String horainicio;
	private String horafin;
	private String mac;
	public static final String VIAJE_ID = "idviaje";
	public static final String VIAJE_NAME = "descripcion";

	public int getIdviaje() {
		return idviaje;
	}

	public void setIdviaje(int idviaje) {
		this.idviaje = idviaje;
	}

	public int getIddispositivo() {
		return iddispositivo;
	}

	public void setIddispositivo(int iddispositivo) {
		this.iddispositivo = iddispositivo;
	}

	public int getIdvehiculo() {
		return idvehiculo;
	}

	public void setIdvehiculo(int idvehiculo) {
		this.idvehiculo = idvehiculo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getHorainicio() {
		return horainicio;
	}

	public void setHorainicio(String horainicio) {
		this.horainicio = horainicio;
	}

	public String getHorafin() {
		return horafin;
	}

	public void setHorafin(String horafin) {
		this.horafin = horafin;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

}
