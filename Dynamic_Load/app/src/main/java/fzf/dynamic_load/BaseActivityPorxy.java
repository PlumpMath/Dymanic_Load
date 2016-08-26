package fzf.dynamic_load;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by root on 16-8-23.
 */
public class BaseActivityPorxy extends Activity {
    AssetManager mAssestManager;
    Resources mResources;
    Resources.Theme mTheme;

    protected void loadResources(String pathname) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        AssetManager assetManager=AssetManager.class.newInstance();
        Method addAssetPath=assetManager.getClass().getDeclaredMethod("addAssetPath",String.class);
        addAssetPath.setAccessible(true);
        addAssetPath.invoke(assetManager,pathname);
        mAssestManager=assetManager;

        Resources superRes=super.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        mResources = new Resources(mAssestManager,superRes.getDisplayMetrics(),superRes.getConfiguration());
        mTheme=mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }
    @Override
    public AssetManager getAssets(){
        if(mAssestManager==null){
            return super.getAssets();
        }
        else{
            Log.d("FZF","Get ourAssestManager");
            return mAssestManager;
        }
    }
    @Override
    public Resources getResources(){
        if(mAssestManager==null){
            return super.getResources();
        }
        else{
            Log.d("FZF","Get ourResources");
            return mResources;
        }
    }
    @Override
    public Resources.Theme getTheme(){
     if (mAssestManager==null)
     {
         return super.getTheme();
     }
        else {
         Log.d("FZF","Get ourTheme");
         return mTheme;
     }
    }
}
