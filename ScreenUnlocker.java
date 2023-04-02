import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the device policy manager and the admin component
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyAdmin.class);

        // Check if the app has been granted device admin privileges
        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "This app needs device admin privileges to unlock the screen.");
            startActivityForResult(intent, 0);
        } else {
            // The app has already been granted device admin privileges
            devicePolicyManager.lockNow();
            finish();
        }
    }

    // Handle the result of the device admin request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // The user granted device admin privileges
                devicePolicyManager.lockNow();
                finish();
            } else {
                // The user didn't grant device admin privileges
                Toast.makeText(this, "This app needs device admin privileges to unlock the screen.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
