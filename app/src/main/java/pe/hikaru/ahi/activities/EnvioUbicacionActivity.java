package pe.hikaru.ahi.activities;

import pe.hikaru.ahi.bean.Ubicacion;
import pe.hikaru.ahi.bean.Viaje;
import pe.hikaru.ahi.util.JSONHttpClient;
import pe.hikaru.ahi.util.ServiceUrl;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EnvioUbicacionActivity extends Activity {

	Button btnFinalizarEnvio;
	boolean enviando = false;
	int idviaje = 0;
	int seg = 0;
	//INICIO
	LocationManager locManager;
	LocationListener locListener;
	Location loca;
	//FIN

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.envio_ubicacion);
		inicializa();
		Intent intent = getIntent();
		idviaje = intent.getIntExtra(Viaje.VIAJE_ID, 0);
		seg = intent.getIntExtra("seg", 2);
		//INICIO
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//loca = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		locListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				loca = location;
				if (!enviando)
					new guardarUbicacion().execute();
			}
			public void onProviderDisabled(String provider) {
			}
			public void onProviderEnabled(String provider) {
			}
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
		};
		if (Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return  ;
		}
		if (locManager != null) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, seg * 1000, 0, locListener);
			//locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, seg * 1000, 0, locListener);
		}
	}

	private void inicializa() {
		btnFinalizarEnvio = (Button) findViewById(R.id.btnFinalizarEnvio);
		btnFinalizarEnvio.setOnClickListener(btnFinalizarEnvioClick);
	}

	private View.OnClickListener btnFinalizarEnvioClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
			if (Build.VERSION.SDK_INT >= 23 &&
					ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
					ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				return  ;
			}
			if (locManager != null) {
				locManager.removeUpdates(locListener);
				locManager = null;
			}
		}
	};
	
	class guardarUbicacion extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String s) {
			if (s.equals("0"))
				Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getApplicationContext(), "Posición enviada", Toast.LENGTH_SHORT).show();
			enviando = false;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			enviando = true;
			Ubicacion o = new Ubicacion();
			o.setIdviaje(idviaje);
			o.setLatitud(String.valueOf(loca.getLatitude()));
			o.setLongitud(String.valueOf(loca.getLongitude()));
			JSONHttpClient jsonHttpClient = new JSONHttpClient();
			o = (Ubicacion) jsonHttpClient.PostObject(ServiceUrl.UBICACION, o, Ubicacion.class);
			if (o == null)
	            return "0";
			return o.getIdviaje() + "";
		}
	}

}
