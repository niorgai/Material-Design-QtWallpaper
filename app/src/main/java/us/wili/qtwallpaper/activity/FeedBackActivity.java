package us.wili.qtwallpaper.activity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.utils.ToastUtil;

/**
 * 意见反馈
 * Created by qiu on 2/1/16.
 */
public class FeedBackActivity extends BaseActivity {

    private EditText mContent;
    private EditText mContact;

    private FeedbackAgent agent;

    @Override
    protected void initViews() {
        super.initViews();
        setTitle(R.string.feedback);
        setContentView(R.layout.activity_feed_back);
        mContent = (EditText) findViewById(R.id.feed_back_content);
        mContact = (EditText) findViewById(R.id.feed_back_contact);
    }

    @Override
    protected void initData() {
        super.initData();
        agent = new FeedbackAgent(this);
        UserInfo userInfo = agent.getUserInfo();
        if (userInfo != null) {
            Map<String, String> info = userInfo.getContact();
            if (info != null) {
                mContact.setText(info.get("phone"));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed_back, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.submit) {
            submitFeedBack(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitFeedBack(MenuItem item) {
        String content = mContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.getInstance().showToast(R.string.please_input_content);
            return;
        }
        String contact = mContact.getText().toString();
        if (TextUtils.isEmpty(contact)) {
            ToastUtil.getInstance().showToast(R.string.please_input_contact);
            return;
        }
        item.setEnabled(false);
        UserInfo userInfo = agent.getUserInfo();
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        Map<String, String> info = userInfo.getContact();
        if (info == null) {
            info = new HashMap<>();
        }
        info.put("phone", contact);
        userInfo.setContact(info);
        agent.setUserInfo(userInfo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                agent.updateUserInfo();
            }
        }).start();
        agent.getDefaultConversation().addUserReply(content);
        agent.getDefaultConversation().sync(new SyncListener() {
            @Override
            public void onReceiveDevReply(List<Reply> list) {

            }

            @Override
            public void onSendUserReply(List<Reply> list) {
                ToastUtil.getInstance().showToast(R.string.submit_feed_back_success);
                FeedBackActivity.this.finish();
            }
        });
    }
}
