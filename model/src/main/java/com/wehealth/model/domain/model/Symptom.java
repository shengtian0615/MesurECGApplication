/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 17, 2015
 */

package com.wehealth.model.domain.model;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum Symptom implements NamedObject {
	s1("突然发生剧烈而持久的胸骨后或心前区压榨性疼痛，持续3分钟以上，并向左上肢及颈部或上腹部放射；"), s2(
			"心绞痛性质较以往发生改变、时间延长，使用硝酸甘油不易缓解；"), s3(
			"疼痛伴有恶心、呕吐、大汗、烦躁不安、恐惧或濒死感、明显心动过缓或过速；"), s4("心绞痛发作时伴气短、呼吸困难；"),

	;

	private final String text;

	private Symptom(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}
}
