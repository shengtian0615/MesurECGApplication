package com.wehealth.model.domain.enumutil;

public enum DoctorPackageStatus {
	edit("编辑"), // 新增的套餐，编辑中的套餐，用户不应该见到此状态。
	run("运行"), // 编辑好的套餐，调用套餐发布接口，用户可以看到。
	history("历史");// 历史记录。用户已经购买了某(pacId)套餐，然后医生修改了相应的套餐。pacId这个套餐变为历史记录，同时生成新的处于edit状态的套餐(新的pacId)。

	private String _text;

	private DoctorPackageStatus(String text) {
		_text = text;
	}

	public String getText() {
		return _text;
	}
}
