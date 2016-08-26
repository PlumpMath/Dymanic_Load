package fzf.dynamic_load;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by root on 16-8-23.
 */
public class MainActivityProxy extends BaseActivityPorxy {
    Class pluginClass;
    private Object pluginActivity;
    Constructor localConstructor;
    Method onCreate;
    private HashMap<String,Method>methodMap=new HashMap<String, Method>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //setup DexClassloader
        String filesDir=this.getCacheDir().getAbsolutePath();
        String libPath=filesDir+ File.separator+"PluginActivity.apk";
        Log.d("FZF","File is exist?:"+new File(libPath).exists());
        try {
            loadResources(libPath);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
     DexClassLoader loader=new DexClassLoader(libPath,filesDir,null,getClass().getClassLoader());
        try {
             pluginClass=loader.loadClass("com.example.dynamicactivity.MainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
             localConstructor=pluginClass.getConstructor(new Class[] {});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            pluginActivity=localConstructor.newInstance(new Object[]{});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            Method setProxy=pluginClass.getMethod("setProxy",new Class[]{Activity.class});
            setProxy.setAccessible(true);
            setProxy.invoke(pluginActivity,new Object[]{this});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            initMethodMap();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            onCreate=pluginClass.getDeclaredMethod("onCreate",new Class[]{Bundle.class});
            onCreate.setAccessible(true);
            onCreate.invoke(pluginActivity,new Object[]{new Bundle()});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    private void initMethodMap() throws NoSuchMethodException {
        methodMap.put("onPause",null);
        methodMap.put("onResume",null);
        methodMap.put("onStop",null);
        methodMap.put("onStart",null);
        methodMap.put("onDestroy",null);
        for(String key : methodMap.keySet()){
            Method method=pluginClass.getDeclaredMethod(key);
            method.setAccessible(true);
            methodMap.put(key,method);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("demo", "proxy onResume");
        try{
            methodMap.get("onResume").invoke(pluginActivity, new Object[]{});
        }catch(Exception e){
            Log.i("demo", "run resume error:"+Log.getStackTraceString(e));
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("demo", "proxy onDestroy");
        try{
            methodMap.get("onDestroy").invoke(pluginActivity, new Object[]{});
        }catch(Exception e){
            Log.i("demo", "run destroy error:"+Log.getStackTraceString(e));
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("demo", "proxy onStart");
        try{
            methodMap.get("onStart").invoke(pluginActivity, new Object[]{});
        }catch(Exception e){
            Log.i("demo", "run start error:"+Log.getStackTraceString(e));
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("demo", "proxy onStop");
        try{
            methodMap.get("onStop").invoke(pluginActivity, new Object[]{});
        }catch(Exception e){
            Log.i("demo", "run stop error:"+Log.getStackTraceString(e));
        }
    }
}
