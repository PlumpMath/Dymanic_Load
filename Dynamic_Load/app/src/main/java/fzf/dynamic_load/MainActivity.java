package fzf.dynamic_load;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends BaseActivity {
    @Override
    protected void attachBaseContext(Context context) {
        //replaceContextResources(context);
        super.attachBaseContext(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button click=(Button)findViewById(R.id.button);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    click();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void click() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        String filesDir=this.getCacheDir().getAbsolutePath();
        Log.d("FZF","filesDir:"+filesDir.toString());
        String libPath=filesDir+ File.separator+"PluginActivity1.apk";
        Log.d("FZF","libPath:"+libPath);
        Log.d("FZF","File is exit?"+new File(libPath).exists());
        String dexOutputDir = getApplicationInfo().dataDir;
        loadResources(libPath);
        DexClassLoader loader=new DexClassLoader(libPath,filesDir,null,getClassLoader());
        //TargetContext targetContext=new TargetContext(this);
       // targetContext.setContext(loader);
        Class clazz=loader.loadClass("fzf.helloworld.MainActivity");
        Class rclazz=loader.loadClass("fzf.helloworld.R$layout");
        Field field=rclazz.getField("activity_main");
        Integer obj=(Integer)field.get(null);
        Log.d("FZF","obj is :"+obj);
        View view= LayoutInflater.from(this).inflate(obj,null);
        Method method =clazz.getMethod("setLayoutView",View.class);
        method.invoke(null,view);
        loadApkClassLoader(loader);
        Intent intent=new Intent(this,clazz);
        startActivity(intent);
    }
    private void loadApkClassLoader(DexClassLoader dloader) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
       Class ActivityThread=Class.forName("android.app.ActivityThread");
        Method currentActivityThread=ActivityThread.getDeclaredMethod("currentActivityThread");
        currentActivityThread.setAccessible(true);
        Object CurrentActivity=currentActivityThread.invoke(null);
        Field Field_mpackage=ActivityThread.getDeclaredField("mPackages");
        Field_mpackage.setAccessible(true);
        ArrayMap mPackages=(ArrayMap)Field_mpackage.get(CurrentActivity);
        String packagename=this.getPackageName();
        WeakReference wr=(WeakReference) mPackages.get(packagename);
        Class obj=Class.forName("android.app.LoadedApk");
        Field mClassLoader=obj.getDeclaredField("mClassLoader");
        mClassLoader.setAccessible(true);
        Log.d("FZF","classloader:"+dloader);
        mClassLoader.set(wr.get(),dloader);

    }
    /*
    public void replaceContextResources(Context context){
        try {
            Field field = context.getClass().getDeclaredField("mResources");
            field.setAccessible(true);
            field.set(context,getResources());
            field.set(context,getTheme());
            System.out.println("debug:repalceResources succ");
        } catch (Exception e) {
            System.out.println("debug:repalceResources error");
            e.printStackTrace();
        }
    }
    */
}
