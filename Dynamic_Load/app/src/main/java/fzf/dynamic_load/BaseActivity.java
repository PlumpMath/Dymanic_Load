package fzf.dynamic_load;

import java.io.File;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.Log;
/**
 * Created by root on 16-8-22.
 */
public class BaseActivity extends Activity {

    protected AssetManager mAssetManager;//×ÊÔ´¹ÜÀíÆ÷
    protected  Resources mResources;//×ÊÔ´
    protected Theme mTheme;//Ö÷Ìâ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void loadResources(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            Log.i("inject", "loadResource error:"+Log.getStackTraceString(e));
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),superRes.getConfiguration());
       // Log.d("FZF","Don't have 0x7f04001a!!!!!!!!:"+mResources.getResourceName(0x7f04001a));
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        Log.d("FZF","Get ourResources");
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Theme getTheme() {
        Log.d("FZF","Get ourTheme");
        return mTheme == null ? super.getTheme() : mTheme;
    }

}
