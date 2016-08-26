package fzf.helloworld;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    protected Activity mProxyActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("FZF","Come here !!!");
        setContentView(R.layout.activity_main);
    }

    public void setProxy(Activity proxyActivity){
        mProxyActivity=proxyActivity;
        Log.d("FZF","setProxy is finished!!");
    }

    @Override
    public void setContentView(int layoutResID){
        Log.d("FZF","Ready to setContentView!!");
        if(mProxyActivity!=null && mProxyActivity instanceof Activity){
            Log.d("FZF","LayoutResID is:"+layoutResID);
            mProxyActivity.setContentView(layoutResID);
            mProxyActivity.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mProxyActivity,"HAHAHAHAHA",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Log.d("FZF","Have nothing !!!");
        }
    }

    @Override
    protected void onDestroy() {
    Log.d("FZF","onDestory");
    }

    @Override
    protected void onPause() {
        Log.d("FZF","onPause");
    }

    @Override
    protected void onResume() {
        Log.d("FZF","onResume");
    }

    @Override
    protected void onStart() {
        Log.i("FZF", "onStart");
    }

    @Override
    protected void onStop() {
        Log.i("FZF", "onStop");
    }
}

