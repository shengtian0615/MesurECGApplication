package com.wehealth.mesurecg.activity;

import java.text.ParseException;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.adapter.BloodArrayAdapter;
import com.wehealth.mesurecg.utils.CommUtils;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.ToastUtil;
import com.wehealth.model.domain.enumutil.BloodType;
import com.wehealth.model.domain.enumutil.Gender;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.ContactPerson;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.interfaces.inter_register.WeHealthRegisteredUser;
import com.wehealth.model.util.DateUtils;
import com.wehealth.model.util.IDCardValidator;
import com.wehealth.model.util.NetWorkService;
import com.wehealth.model.util.RegexUtil;
import com.wehealth.model.util.StringUtil;

public class PersonalInfoActivity extends BaseActivity implements OnClickListener{
	
	private Button saveButton;
	private EditText nameText;
	private TextView dateBirthText, genderText;
	private EditText idCardText;
	private EditText phoneText;
	private EditText heightText;
	private EditText weightText;
	private EditText contactPersonText;
	private EditText contactPersonRelaText;
	private EditText contactPersonPhoneText;
	private CheckBox mh_1, mh_2,mh_3, mh_4, mh_5, mh_6;
	private String medicalHostory = null;
	private RegisteredUser registeredUser;
	private RelativeLayout titleLayout, pageLayout, set_ly;
	private int BLOOD_TYPE = 0;
	private String[] stringArray;
	private BloodArrayAdapter bloodAdapter;
	private Spinner bloodType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_infomation);
		stringArray = getResources().getStringArray(R.array.blood_type);
		bloodAdapter = new BloodArrayAdapter(this, stringArray);
		
		init();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	protected void init() {
		InitInstance();
		reflushBackgroud();
		InitViewListener();
		registeredUser = MeasurECGApplication.getInstance().getRegisterUser();
		if (registeredUser != null) {
			InitViewText(registeredUser);
		}
	}

	/** 更改主题风格  **/
	private void reflushBackgroud() {
			pageLayout.setBackgroundColor(getResources().getColor(R.color.page_background_color));
			titleLayout.setBackgroundColor(getResources().getColor(R.color.page_title_bar_color));
			set_ly.setBackgroundColor(getResources().getColor(R.color.set_edit_bg_1));

			nameText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			genderText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			dateBirthText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			phoneText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			idCardText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			contactPersonText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			heightText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			contactPersonRelaText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			contactPersonPhoneText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			weightText.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			mh_1.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			mh_2.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			mh_3.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			mh_4.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			mh_5.setTextColor(getResources().getColor(R.color.set_edit_text_1));
			mh_6.setTextColor(getResources().getColor(R.color.set_edit_text_1));
	}
	
	private void InitInstance(){
		pageLayout = (RelativeLayout) findViewById(R.id.backgroud);
		titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		set_ly = (RelativeLayout) findViewById(R.id.set_ly);
		nameText = (EditText) findViewById(R.id.editText_name);
		genderText = (TextView) findViewById(R.id.editText_gender);
		dateBirthText = (TextView) findViewById(R.id.editText_birthday);
		phoneText = (EditText) findViewById(R.id.editText_phone_number);
		idCardText = (EditText) findViewById(R.id.editText_id_card);
		contactPersonText = (EditText) findViewById(R.id.editText_contact_person);
		heightText = (EditText) findViewById(R.id.editText_height);
		contactPersonRelaText = (EditText) findViewById(R.id.editText_contact_relation);
		contactPersonPhoneText = (EditText) findViewById(R.id.editText_contact_person_phone);
		weightText = (EditText) findViewById(R.id.editText_weight);
	    saveButton = (Button) findViewById(R.id.btn_save);
	    bloodType = (Spinner) findViewById(R.id.patient_info_bloodtype);
	    bloodType.setAdapter(bloodAdapter);
	    
	    mh_1 = (CheckBox) findViewById(R.id.person_info_m_1);
		mh_2 = (CheckBox) findViewById(R.id.person_info_m_2);
		mh_3 = (CheckBox) findViewById(R.id.person_info_m_3);
		mh_4 = (CheckBox) findViewById(R.id.person_info_m_4);
		mh_5 = (CheckBox) findViewById(R.id.person_info_m_5);
		mh_6 = (CheckBox) findViewById(R.id.person_info_m_6);
	    
	    bloodType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				BLOOD_TYPE = paramInt;
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) { }
		});
	}
	
	private void InitViewListener(){
		saveButton.setOnClickListener(this);
	}
	
	private void InitViewText(RegisteredUser pat){
		if (pat==null) {
			ToastUtil.showShort(this, R.string.select_user);
			this.finish();
			return;
		}
		if (pat.getGender()!=null) {
			if (pat.getGender() == Gender.male) {
				genderText.setText(R.string.sex_male);
			}else {
				genderText.setText(R.string.sex_femal);
			}
		}
		nameText.setText(pat.getName());
		nameText.setEnabled(false);
		if (IDCardValidator.isValidateIDCard(pat.getIdCardNo())) {
			dateBirthText.setText(StringUtil.getBirthDay(pat.getIdCardNo()));
			dateBirthText.setEnabled(false);
		}else {
			dateBirthText.setText(DateFormat.format("yyyy-MM-dd", pat.getDateOfBirth()));
			dateBirthText.setEnabled(true);
		}
		idCardText.setText(pat.getIdCardNo());
		idCardText.setEnabled(false);
		phoneText.setText(pat.getCellPhone());

	    saveButton.setText(R.string.save);
	    if (registeredUser.getWeight()!=0) {
	    	weightText.setText(pat.getWeight()+"");
		}
	    if (registeredUser.getHeight()!=0) {
	    	heightText.setText(pat.getHeight()+"");
		}
	    medicalHostory = pat.getMedicalHistory();
	    if (medicalHostory != null) {
			if (medicalHostory.contains("高血压")) {
				mh_1.setChecked(true);
			}
			if (medicalHostory.contains("糖尿病")) {
				mh_2.setChecked(true);
			}
			if (medicalHostory.contains("心肌梗塞")) {
				mh_3.setChecked(true);
			}
			if (medicalHostory.contains("安装起搏器")) {
				mh_4.setChecked(true);
			}
			if (medicalHostory.contains("做过支架")) {
				mh_5.setChecked(true);
			}
			if (medicalHostory.contains("做过搭桥手术")) {
				mh_6.setChecked(true);
			}
		}
	    if (pat.getBloodType() == null) {
	    	bloodType.setSelection(0);
		}else if (pat.getBloodType() == BloodType.A) {
	    	bloodType.setSelection(1);
	    }else if (pat.getBloodType() == BloodType.B) {
	    	bloodType.setSelection(2);
	    }else if (pat.getBloodType() == BloodType.AB) {
	    	bloodType.setSelection(3);
	    }else if (pat.getBloodType() == BloodType.O) {
	    	bloodType.setSelection(4);
	    }
	    
	    if (pat.getEmergencyContact() != null) {
			contactPersonText.setText(pat.getEmergencyContact().getName());
			contactPersonRelaText.setText(pat.getEmergencyContact().getRelationship());
			contactPersonPhoneText.setText(pat.getEmergencyContact().getPhoneNumber());
		}
	}
	
	public void onSaveButtonClicked() {
		if (!NetWorkService.isNetWorkConnected(this)) {
			ToastUtil.showShort(this, R.string.net_failed);
			return;
		}
		
	    String name = nameText.getText().toString();
		String dateBirth = dateBirthText.getText().toString();
		String phone = phoneText.getText().toString();
		String idCard = idCardText.getText().toString();
		String contactPerson = contactPersonText.getText().toString();
		String height = heightText.getText().toString();
		String contactPersonRela = contactPersonRelaText.getText().toString();
		String contactPersonPhone = contactPersonPhoneText.getText().toString();
		String weight = weightText.getText().toString();	
	
		if (TextUtils.isEmpty(name)) {
			ToastUtil.showShort(this, R.string.name_isempty);
			return;
		}
		
		if (!StringUtil.verifyName(name)) {
			ToastUtil.showShort(this, R.string.name_is_en_zh);
			return;
		}
		
		if (!IDCardValidator.isValidateIDCard(idCard)) {
			ToastUtil.showShort(this, R.string.idcard_error);
			return;
		}
		
		if (BLOOD_TYPE==1) {
			registeredUser.setBloodType(BloodType.A);
		}else if (BLOOD_TYPE==2) {
			registeredUser.setBloodType(BloodType.B);
		}else if (BLOOD_TYPE==3) {
			registeredUser.setBloodType(BloodType.AB);
		}else if (BLOOD_TYPE==4) {
			registeredUser.setBloodType(BloodType.O);
		}else if (BLOOD_TYPE==0) {
			registeredUser.setBloodType(null);
		}
		
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.showShort(this, R.string.phone_number_is_empty);
			return;
		}
		if (!RegexUtil.phone(phone)) {
			ToastUtil.showShort(this, R.string.phone_num_error);
			return;
		}
		
		registeredUser.setCellPhone(phone);
		try {
			registeredUser.setDateOfBirth(DateUtils.sdf_yyyy_MM_dd.parse(dateBirth));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(height)) {
			char[] length = height.replace("cm", "").toCharArray();
			if (length.length > 4 || length.length<0) {
				ToastUtil.showShort(this, R.string.heigh_error);
				return;
			}
			int h = Integer.parseInt(height.replace("cm", ""));
			registeredUser.setHeight(h);
		}else {
			registeredUser.setHeight(0);
		}
		registeredUser.setIdCardNo(idCard);
		registeredUser.setName(name);
		if (!TextUtils.isEmpty(weight)) {
			char[] length = weight.replace("kg", "").toCharArray();
			if (length.length>4 || length.length<0) {
				ToastUtil.showShort(this, R.string.weight_error);
				return;
			}
			registeredUser.setWeight(Integer.parseInt(weight.replace("kg", "")));
		}else {
			registeredUser.setWeight(0);
		}
		medicalHostory=null;
		if (mh_1.isChecked()) {
			medicalHostory+=" 高血压  ";
		}
		if (mh_2.isChecked()) {
			medicalHostory+=" 糖尿病  ";
		}
		if (mh_3.isChecked()) {
			medicalHostory+=" 心肌梗塞  ";
		}
		if (mh_4.isChecked()) {
			medicalHostory+=" 安装起搏器  ";
		}
		if (mh_5.isChecked()) {
			medicalHostory+=" 做过支架  ";
		}
		if (mh_6.isChecked()) {
			medicalHostory+=" 做过搭桥手术  ";
		}
		registeredUser.setMedicalHistory(medicalHostory);
		registeredUser.setUpdateTime(new Date());
		if (TextUtils.isEmpty(contactPerson)) {
			ToastUtil.showShort(this, R.string.emergency_empty);
			return;
		}
		if (!StringUtil.verifyName(contactPerson)) {
			ToastUtil.showShort(this, R.string.emergency_empty_cn_en);
			return;
		}
		if (TextUtils.isEmpty(contactPersonRela)) {
			ToastUtil.showShort(this, R.string.emergency_contant_empty);
			return;
		}
		if (TextUtils.isEmpty(contactPersonPhone)) {
			ToastUtil.showShort(this, R.string.emergency_phone_is_empty);
			return;
		}
		if (!RegexUtil.phone(contactPersonPhone)) {
			ToastUtil.showShort(this, R.string.emergency_phone_error);
			return;
		}
		if (registeredUser.getEmergencyContact() != null) {
			registeredUser.getEmergencyContact().setName(contactPerson);
			registeredUser.getEmergencyContact().setRelationship(contactPersonRela);
			registeredUser.getEmergencyContact().setPhoneNumber(contactPersonPhone);
		}else {
			ContactPerson cp = new ContactPerson();
			cp.setName(contactPerson);
			cp.setRelationship(contactPersonRela);
			cp.setPhoneNumber(contactPersonPhone);
			registeredUser.setEmergencyContact(cp);
		}
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		String promptStr = getString(R.string.update_user_info);
		pd.setMessage(promptStr);
		pd.show();
		new ReigsterPatientOnServer(pd).execute(registeredUser);
	}
	
	
	private class ReigsterPatientOnServer extends
			AsyncTask<RegisteredUser, Void, RegisteredUser> {
		ProgressDialog pd;


		public ReigsterPatientOnServer(ProgressDialog p) {
			pd = p;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected RegisteredUser doInBackground(RegisteredUser... params) {



			RegisteredUser ret;
			try {
				AuthToken token = CommUtils.refreshToken();
				if(token == null) return null;
				ret = NetWorkService.createApi(WeHealthRegisteredUser.class, PreferUtils.getIntance().getServerUrl()).updateRegisteredUser("Bearer " + token.getAccess_token(), params[0], null, null);
			} catch (Exception e) {
				return null;
			}
			return ret;
		}

		@Override
		protected void onPostExecute(final RegisteredUser patient) {
			pd.dismiss();
			if (patient == null) {
				ToastUtil.showShort(PersonalInfoActivity.this, R.string.update_failed);
				return;
			}
			ToastUtil.showShort(PersonalInfoActivity.this, "更新成功");
		}
	}

	public void onBackBottonClick(View view){
	      finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			onSaveButtonClicked();
			break;

		default:
			break;
		}
	}
	
}
