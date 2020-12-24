/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.project.mlmpro.R;
import com.project.mlmpro.adapter.SubscriptionAdapter;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.listener.SubscriptionListner;
import com.project.mlmpro.model.SubscriptionModel;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Subscription extends AppCompatActivity implements SubscriptionListner, PaymentResultListener {

    ArrayList<SubscriptionModel> list = new ArrayList<>();
    Toolbar toolbar;
    String[] types = {};
    /**
     * {"1 Month", "3 Month", "6 Month", "1 Year", "3 Year", "5 Year", "10 Year", "Lifetime"}
     */
    String[] amount = {};
    /**
     * {"200", "500", "1000", "2000", "6000", "10000", "20000", "50000"}
     */
    String walletDeductedAmount = null;

    SubscriptionAdapter subscriptionAdapter;
    RecyclerView listView;
    RequestApi api;
    BottomSheetDialog paymentDetails;
    Loader loader;
    Checkout checkout;
    String type =null ;

    SessionHandler sessionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        Checkout.preload(getApplicationContext());
        setTitle(getString(R.string.subscription));
        listView = findViewById(R.id.list_view);
        subscriptionAdapter = new SubscriptionAdapter(list, this, this);
        listView.setAdapter(subscriptionAdapter);

        listView.setLayoutManager(new LinearLayoutManager(this));
        api = new RequestApi(this);
        loader = new Loader(this);
        sessionHandler = new SessionHandler(this);

        checkout = new Checkout();
        checkout.setKeyID(getString(R.string.razp_live_key));

        try{
            type = getIntent().getStringExtra("type");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(type.equals("6")){
            types = new String[]{"1 Month", "3 Month", "6 Month", "1 Year", "3 Year", "5 Year", "10 Year", "Lifetime"};
            amount = new String[]{"300", "700", "1200", "2400", "7000", "12000", "25000", "70000"} ;

        }else if(type.equals("7")){
            types = new String[]{"1 Month", "3 Month", "6 Month", "1 Year", "3 Year", "5 Year", "10 Year", "Lifetime"};
            amount = new String[]{"500", "1000", "1500", "3000", "8000", "20000", "35000", "100000"} ;
        }else{
            types = new String[]{"1 Month", "3 Month", "6 Month", "1 Year", "3 Year", "5 Year", "10 Year", "Lifetime"};
            amount = new String[]{"200", "500", "1000", "2000", "6000", "10000", "20000", "50000"} ;
        }
        createSubscription();


    }

    private void createSubscription() {
        for (int i = 0; i < types.length; i++) {
            SubscriptionModel s = new SubscriptionModel(String.valueOf(i), types[i], amount[i], amount[i]);
            list.add(s);
        }
        subscriptionAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(SubscriptionModel model) {

        //Step 1 get Wallet details
        //And apply maximum 30 % of total amount

        Constant.SUBSCRIPTION_TYPE = model.getId();
        loader.show("Please wait...");
        Log.d("TAG", "onClick: "+model.getId());


        getWalletDetails(model);


    }

    private void getWalletDetails(SubscriptionModel model) {

        api.getRequest(Server.GET_WALLET, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 200) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    String amount = data.getString("walletAmount");
                    if (amount == null || amount.equals("null")) {
                        amount = String.valueOf(0.00);
                    }
                    loader.dismiss();
                    displayPayment(model, amount);


                } else {
                    //Error

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

    }

    private void displayPayment(SubscriptionModel model, String amount) {

        TextView total, subscriptionAmount, walletAmount;
        Button checkOut;
        double totalWallet = Double.valueOf(amount);
        double totalAmount = Double.valueOf(model.getAmount());
        double maximumApplicable = (totalAmount * 30) / 100;
        double appliedValue, totalValue;
        if (totalWallet > maximumApplicable) {
            appliedValue = maximumApplicable;
        } else {
            appliedValue = totalWallet;
        }

        totalValue = totalAmount - appliedValue;
        paymentDetails = new BottomSheetDialog(Subscription.this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.payment_details, null);


        subscriptionAmount = view.findViewById(R.id.subscription_amount);
        walletAmount = view.findViewById(R.id.wallet_Amount);
        total = view.findViewById(R.id.total_amount);
        checkOut = view.findViewById(R.id.checkout);

        walletAmount.setText("Rs." + String.valueOf(appliedValue));
        subscriptionAmount.setText("Rs." + model.getAmount() + ".0");
        total.setText("Rs." + String.valueOf(totalValue));
        paymentDetails.setContentView(view);
        paymentDetails.show();
        walletDeductedAmount = String.valueOf(appliedValue);

        JSONObject object = Constant.CURRENT_POST_DATA;
        try {
            object.put("subscriptionType" , model.getId());
            object.put("total", String.valueOf(totalValue));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        loader.show("Please wait..");


        checkOut.setOnClickListener(v -> {
            openCheckout(object);
        });

    }

    private void openCheckout(JSONObject object) {
//        Log.d("TAG", "openCheckout: " + object);
        loader.show("Please wait");
        api.postRequest(object, response -> {
            loader.dismiss();
            Log.d("RZP", "displayPayment: " + response);
            try {
                int status = response.getInt("status");
                if (status == 200) {
                    JSONObject re = response.getJSONObject("data");
                    Log.d("TAG", "openCheckout: "+re);
                    String RZP_ORDER_ID = re.getString("razorpay_order_id");
                    String _id = re.getString("id");
                    Constant.PAYMENT_ID = _id;
////                    Log.d("TAG", "openCheckout: " + RZP_ORDER_ID);
                    Constant.RZP_ORDER_ID = RZP_ORDER_ID;
                    openRzpPayment(RZP_ORDER_ID, object);
                } else {
                    Log.d("TAG", "openCheckout: Payment failed from backend");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Server.FEAUTRE_CHECKOUT);

    }

    private void openRzpPayment(String rzp_order_id, JSONObject object) throws JSONException {
        double d = object.getDouble("total");
        try {
            JSONObject options = new JSONObject();

            options.put("name", "MLM Pro");
            options.put("description", "Subscription Payment");
            options.put("order_id", rzp_order_id);//from response of step 3.
            options.put("currency", "INR");
            options.put("amount", String.valueOf(d * 100));//pass amount in currency subunits
            options.put("prefill.email", sessionHandler.getUserEmail());
            options.put("prefill.contact", sessionHandler.getLoggedInMobile());
            checkout.open(Subscription.this, options);

        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Checkout.clearUserData(getApplicationContext());
        Log.d("TAG", "onPaymentSuccess: " + s);
        Log.d("TAG", "onPaymentSuccess: " + Constant.SUBSCRIPTION_TYPE);

        JSONObject payment = new JSONObject();
        try {
            payment.put("id", Constant.PAYMENT_ID);
            payment.put("razorpay_payment_id", s);
            payment.put("razorpay_order_id", Constant.RZP_ORDER_ID);
            payment.put("razorpay_signature", "NA");
            payment.put("type", Constant.SUBSCRIPTION_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "onPaymentSuccess: " + payment);

        JSONObject walletAmount = new JSONObject();
        try {
            walletAmount.put("total", walletDeductedAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.postRequest(walletAmount, response -> Log.d("TAG", "onResponse: " + response), Server.DEDUCT_WALLET);
//        Log.d("TAG", "onPaymentSuccess: " + payment);
        api.postRequest(payment, response -> {
            Log.d("TAG", "onPaymentSuccess: " + response);
            loader.dismiss();
            paymentDetails.dismiss();
            int status = 0;
            try {
                status = response.getInt("status");
                if (status == 200) {

//                    validateSession();
                    Toast.makeText(getApplicationContext(), "Success full", Toast.LENGTH_SHORT).show();
                    Intent result = new Intent();
                    Bundle bundle = new Bundle();
//                    bundle.putString("id", id);
                    bundle.putString("payment", "TRUE");
                    result.putExtras(bundle);
                    setResult(Activity.RESULT_OK, result);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Payment failed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, Server.FEAUTRE_PURCHASE);

    }

    private void validateSession() {
        api.validateSession(res -> {
            try {
                JSONObject response = new JSONObject(res);
                int status = response.getInt("status");
                String message = response.getString("message");
                if (status == 200) {
                    //login success
                    JSONObject data = response.getJSONObject("data");
                    String _id = data.getString("id");
                    String _name = data.getString("fullName");
                    String _email = data.getString("email");
                    String _phone = data.getString("phone");
                    String _token = data.getString("accessToken"); // token
                    sessionHandler.setLoggedInMobile(_phone);
                    sessionHandler.setLoggedInUser(_id);
                    sessionHandler.setLoggedToken(_token);
                    sessionHandler.setUserName(_name);
                    sessionHandler.setLoggedInEmail(_email);
//                    sessionHandler.setLogin(true);
                    String isVerified = data.getString("isVerified");
                    String subscriptionType = data.getString("subscriptionType");
                    String subscriptionExpiryDate = data.getString("subscriptionExpiryDate");
                    Constant.PURCHASED_SUBSCRIPTION_TYPE = subscriptionType;
                    Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON = subscriptionExpiryDate;


                    Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent() ;
                    Intent result = new Intent();
                    Bundle bundle = new Bundle();
//                    bundle.putString("id", id);
                    bundle.putString("payment", "TRUE");
                    result.putExtras(bundle);
                    setResult(Activity.RESULT_OK, result);
                    finish();



                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Checkout.clearUserData(getApplicationContext());
        loader.dismiss();
        Toast.makeText(getApplicationContext(), "Payment Error" + s, Toast.LENGTH_SHORT).show();
    }
}