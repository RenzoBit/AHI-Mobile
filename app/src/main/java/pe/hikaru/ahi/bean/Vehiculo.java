package pe.hikaru.ahi.bean;

public class Vehiculo {

	private int idvehiculo;
	private String placa;
    public static final String VEHICULO_ID = "idvehiculo";
    public static final String VEHICULO_NAME = "placa";

	public int getIdvehiculo() {
		return idvehiculo;
	}

	public void setIdvehiculo(int idvehiculo) {
		this.idvehiculo = idvehiculo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

}
