package pe.hikaru.ahi.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnNuevoViaje, btnReanudarViaje, btnNuevoVehiculo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inicializa();
	}

	private void inicializa() {
		btnNuevoViaje = (Button) findViewById(R.id.btnNuevoViaje);
		btnReanudarViaje = (Button) findViewById(R.id.btnReanudarViaje);
		btnNuevoVehiculo = (Button) findViewById(R.id.btnNuevoVehiculo);
		btnNuevoViaje.setOnClickListener(btnNuevoViajeClick);
		btnReanudarViaje.setOnClickListener(btnReanudarViajeClick);
		btnNuevoVehiculo.setOnClickListener(btnNuevoVehiculoClick);
	}

	private View.OnClickListener btnNuevoViajeClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (obtenerStatusGPS()) {
				if (isOnline()) {
					Intent intent = new Intent(getApplicationContext(), NuevoViajeActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} else
					Toast.makeText(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(getApplicationContext(), "Active el GPS del dispositivo", Toast.LENGTH_SHORT).show();
		}
	};

	private View.OnClickListener btnReanudarViajeClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "En construcción", Toast.LENGTH_SHORT).show();
		}
	};

	private View.OnClickListener btnNuevoVehiculoClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), NuevoVehiculoActivity.class);
			startActivity(intent);
		}
	};

	private boolean obtenerStatusGPS() {
		ContentResolver contentResolver = getBaseContext().getContentResolver();
		return Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
	}

	public boolean isOnline() {
		NetworkInfo netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		return (netInfo != null && netInfo.isConnectedOrConnecting());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		*/
		return super.onOptionsItemSelected(item);
	}
}
