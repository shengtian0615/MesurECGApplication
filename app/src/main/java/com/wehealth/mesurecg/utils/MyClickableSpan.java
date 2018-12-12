package com.wehealth.mesurecg.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.wehealth.model.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;

public class MyClickableSpan extends ClickableSpan {
	private Context ctx;
	private String text;
	private int color;
    public MyClickableSpan(Context context, String text, int col) {
        super();
        this.text = text;
        this.ctx=context;
        this.color = col;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
    	ds.setColor(color);
        ds.setUnderlineText(true);
//    	ds.setAlpha(100);
    }

    @Override
    public void onClick(View widget) {
    	try {
    		if(widget instanceof TextView)
    			((TextView)widget).setHighlightColor(ctx.getResources().getColor(android.R.color.transparent));
		} catch (Exception e) { }
    	String str = text.trim();
    	String result[] = new String[2];
    	if (str.equals("ST & T异常")) {
    		result[0] = "二";
    		result[1] = "ST-T异常分为原发和继发性、特异性和非特异性等。情况比较复杂，是缺血性心脏病的常见表现。";
		}else {
			InputStream is = null;
			try {
				is = ctx.getAssets().open("ecgdata_resultadvice.xml");
				result = FileUtil.parseResultAdvice(str, is);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (is!=null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
    	DialogHelp.getMyMessageDialog(ctx, text, result).show();
    }

}
