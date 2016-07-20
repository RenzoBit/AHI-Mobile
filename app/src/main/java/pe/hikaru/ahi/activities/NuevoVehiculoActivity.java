package pe.hikaru.ahi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pe.hikaru.ahi.bean.Vehiculo;
import pe.hikaru.ahi.util.JSONHttpClient;
import pe.hikaru.ahi.util.ServiceUrl;

import java.util.Locale;

public class NuevoVehiculoActivity extends Activity {

	private ProgressDialog progressDialog;
	private EditText txtPlaca;
	private Button btnGuardar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_vehiculo);
		inicializa();
	}

	private void inicializa() {
		txtPlaca = (EditText) findViewById(R.id.txtPlaca);
		btnGuardar = (Button) findViewById(R.id.btnGuardar);
		btnGuardar.setOnClickListener(btnGuardarClick);
	}

	private View.OnClickListener btnGuardarClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (txtPlaca.getText().toString().length() != 7)
				txtPlaca.setError("La placa debe tener 7 caracteres");
			else {
				String placa = txtPlaca.getText().toString().toUpperCase(Locale.ENGLISH);
				new guardarVehiculo().execute(placa);
			}
		}
	};

	class guardarVehiculo extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String s) {
			progressDialog.dismiss();
			if (s.equals("0"))
				Toast.makeText(getApplicationContext(), "No se pudo guardar porque la placa ingresada ya existe o por un error de conexión", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getApplicationContext(), "Vehículo " + s + " guardado con éxito", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(NuevoVehiculoActivity.this);
			progressDialog.setMessage("Guardando vehículo...");
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			Vehiculo o = new Vehiculo();
			String placa = params[0];
			o.setPlaca(placa);
			JSONHttpClient jsonHttpClient = new JSONHttpClient();
			o = (Vehiculo) jsonHttpClient.PostObject(ServiceUrl.VEHICULO, o, Vehiculo.class);
			if (o == null)
				return "0";
			else {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
			return o.getPlaca();
		}
	}
}
