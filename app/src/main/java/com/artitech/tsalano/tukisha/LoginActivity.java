package com.artitech.tsalano.tukisha;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.errorhandling.LoginException;
import com.artitech.tsalano.tukisha.model.AgentModel;
import com.loopj.android.http.HttpGet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    TukishaApplication tukishaApplication;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private AgentModel agent = null;
    // UI references.
    private EditText mAgentidView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mAgentidView = (EditText) findViewById(R.id.agentid);

        tukishaApplication = (TukishaApplication) getApplication();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mAgentidView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String agentid = mAgentidView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(agentid)) {
            mAgentidView.setError(getString(R.string.error_field_required));
            focusView = mAgentidView;
            cancel = true;
        }

        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(agentid, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mAgentId;
        private final String mPassword;
        private Boolean isValid;

        UserLoginTask(String agentid, String password) {
            mAgentId = agentid;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpGet httpget = new HttpGet("https://munipoiapp.herokuapp.com/api/app/agentlogin?agentid=" + mAgentId + "&password=" + mPassword);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            try {

                response = httpclient.execute(httpget);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {

                    HttpEntity entity = response.getEntity();
                    String responsedata = EntityUtils.toString(entity);

                    JSONObject responseAgent = new JSONObject(responsedata);

                    if (responseAgent.getString("status").contains("invalid"))
                        throw new LoginException(responseAgent.getString("status"));

                    agent = new AgentModel(responseAgent.getString("balance"), responseAgent.getString("status"));

                    isValid = agent.getStatus().equals("success");

                    Log.d("Agent", isValid.toString());

                } else {
                    if (status == 404) {
                        throw new BackendDownException("System is down, Please try again later or Call this number 078899999");
                    }
                }

                Log.d("Error", response.getStatusLine().toString());


            } catch (final java.net.UnknownHostException e) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        mPasswordView.setError("Please check the following \n1. If network is active and  \n2. Data is enabled");
                        mPasswordView.requestFocus();
                    }
                });

                isValid = false;

            }
            catch (final IOException e) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        mPasswordView.setError("Technical error occurred " + e.getMessage());
                        mPasswordView.requestFocus();
                    }
                });

                isValid = false;

            } catch (final JSONException e) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        mPasswordView.setError("Technical error occurred:" + e.getMessage());
                        mPasswordView.requestFocus();
                    }
                });


            } catch (final LoginException e) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        // update your UI component here.
                        mPasswordView.setError(e.getMessage());
                        mPasswordView.requestFocus();
                    }
                });

                isValid = false;

            } catch (final BackendDownException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // update your UI component here.
                        mPasswordView.setError(e.getMessage());
                        mPasswordView.requestFocus();
                    }
                });

                isValid = false;
            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // update your UI component here.
                        mPasswordView.setError("Something went horribly wrong. Please restart the app.");
                        mPasswordView.requestFocus();
                    }
                });

                isValid = false;
            }

            // TODO: register the new account here.
            return isValid;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {


                //               TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//                String device_id = tm.getDeviceId();

//                Log.d("Device id is ", device_id);

                try {

                    tukishaApplication.setBalance(agent.getAgentBalance());
                    tukishaApplication.setAgentID(mAgentId);

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("balance", tukishaApplication.getBalance());
                    i.putExtra("status", agent.getStatus());
                    i.putExtra("agentid", tukishaApplication.getAgentID());
                    startActivity(i);
                    finish();

                } catch (Exception ex) {
                    mPasswordView.setError("Something went horrible wrong. Please restart the app. " + ex.getMessage());
                    mPasswordView.requestFocus();
                }


                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }
}

