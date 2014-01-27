package com.example.ifoxdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.arzen.ifox.iFox;
import com.arzen.ifox.iFox.ChargeListener;
import com.arzen.ifox.iFox.InitCallBack;
import com.arzen.ifox.iFox.LoginListener;
import com.arzen.ifox.iFox.OnCommitScoreCallBack;
import com.arzen.ifox.setting.KeyConstants;
import com.arzen.ifox.utils.MsgUtil;

public class MainActivity extends Activity {
	
	private Button mBtnPay;
	private Button mBtnLogin;
	private Button mBtnTop;
	private Button mBtnCommit;
	private Button mBtnShare;
	/**
	 * 申请的key
	 */
	private String mKey  = "313121500165700120";
	/**
	 * 申请的Secrect
	 */
	private String mAppSecrect = "db0938b04d97466b8a5f7ececff681a9052ac8479";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mBtnPay = (Button) findViewById(R.id.btnPay);
		mBtnLogin = (Button) findViewById(R.id.btnLogin);
		mBtnTop = (Button) findViewById(R.id.btnTop);
		mBtnCommit = (Button) findViewById(R.id.btnCommit);
		mBtnShare = (Button) findViewById(R.id.btnShare);
		
		
		mBtnTop.setOnClickListener(mOnClickListener);
		mBtnCommit.setOnClickListener(mOnClickListener);
		mBtnLogin.setOnClickListener(mOnClickListener);
		mBtnPay.setOnClickListener(mOnClickListener);
		mBtnShare.setOnClickListener(mOnClickListener);

		/**
		 * 初始化时必须调用Ifox.init()方法
		 * 当回调成功后才能继续后续工作
		 */
		iFox.init(this, mKey, mAppSecrect,new InitCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				MsgUtil.msg("初始化成功", MainActivity.this);
			}
			
			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub
				MsgUtil.msg("初始化失败:" + msg, MainActivity.this);
			}
		});
	
	}
	
	public OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.btnPay:
				toPay();
				break;
			case R.id.btnLogin:
				toLogin();
				break;
			case R.id.btnTop:
				iFox.leaderboardPage(MainActivity.this);
				break;
			case R.id.btnCommit:
				submitScore();
				break;
			case R.id.btnShare:
				toShare();
				break;
			}
		}
	};
	/**
	 * 跳转支付页
	 * 其中需要传以下参数
	 * pid: 购买的道具号 (必传,不能为0)
	 * extra: 需要保存到服务器的附加信息 (非必须) 字符串类型,可上传任意需要保存的信息
	 */
	public void toPay()
	{
		Bundle bundle = new Bundle();
		bundle.putString(KeyConstants.INTENT_DATA_KEY_EXTRA, "server:39,name=Jack"); //附加信息
		bundle.putInt(KeyConstants.INTENT_DATA_KEY_PID, 333);  //道具号
		//支付固定金额 , 选填
//		bundle.putFloat(KeyConstants.INTENT_DATA_KEY_AMOUNT, 10F);
		
		iFox.chargePage(MainActivity.this, bundle, new ChargeListener() {
			
			/**
			 * 支付成功后会回调
			 * 道具号:pid
			 * 订单号:orderId
			 * 价格: amout
			 * 可根据回调的相应信息做相应的处理
			 */
			@Override
			public void onSuccess(Bundle bundle) {
				// TODO Auto-generated method stub
				//商品id
				int pid = bundle.getInt(KeyConstants.INTENT_DATA_KEY_PID);
				String orderId = bundle.getString(KeyConstants.INTENT_DATA_KEY_ORDERID);//订单id
				float amount = bundle.getFloat(KeyConstants.INTENT_DATA_KEY_AMOUNT); //价钱
				
				MsgUtil.msg("支付成功 ! 商品id:"+ pid + " 订单id:" + orderId + " 价格:" + amount, MainActivity.this);
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				MsgUtil.msg("onFinish()", MainActivity.this);
			}
			/**
			 * 支付失败
			 */
			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub
				MsgUtil.msg("支付失败:" + msg, MainActivity.this);
			}
			/**
			 * 支付取消
			 */
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				MsgUtil.msg("支付取消", MainActivity.this);
			}
		});
	}
	
	/**
	 * 跳转登录页面
	 * 需要传的参数暂时传null
	 * 当成功登录过一次后,下次调用,会启动自动登录
	 */
	public void toLogin()
	{
		iFox.loginPage(MainActivity.this, null, new LoginListener() {
			
			/**
			 * 登录成功后会回调登录成功后的token,可做相应的操作
			 */
			@Override
			public void onSuccess(Bundle bundle) {
				// TODO Auto-generated method stub
				String token = bundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN);
				String uid = bundle.getString(KeyConstants.INTENT_DATA_KEY_UID);
				MsgUtil.msg("login onSuccess! token is " + token + " uid:" + uid, MainActivity.this);
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				MsgUtil.msg("login onCancel()", MainActivity.this);
			}
		});
	}
	/**
	 * 提交分数
	 */
	public void submitScore()
	{
		//需要提交的分数
		long score = 123456;
		//需要提交到的排行榜号码
		int lid = 0;
		
		iFox.submitScore(MainActivity.this, score,lid, new OnCommitScoreCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				MsgUtil.msg("提交分数成功!", MainActivity.this);
			}
			
			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub
				MsgUtil.msg("提交分数失败:" + msg, MainActivity.this);
			}
		});
	}
	/**
	 * 分享
	 */
	public void toShare()
	{
		iFox.share(this);
	}
}
